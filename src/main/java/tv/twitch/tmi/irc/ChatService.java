package tv.twitch.tmi.irc;

import com.frankerfacez.FrankerFaceZ;
import lombok.Getter;
import net.betterttv.BetterTTV;
import tv.twitch.tmi.events.IListener;
import tv.twitch.tmi.handle.impl.events.irc.channel.ChannelJoinEvent;
import tv.twitch.tmi.handle.impl.events.irc.channel.ChannelLeaveEvent;
import tv.twitch.tmi.handle.impl.events.irc.channel.ChatClearEvent;
import tv.twitch.tmi.handle.impl.events.irc.channel.message.*;
import tv.twitch.tmi.handle.impl.events.irc.channel.user.UserBanEvent;
import tv.twitch.tmi.handle.impl.events.irc.channel.user.UserTimeoutEvent;
import tv.twitch.tmi.handle.impl.events.irc.status.*;
import tv.twitch.tmi.handle.impl.events.irc.raw.RawDataEvent;
import tv.twitch.tmi.handle.impl.obj.Emote;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.handle.impl.obj.irc.Message;
import tv.twitch.tmi.handle.impl.obj.irc.User;
import tv.twitch.utils.Parser;
import tv.twitch.utils.Utils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChatService extends Thread {
	public static final String IP = "irc.chat.twitch.tv";
	public static final int PORT = 6697;
	
	private SocketFactory SocketFactory = SSLSocketFactory.getDefault();
	private Socket Socket;
	private BufferedWriter Writer;
	private BufferedReader Reader;
	
	@Getter private TwitchTMI TMI;
	@Getter private User clientUser;
	@Getter private HashMap<String, Channel> connectedChannels = new HashMap<>();
	
	@Getter private List<BetterTTV.Emote> globalBTTVEmotes;
	@Getter private List<FrankerFaceZ.Emote> globalFFZEmotes;
	
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
					System.out.println("TwitchIRC: < "+ line.substring(0, line.length() - 2));
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
				
				BetterTTV();
				FrankerFaceZ();
				
				String line = null;
				while(this.isConnected() && ((line = this.Reader.readLine()) != null)) {
					if(this.TMI.getClient().getSettings().getVerbose().getLevel() > 0)
						System.out.println("TwitchIRC: > "+ line);
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
	
	public void BetterTTV() throws IOException {
		this.globalBTTVEmotes = BetterTTV.getGlobalEmotes().getEmotes();
	}
	
	public void FrankerFaceZ() throws IOException {
		List<FrankerFaceZ.Emote> emotes = new ArrayList<>();
		for(FrankerFaceZ.Set set : FrankerFaceZ.getDefaultSet().getSets().values())
			for(FrankerFaceZ.Emote emote : set.getEmoticons())
				emotes.add(emote);
		this.globalFFZEmotes = emotes;
	}
	
	@Getter
	protected class ChatServiceRawDataEventListener implements IListener<RawDataEvent> {
		private ChatService chatService;
		
		public void handle(RawDataEvent event) throws Exception {
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
								this.getChatService().clientUser = new User(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername().toLowerCase(), event.getRawData().getTags());
								if(!this.getChatService().isReady()) {
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ReadyEvent(this.getChatService().getTMI()));
									this.getChatService().ready = true;
								}
							}
						break;
						
						case "NOTICE":
							{
								boolean enabled = false;
								switch(msgid) {
									//TODO: SubOnly Mode, EmoteOnly Mode, etc.
								}
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
								Channel c = this.getChatService().getTMI().getChannel(channel);
								if(event.getRawData().getTags().containsKey("ban-reason")) {
									int userId = Integer.parseInt(event.getRawData().getTags().getOrDefault("target-user-id", "-1"));
									String reason = event.getRawData().getTags().getOrDefault("ban-reason", null);
									if(event.getRawData().getTags().containsKey("ban-duration")) {
										int duration = Integer.parseInt(event.getRawData().getTags().get("ban-duration"));
										this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new UserTimeoutEvent(this.getChatService().getTMI(), c, userId, msg, reason, duration));
									}
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new UserBanEvent(this.getChatService().getTMI(), c, userId, msg, reason, !event.getRawData().getTags().containsKey("ban-duration")));
								} else
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ChatClearEvent(this.getChatService().getTMI(), c));
							}
						break;
						
						case "RECONNECT":
							this.getChatService().reconnect();
						break;
						
						case "USERSTATE":
							{
								this.getChatService().getTMI().getChannel(channel).setUserState(new User(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername().toLowerCase(), event.getRawData().getTags()));
								
								if(!event.getRawData().getTags().getOrDefault("emote-sets", "").isEmpty())
									this.getChatService().getTMI().getChannel(channel).setEmoteSets(Arrays.asList(event.getRawData().getTags().get("emote-sets").split(",")));
							}
						break;
						
						case "GLOBALUSERSTATE":
							this.getChatService().clientUser = new User(this.getChatService().getTMI(), this.getChatService().getTMI().getClient().getUsername().toLowerCase(), event.getRawData().getTags());
							if(!this.getChatService().isReady()) {
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new AuthenticationEvent(this.getChatService().getTMI()));
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ReadyEvent(this.getChatService().getTMI()));
								this.getChatService().ready = true;
							}
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
							{
								User sender = new User(this.getChatService().getTMI(), username, event.getRawData().getTags());
								Channel c = this.getChatService().getTMI().getChannel("jtv");
								Message message = new Message(this.getChatService().getTMI(), c, sender, msg, Message.Type.WHISPER, event.getRawData().getTags());
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new WhisperEvent(this.getChatService().getTMI(), sender, message, false));
								this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new MessageEvent(this.getChatService().getTMI(), c, sender, message, false));
							}
						break;
						
						case "PRIVMSG":
							if(username.equalsIgnoreCase("jtv")) {
								//TODO: Handle host message
							} else {
								Channel c = this.getChatService().getTMI().getChannel(channel);
								User sender = new User(this.getChatService().getTMI(), username.toLowerCase(), event.getRawData().getTags());
								
								Message.Type type = Message.Type.CHAT;
								Message message = null;
								if(msg.matches("^\\u0001ACTION ([^\\u0001]+)\\u0001$")) {
									type = Message.Type.ACTION;
									message = new Message(this.getChatService().getTMI(), c, sender, msg, type, event.getRawData().getTags());
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ActionEvent(this.getChatService().getTMI(), c, sender, message, false));
								}
								
								if(!event.getRawData().getTags().getOrDefault("bits", "").isEmpty()) {
									type = Message.Type.CHEER;
									message = new Message(this.getChatService().getTMI(), c, sender, msg, type, event.getRawData().getTags());
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new CheerEvent(this.getChatService().getTMI(), c, sender, message, Integer.parseInt(event.getRawData().getTags().get("bits")), false));
								}
								
								if(type.equals(Message.Type.CHAT)) {
									message = new Message(this.getChatService().getTMI(), c, sender, msg, type, event.getRawData().getTags());
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new ChatEvent(this.getChatService().getTMI(), c, sender, message, false));
								}
								
								if(message != null)
									this.getChatService().getTMI().getClient().getEventDispatcher().dispatch(new MessageEvent(this.getChatService().getTMI(), c, sender, message, false));
							}
						break;
					}
				}
			}
		}
		
		ChatServiceRawDataEventListener(ChatService chatService) { this.chatService = chatService; }
	}
}
