package tv.twitch.tmi;

import lombok.Getter;
import lombok.Setter;
import tv.twitch.tmi.events.obj.*;
import tv.twitch.tmi.events.EventListener;
import tv.twitch.tmi.exception.ChannelJoinFailureException;
import tv.twitch.tmi.exception.ChannelLeaveFailureException;
import tv.twitch.tmi.exception.MessageSendFailureException;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.RawData;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TwitchTMI {
	private ChatService Chat;
	
	@Getter private String IP;
	@Getter private int port;
	
	@Getter private HashMap<String, Channel> connectedChannels;
	@Getter @Setter private EventListener eventListener;
	@Getter @Setter private String username;
	@Getter @Setter private String oAuth;
	@Getter @Setter private boolean verbose;
	
	public TwitchTMI() {
		this.IP = "irc.chat.twitch.tv";
		this.port = 6667;
		
		this.connectedChannels = new HashMap<String, Channel>();
		this.eventListener = new EventListener() {};
		this.verbose = false;
	}
	
	/**
	 * Connects to the Twitch IRC Server
	 */
	public void connect() {
		this.Chat = new ChatService(this);
		this.Chat.start();
	}
	
	/**
	 * Returns a channel object
	 *
	 * @param channel
	 * @return
	 */
	public Channel getChannel(String channel) {
		channel = channel.toLowerCase();
		if(channel.startsWith("#"))
			channel = channel.replaceFirst("#", "");
		if(this.getConnectedChannels().containsKey(channel))
			return this.getConnectedChannels().get(channel);
		return new Channel(this, channel, false);
	}
	
	/**
	 * Sends a whisper to the given user
	 *
	 * @param recipient
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void sendWhisper(String recipient, String message) throws MessageSendFailureException {
		try {
			if(!this.isConnected())
				throw new IOException("Unable to send whisper!");
			this.Chat.sendRawData("PRIVMSG #jtv :/w "+ recipient.toLowerCase() +" "+ message);
		} catch(IOException e) {
			throw new MessageSendFailureException("Something went wrong while sending your whisper!");
		}
	}
	
	/**
	 * Sends the provided raw messages to the IRC Server
	 *
	 * @param data
	 * @throws Exception
	 */
	public void sendRawData(String... data) throws Exception { this.Chat.sendRawData(data); }
	
	/**
	 * Returns true if logged in to the Twitch IRC Service
	 *
	 * @return
	 */
	public boolean isConnected() {
		if(this.Chat == null)
			return false;
		return this.Chat.connected;
	}
	
	/**
	 * Disconnects from the Twitch IRC Service and ends the thread
	 */
	public void close() {
		this.Chat.interrupt();
	}
	
	private class ChatService extends Thread {
		private Socket Socket;
		private BufferedWriter Writer;
		private BufferedReader Reader;
		
		private TwitchTMI TMI;
		private boolean connected;
		
		private ChatService(TwitchTMI TMI) {
			this.TMI = TMI;
			this.connected = false;
		}
		
		@Override
		public void run() {
			try {
				this.Socket = new Socket(this.TMI.getIP(), this.TMI.getPort());
				this.Writer = new BufferedWriter(new OutputStreamWriter(this.Socket.getOutputStream()));
				this.Reader = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
				
				this.sendRawData(
					"PASS "+ this.TMI.getOAuth(),
					"NICK "+ this.TMI.getUsername()
				);
				
				String line = null;
				while((line = this.Reader.readLine()) != null) {
					if(this.TMI.isVerbose())
						System.out.println(line);
					try {
						RawData rawData = Parser.msg(line);
						this.handle(rawData);
					} catch(Exception e) { e.printStackTrace(); }
				}
			} catch(Exception e) {
				e.printStackTrace();
				this.connected = false;
				this.interrupt();
			}
		}
		
		/**
		 * Sends a raw IRC message to the Twitch IRC Service
		 *
		 * @param data
		 * @throws IOException
		 */
		private void sendRawData(String... data) throws IOException {
			for(int i = 0; i < data.length; i++) {
				String line = new String(data[i].getBytes(), "UTF-8");
				if(!line.endsWith("\r\n"))
					line += "\r\n";
				this.Writer.write(line);
			}
			this.Writer.flush();
		}
		
		private void handle(RawData rawData) throws Exception {
			if(!Utils.isNull(rawData)) {
				String channel = rawData.getParams().get(0).replaceFirst("#", "").toLowerCase();
				String msg = (rawData.getParams().size()>1)? rawData.getParams().get(1) : "";
				String msgid = rawData.getTags().getOrDefault("msg-id", "").toUpperCase();
				
				if(Utils.isNull(rawData.getPrefix())) {
					switch(rawData.getCommand().toUpperCase()) {
						case "PING":
							this.sendRawData("PONG " + rawData.getData().substring(5));
							this.TMI.getEventListener().onPing(new PingEvent(this.TMI, rawData));
						break;
					}
				} else if(rawData.getPrefix().equalsIgnoreCase("tmi.twitch.tv")) {
					switch(rawData.getCommand().toUpperCase()) {
						case "002":
						case "003":
						case "004":
						case "375":
						case "376":
						case "CAP":
							break;
						
						case "001":
							this.TMI.username = rawData.getParams().get(0);
						break;
						
						case "372":
							this.sendRawData(
								"CAP REQ :twitch.tv/membership",
								"CAP REQ :twitch.tv/tags",
								"CAP REQ :twitch.tv/commands"
							);
							this.connected = true;
							this.TMI.getEventListener().onConnect(new ConnectEvent(this.TMI));
						break;
						
						case "NOTICE":
							boolean enabled = false;
							switch(msgid) {
								case "SUBS_ON":
								case "SUBS_OFF":
									if(msgid.equalsIgnoreCase("SUBS_ON"))
										enabled = true;
									this.TMI.getEventListener().onSubMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.SUBSCRIBER));
									this.TMI.getEventListener().onChannelMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.SUBSCRIBER));
								break;
								
								case "EMOTE_ONLY_ON":
								case "EMOTE_ONLY_OFF":
									if(msgid.equalsIgnoreCase("EMOTE_ONLY_ON"))
										enabled = true;
									this.TMI.getEventListener().onEmoteMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.EMOTE));
									this.TMI.getEventListener().onChannelMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.EMOTE));
								break;
								
								case "R9K_ON":
								case "R9K_OFF":
									if(msgid.equalsIgnoreCase("R9K_ON"))
										enabled = true;
									this.TMI.getEventListener().onR9KMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.R9K));
									this.TMI.getEventListener().onChannelMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.R9K));
								break;
								
								case "ROOM_MODS":
								
								break;
							}
						break;
						
						case "USERNOTICE":
							switch(msgid) {
							
							}
						break;
						
						case "HOSTTARGET":
							break;
						
						case "CLEARCHAT":
							
							break;
						
						case "RECONNECT":
							break;
						
						case "SERVERCHANGE":
							break;
						
						case "USERSTATE":
							break;
						
						case "GLOBALUSERSTATE":
							break;
						
						case "ROOMSTATE":
							break;
					}
				} else if(rawData.getPrefix().equalsIgnoreCase("jtv")) {
					switch(rawData.getCommand().toUpperCase()) {
						case "MODE":
							switch(msg.toUpperCase()) {
								case "+O":
									if(!this.TMI.getChannel(channel).getMods().contains(rawData.params.get(2).toLowerCase()))
										this.TMI.getChannel(channel).getMods().add(rawData.params.get(2).toLowerCase());
								break;
								
								case "-O":
									if(this.TMI.getChannel(channel).getMods().contains(rawData.params.get(2).toLowerCase()))
										this.TMI.getChannel(channel).getMods().remove(rawData.params.get(2).toLowerCase());
								break;
							}
						break;
					}
				} else {
					String username = rawData.getPrefix().split("!")[0].toLowerCase();
					switch(rawData.getCommand().toUpperCase()) {
						case "353":
							//List of users in chat
						break;
						
						case "366":
							break;
						
						case "JOIN":
							if(username.equalsIgnoreCase(this.TMI.getUsername()))
								if(!this.TMI.getConnectedChannels().containsKey(channel))
									this.TMI.getConnectedChannels().put(channel, new Channel(this.TMI, channel, true));
							this.TMI.getEventListener().onChannelJoin(new ChannelJoinEvent(this.TMI, rawData, this.TMI.getChannel(channel), username, username.equalsIgnoreCase(this.TMI.getUsername())));
						break;
						
						case "PART":
							if(username.equalsIgnoreCase(this.TMI.getUsername()))
								if(this.TMI.getConnectedChannels().containsKey(channel))
									this.TMI.getConnectedChannels().remove(channel);
							this.TMI.getEventListener().onChannelLeave(new ChannelLeaveEvent(this.TMI, rawData, this.TMI.getChannel(channel), username, username.equalsIgnoreCase(this.TMI.getUsername())));
						break;
						
						case "WHISPER":
							if(!rawData.tags.containsKey("username"))
								rawData.tags.put("username", rawData.prefix.split("!")[0]);
							this.TMI.getEventListener().onWhisper(new MessageEvent(this.TMI, rawData, this.TMI.getChannel(channel), rawData.tags.get("username").toLowerCase(), Message.MessageType.WHISPER));
						break;
						
						case "PRIVMSG":
							if(username.equalsIgnoreCase("jtv")) {
								if(msg.toLowerCase().contains("hosting you")) {
									int count = 0;
									if(msg.toLowerCase().contains("hosting you for"))
										count = Utils.extractNumber(msg);
									this.TMI.getEventListener().onHosted(new HostEvent(this.TMI, rawData, this.TMI.getChannel(channel), Utils.username(msg.split(" ")[0]), count, msg.toLowerCase().contains("auto")));
								}
							} else {
								if(msg.matches("^\\u0001ACTION ([^\\u0001]+)\\u0001$")) {
									MessageEvent message = new MessageEvent(this.TMI, rawData, this.TMI.getChannel(channel), username, Message.MessageType.ACTION);
									this.TMI.getEventListener().onAction(message);
									this.TMI.getEventListener().onMessage(message);
								} else {
									if(rawData.getTags().containsKey("bits")) {
										Message message = new Message(this.TMI, rawData, this.TMI.getChannel(channel), username, Message.MessageType.CHEER);
										this.TMI.getEventListener().onCheer(new CheerEvent(this.TMI, rawData, message));
										this.TMI.getEventListener().onMessage(new MessageEvent(this.TMI, rawData, message));
									} else
										this.TMI.getEventListener().onMessage(new MessageEvent(this.TMI, rawData, this.TMI.getChannel(channel), username, Message.MessageType.MESSAGE));
								}
							}
						break;
					}
				}
			}
		}
	}
}
