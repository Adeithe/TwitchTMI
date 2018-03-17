package tv.twitch.tmi;

import lombok.Getter;
import lombok.Setter;
import tv.twitch.tmi.events.obj.*;
import tv.twitch.tmi.events.EventListener;
import tv.twitch.tmi.exception.ChannelJoinFailureException;
import tv.twitch.tmi.exception.ChannelLeaveFailureException;
import tv.twitch.tmi.exception.MessageSendFailureException;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.RawData;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TwitchTMI {
	private ChatService Chat;
	private List<String> channels;
	
	@Getter private String IP;
	@Getter private int port;
	
	@Getter private EventListener eventListener;
	@Getter @Setter private String username;
	@Getter @Setter private String oAuth;
	@Getter @Setter private boolean verbose;
	
	public TwitchTMI() {
		this.IP = "irc.chat.twitch.tv";
		this.port = 6667;
		this.verbose = false;
		this.channels = new ArrayList<String>();
		this.eventListener = new EventListener() {};
	}
	
	/**
	 * Connects to the Twitch IRC Server
	 */
	public void connect() {
		this.Chat = new ChatService(this.IP, this.port, this.username, this.oAuth, this.eventListener, this.verbose);
		this.Chat.start();
	}
	
	/**
	 * Update the clients EventListener
	 *
	 * @param listener
	 */
	public void setEventListener(EventListener listener) {
		this.eventListener = listener;
		if(this.Chat != null)
			this.Chat.eventListener = this.eventListener;
	}
	
	/**
	 * Joins the provided Twitch Chat and prepares the client to send messages
	 * This <b>MUST</b> be done after ConnectEvent has been fired
	 *
	 * @param channel
	 * @throws ChannelJoinFailureException
	 */
	public void join(String channel) throws ChannelJoinFailureException {
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.isConnected() || this.channels.contains(channels))
				throw new IOException("Unable to join channel!");
			
			this.Chat.sendRawData("JOIN " + channel);
			this.channels.add(channel);
		} catch(IOException e) {
			throw new ChannelJoinFailureException("Something went wrong while joining the channel!");
		}
	}
	
	/**
	 * Sends the provided message to the channel
	 * Client <b>MUST</b> join the channel before sending messages
	 *
	 * @param channel
	 * @param message
	 */
	public void sendMessage(String channel, String message) throws MessageSendFailureException {
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.isConnected() || !this.channels.contains(channel))
				throw new IOException("Unable to send message!");
			
			this.Chat.sendRawData("PRIVMSG "+ channel +" :"+ message);
		} catch(IOException e) {
			throw new MessageSendFailureException("Something went wrong while sending your message!");
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
	 * Leaves the provided Twitch Chat removing the ability to chat there until rejoined
	 *
	 * @param channel
	 * @throws ChannelLeaveFailureException
	 */
	public void leave(String channel) throws ChannelLeaveFailureException {
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.isConnected() || !this.channels.contains(channels))
				throw new IOException("Unable to leave channel!");
			
			this.Chat.sendRawData("PART " + channel);
			this.channels.remove(channel);
		} catch(IOException e) {
			throw new ChannelLeaveFailureException("Something went wrong while leaving the channel!");
		}
	}
	
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
		
		private String ip;
		private int port;
		private String username;
		private String oAuth;
		private EventListener eventListener;
		private boolean verbose;
		
		private boolean connected;
		
		private ChatService(String ip, int port, String username, String oAuth, EventListener eventListener, boolean verbose) {
			this.ip = ip;
			this.port = port;
			this.username = username;
			this.oAuth = oAuth;
			this.eventListener = eventListener;
			this.verbose = verbose;
			this.connected = false;
		}
		
		@Override
		public void run() {
			try {
				this.Socket = new Socket(this.ip, this.port);
				this.Writer = new BufferedWriter(new OutputStreamWriter(this.Socket.getOutputStream()));
				this.Reader = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
				
				this.sendRawData(
					"PASS "+ this.oAuth,
					"NICK "+ this.username
				);
				
				String line = null;
				while((line = this.Reader.readLine()) != null) {
					if(this.verbose)
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
				String channel = rawData.getParams().get(0).replaceFirst("#", "");
				String msg = (rawData.getParams().size()>1)? rawData.getParams().get(1) : "";
				String msgid = rawData.getTags().getOrDefault("msg-id", "").toUpperCase();
				
				if(Utils.isNull(rawData.getPrefix())) {
					switch(rawData.getCommand().toUpperCase()) {
						case "PING":
							this.sendRawData("PONG " + rawData.getData().substring(5));
							this.eventListener.onPing(new PingEvent(rawData));
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
							this.username = rawData.getParams().get(0);
						break;
						
						case "372":
							this.sendRawData(
								"CAP REQ :twitch.tv/membership",
								"CAP REQ :twitch.tv/tags",
								"CAP REQ :twitch.tv/commands"
							);
							this.connected = true;
							this.eventListener.onConnect(new ConnectEvent(this.ip, this.port, this.username));
						break;
						
						case "NOTICE":
							boolean enabled = false;
							switch(msgid) {
								case "SUBS_ON":
								case "SUBS_OFF":
									if(msgid.equalsIgnoreCase("SUBS_ON"))
										enabled = true;
									this.eventListener.onSubMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.SUBSCRIBER));
									this.eventListener.onChannelMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.SUBSCRIBER));
								break;
								
								case "EMOTE_ONLY_ON":
								case "EMOTE_ONLY_OFF":
									if(msgid.equalsIgnoreCase("EMOTE_ONLY_ON"))
										enabled = true;
									this.eventListener.onEmoteMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.EMOTE));
									this.eventListener.onChannelMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.EMOTE));
								break;
								
								case "R9K_ON":
								case "R9K_OFF":
									if(msgid.equalsIgnoreCase("R9K_ON"))
										enabled = true;
									this.eventListener.onR9KMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.R9K));
									this.eventListener.onChannelMode(new ChannelModeEvent(rawData, channel, enabled, ChannelModeEvent.Mode.R9K));
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
							//The IRC Server did something
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
							this.eventListener.onChannelJoin(new ChannelJoinEvent(rawData, channel, username, username.equalsIgnoreCase(this.username)));
						break;
						
						case "PART":
							this.eventListener.onChannelLeave(new ChannelLeaveEvent(rawData, channel, username, username.equalsIgnoreCase(this.username)));
						break;
						
						case "WHISPER":
							//You've got mail!
						break;
						
						case "PRIVMSG":
							if(username.equalsIgnoreCase("jtv")) {
								if(msg.toLowerCase().contains("hosting you")) {
									int count = 0;
									if(msg.toLowerCase().contains("hosting you for"))
										count = Utils.extractNumber(msg);
									this.eventListener.onHosted(new HostEvent(rawData, channel, Utils.username(msg.split(" ")[0]), count, msg.toLowerCase().contains("auto")));
								}
							} else {
								if(msg.matches("^\\u0001ACTION ([^\\u0001]+)\\u0001$")) {
									MessageEvent message = new MessageEvent(rawData, username, Message.MessageType.ACTION);
									this.eventListener.onAction(message);
									this.eventListener.onMessage(message);
								} else {
									if(rawData.getTags().containsKey("bits")) {
										Message message = new Message(rawData, username, Message.MessageType.CHEER);
										this.eventListener.onCheer(new CheerEvent(rawData, message));
										this.eventListener.onMessage(new MessageEvent(rawData, message));
									} else
										this.eventListener.onMessage(new MessageEvent(rawData, username, Message.MessageType.MESSAGE));
								}
							}
						break;
					}
				}
			}
		}
	}
}
