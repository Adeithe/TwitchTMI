package tv.twitch.tmi;

import lombok.Getter;
import tv.twitch.events.IListener;
import tv.twitch.handle.impl.events.tmi.channel.ChannelJoinEvent;
import tv.twitch.handle.impl.events.tmi.channel.ChannelLeaveEvent;
import tv.twitch.handle.impl.events.tmi.status.ConnectEvent;
import tv.twitch.handle.impl.events.tmi.PingEvent;
import tv.twitch.handle.impl.events.tmi.raw.RawDataEvent;
import tv.twitch.handle.impl.events.tmi.status.DisconnectEvent;
import tv.twitch.handle.impl.events.tmi.status.ReadyEvent;
import tv.twitch.handle.impl.events.tmi.status.ReconnectEvent;
import tv.twitch.handle.impl.obj.tmi.Channel;
import tv.twitch.handle.impl.obj.tmi.ClientUser;
import tv.twitch.utils.Parser;
import tv.twitch.utils.Utils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ChatService extends Thread {
	public static final String IP = "irc.chat.twitch.tv";
	public static final int PORT = 6697;
	
	private SocketFactory SocketFactory = SSLSocketFactory.getDefault();
	private Socket Socket;
	private BufferedWriter Writer;
	private BufferedReader Reader;
	
	@Getter private TwitchTMI TMI;
	@Getter private ClientUser clientUser;
	@Getter private HashMap<String, Channel> connectedChannels = new HashMap<>();
	
	@Getter private boolean connected;
	@Getter private boolean ready;
	private boolean reconnect;
	
	ChatService(TwitchTMI TMI) {
		this.TMI = TMI;
		this.TMI.getClient().getEventDispatcher().registerListener(new ChatServiceRawDataEventListener(this));
	}
	
	@Override
	public void run() { this.onStart(); }
	
	void sendRawData(String... data) throws IOException {
		if(this.connected) {
			for(int i = 0; i < data.length; i++) {
				String line = new String(data[i].getBytes(), "UTF-8");
				if(!line.endsWith("\r\n"))
					line += "\r\n";
				if(this.TMI.getClient().getSettings().getVerbose().getLevel() > 1)
					System.out.println("< "+ line.substring(0, line.length() - 2));
				this.Writer.write(line);
			}
			this.Writer.flush();
		}
	}
	
	boolean connect() {
		try {
			this.Socket = SocketFactory.createSocket(IP, PORT);
			this.Writer = new BufferedWriter(new OutputStreamWriter(this.Socket.getOutputStream()));
			this.Reader = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
			this.connected = true;
		} catch(IOException e) {
			this.disconnect();
		}
		return this.connected;
	}
	
	void disconnect() {
		this.reconnect = false;
		this.connected = false;
		this.ready = false;
	}
	
	void reconnect() {
		this.reconnect = true;
		this.connected = false;
		this.ready = false;
	}
	
	private void onStart() {
		try {
			this.reconnect = false;
			if(this.connect()) {
				this.sendRawData(
						"CAP REQ :twitch.tv/membership",
						"CAP REQ :twitch.tv/tags",
						"CAP REQ :twitch.tv/commands",
						
						"PASS " + this.getTMI().getClient().getOAuth(),
						"NICK " + this.getTMI().getClient().getUsername()
				);
				
				String line = null;
				while(this.isConnected() && ((line = this.Reader.readLine()) != null)) {
					if(this.TMI.getClient().getSettings().getVerbose().getLevel() > 0)
						System.out.println("> "+ line);
					this.TMI.getClient().getEventDispatcher().dispatch(new RawDataEvent(this.getTMI(), Parser.msg(line)));
				}
				
				this.Socket.close();
				this.Writer.close();
				this.Reader.close();
				this.onClose();
			}
		} catch(IOException e) {
			System.out.println("Failed to read line!");
		}
	}
	
	private void onClose() {
		this.getTMI().getClient().getEventDispatcher().dispatch(new DisconnectEvent(this.getTMI(), this.reconnect));
		if(this.reconnect) {
			this.getTMI().getClient().getEventDispatcher().dispatch(new ReconnectEvent(this.getTMI()));
			this.onStart();
		}
	}
	
	@Getter
	protected class ChatServiceRawDataEventListener implements IListener<RawDataEvent> {
		private ChatService chatService;
		
		public void handle(RawDataEvent event) {
			if(!Utils.isNull(event.getRawData())) {
				String channel = (event.getRawData().getParams().size()>0)? event.getRawData().getParams().get(0).replaceFirst("#", "").toLowerCase() : null;
				String msg = (event.getRawData().getParams().size()>1)? event.getRawData().getParams().get(1) : "";
				String msgid = event.getRawData().getTags().getOrDefault("msg-id", "").toUpperCase();
				
				if(Utils.isNull(event.getRawData().getPrefix())) {
					switch(event.getRawData().getCommand().toUpperCase()) {
						case "PING":
							try {
								this.getChatService().sendRawData("PONG " + event.getRawData().getData().substring(5));
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new PingEvent(this.getChatService().getTMI()));
							} catch(IOException e) {
								e.printStackTrace();
							}
						break;
					}
				} else if(event.getRawData().getPrefix().equalsIgnoreCase("tmi.twitch.tv")) {
					switch(event.getRawData().getCommand().toUpperCase()) {
						case "372":
							this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ConnectEvent(this.getChatService().getTMI(), ChatService.IP, ChatService.PORT));
						break;
						
						case "376":
							if(this.getChatService().getTMI().isAnonymous()) {
								this.getChatService().clientUser = new ClientUser(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername());
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ReadyEvent(this.getChatService().getTMI()));
							}
						break;
						
						case "NOTICE":
							switch(msgid) {
								//TODO: SubOnly Mode, EmoteOnly Mode, etc.
							}
						break;
						
						case "USERNOTICE":
							switch(msgid) {
								//TODO: Sub, Resub, Bits, etc.
							}
						break;
						
						case "HOSTTARGET":
							{
								//TODO: Hosts
							}
						break;
						
						case "CLEARCHAT":
							{
								//TODO: Handle bans, timeouts, etc
							}
						break;
						
						case "RECONNECT":
							this.getChatService().reconnect();
						break;
						
						case "USERSTATE":
							this.getChatService().getTMI().getChannel(channel).setUserState(new ClientUser.UserState(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername().toLowerCase(), this.getChatService().getTMI().getChannel(channel), event.getRawData().getTags()));
						break;
						
						case "GLOBALUSERSTATE":
							this.getChatService().clientUser = new ClientUser(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername(), event.getRawData().getTags());
							this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ReadyEvent(this.getChatService().getTMI()));
						break;
						
						case "ROOMSTATE":
							{
								boolean isConnected = this.getChatService().getTMI().getChannel(channel).isConnected();
								this.getChatService().getConnectedChannels().put(channel, new Channel(this.getChatService().getTMI(), channel, event.getRawData().getTags(), true));
								if(!isConnected)
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ChannelJoinEvent(this.getChatService().getTMI(), this.getChatService().getTMI().getChannel(channel), this.getChatService().getTMI().getClient().getUsername().toLowerCase(), true));
							}
						break;
					}
				} else if(event.getRawData().getPrefix().equalsIgnoreCase("jtv")) {
					switch(event.getRawData().getCommand().toUpperCase()) {
						case "+O":
							break;
						
						case "-O":
							break;
					}
				} else {
					boolean isSelf = false;
					String username = event.getRawData().getPrefix().split("!")[0].toLowerCase();
					if(this.getChatService().getClientUser() != null && username.equalsIgnoreCase(this.getChatService().getClientUser().getUsername()))
						isSelf = true;
					switch(event.getRawData().getCommand().toUpperCase()) {
						case "JOIN":
							if(!isSelf)
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ChannelJoinEvent(this.getChatService().getTMI(), this.getChatService().getTMI().getChannel(channel),username, false));
						break;
						
						case "PART":
							if(isSelf && this.getChatService().getTMI().getChannel(channel).isConnected())
								this.getChatService().getConnectedChannels().remove(channel);
							this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ChannelLeaveEvent(this.getChatService().getTMI(), this.getChatService().getTMI().getChannel(channel), username, isSelf));
						break;
						
						case "WHISPER":
							//TODO: Whispers
						break;
						
						case "PRIVMSG":
							if(username.equalsIgnoreCase("jtv")) {
								//TODO: Handle host message
							} else {
								//TODO: Handle chat message
							}
						break;
					}
				}
			}
		}
		
		ChatServiceRawDataEventListener(ChatService chatService) { this.chatService = chatService; }
	}
}
