package tv.twitch.tmi;

import lombok.Getter;
import lombok.Setter;
import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.tmi.events.obj.*;
import tv.twitch.tmi.events.EventListener;
import tv.twitch.tmi.exception.MessageSendFailureException;
import tv.twitch.tmi.obj.*;
import tv.twitch.tmi.obj.extension.BetterTTV;
import tv.twitch.tmi.obj.extension.FrankerFaceZ;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class TwitchTMI {
	public static final String BTTV_BASE_URL = "https://api.betterttv.net/2/";
	public static final String FFZ_BASE_URL = "https://api.frankerfacez.com/v1/";
	
	private ChatService Chat;
	@Getter @Setter private TwitchAPI API;
	
	@Getter @Setter private String username;
	private String oAuth;
	
	@Getter private String IP;
	@Getter private int port;
	
	@Getter @Setter private int reconnectInterval;
	@Getter @Setter private EventListener eventListener;
	@Getter @Setter private boolean verbose;
	
	@Getter private ClientUser clientUser;
	@Getter private List<Channel> connectedChannels;
	
	@Getter private List<BetterTTV.Emote> globalBTTVEmotes;
	@Getter private List<FrankerFaceZ.Emote> globalFFZEmotes;
	
	public TwitchTMI() {
		this.IP = "irc.chat.twitch.tv";
		this.port = 6667;
		
		this.reconnectInterval = 5;
		this.eventListener = new EventListener() {};
		this.verbose = false;
		
		this.clientUser = null;
		this.connectedChannels = new ArrayList<Channel>();
		
		this.globalBTTVEmotes = new ArrayList<BetterTTV.Emote>();
		this.globalFFZEmotes = new ArrayList<FrankerFaceZ.Emote>();
	}
	
	public void setOAuth(String oAuth) {
		if(!oAuth.startsWith("oauth:"))
			oAuth = "oauth:"+ oAuth;
		this.oAuth = oAuth;
	}
	
	/**
	 * Creates an instance of TwitchAPI for you.
	 *
	 * @param clientID
	 * @param clientSecret
	 */
	public void setAPI(String clientID, String clientSecret) { this.API = new TwitchAPI(clientID, clientSecret); }
	
	/**
	 * Connects to the Twitch IRC Server
	 */
	public void connect() {
		try {
			this.getGlobalBTTVEmotes().clear();
			BetterTTV bttv = Utils.GSON.fromJson(Utils.CallAPI(Method.GET, TwitchTMI.BTTV_BASE_URL +"emotes"), BetterTTV.class);
			for(BetterTTV.Emote emote : bttv.getEmotes()) {
				this.getGlobalBTTVEmotes().add(emote);
				if(this.isVerbose())
					System.out.println("[BetterTTV] Registered global emote "+ emote.getCode() +"!");
			}
			
			this.getGlobalFFZEmotes().clear();
			FrankerFaceZ ffz = Utils.GSON.fromJson(Utils.CallAPI(Method.GET, TwitchTMI.FFZ_BASE_URL +"set/global"), FrankerFaceZ.class);
			for(int key : ffz.getSets().keySet()) {
				FrankerFaceZ.Set set = ffz.getSets().get(key);
				for(FrankerFaceZ.Emote emote : set.getEmotes()) {
					this.getGlobalFFZEmotes().add(emote);
					if(this.isVerbose())
						System.out.println("[FrankerFaceZ] Registered global emote "+ emote.getName() +"!");
				}
			}
		} catch(IOException e) { e.printStackTrace(); }
		
		this.Chat = new ChatService(this);
		this.Chat.start();
	}
	
	/**
	 * Reconnects to the Twitch IRC Server
	 */
	public void reconnect() {
		if(this.isConnected()) {
			this.Chat.disconnect();
			this.Chat.Timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Chat.connect();
				}
			}, this.reconnectInterval * 1000);
		}
	}
	
	/**
	 * Returns the provided channel as an object
	 *
	 * @param channel
	 * @return
	 */
	public Channel getChannel(String channel) {
		if(channel.startsWith("#")) channel = channel.substring(1);
		for(Channel cc : this.getConnectedChannels())
			if(channel.equalsIgnoreCase(cc.getName()))
				return cc;
		return new Channel(this, channel);
	}
	
	/**
	 * Sends a message in the given channel
	 *
	 * @param channel
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void sendMessage(String channel, String message) throws MessageSendFailureException {
		channel = channel.toLowerCase();
		if(!channel.startsWith("#"))
			channel = "#"+ channel;
		try {
			if(!this.isConnected() || this.getClientUser() == null)
				throw new IOException("Unable to send message!");
			this.Chat.sendRawData("PRIVMSG "+ channel +" :"+message);
			try {
				MessageEvent.Type type = MessageEvent.Type.CHAT;
				if(message.toLowerCase().startsWith("/me"))
					type = MessageEvent.Type.ACTION;
				Message msg = new Message(this, this.getChannel(channel), this.getClientUser().getUser(), message);
				this.getEventListener().onMessage(new MessageEvent(this, this.getClientUser().getUser(), msg, type, true));
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(IOException e) {
			throw new MessageSendFailureException("Something went wrong while sending your message!");
		}
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
			if(!this.isConnected() || this.getClientUser() == null)
				throw new IOException("Unable to send whisper!");
			this.Chat.sendRawData("PRIVMSG #jtv :/w "+ recipient.toLowerCase() +" "+ message);
			try {
				Message msg = new Message(this, this.getChannel("jtv"), this.getClientUser().getUser(), message);
				MessageEvent event = new MessageEvent(this, this.getClientUser().getUser(), msg, MessageEvent.Type.WHISPER, true);
				this.getEventListener().onWhisper(event);
				this.getEventListener().onMessage(event);
			} catch(Exception e) {
				e.printStackTrace();
			}
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
		
		private Timer Timer = new Timer();
		
		private ChatService(TwitchTMI TMI) {
			this.TMI = TMI;
			this.connected = false;
		}
		
		@Override
		public void run() {
			try {
				this.connect();
				
				this.sendRawData(
						"CAP REQ :twitch.tv/membership",
						"CAP REQ :twitch.tv/tags",
						"CAP REQ :twitch.tv/commands",
						
						"PASS "+ this.TMI.oAuth,
						"NICK "+ this.TMI.getUsername().toLowerCase()
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
				this.interrupt();
			}
		}
		
		private void connect() {
			try {
				this.Socket = new Socket(this.TMI.getIP(), this.TMI.getPort());
				this.Writer = new BufferedWriter(new OutputStreamWriter(this.Socket.getOutputStream()));
				this.Reader = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
			} catch(Exception e) {
				this.connected = false;
			}
		}
		
		private void disconnect() {
			try {
				this.Socket.close();
				this.Writer.close();
				this.Reader.close();
				this.connected = false;
			} catch(Exception e) {
				e.printStackTrace();
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
				String channel = (rawData.getParams().size()>0)?rawData.getParams().get(0).replaceFirst("#", "").toLowerCase():null;
				String msg = (rawData.getParams().size()>1)?rawData.getParams().get(1):"";
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
						case "001":
						case "002":
						case "003":
						case "004":
						case "375":
						case "376":
						case "CAP":
							break;
						
						case "372":
							this.connected = true;
							this.TMI.getEventListener().onConnect(new ConnectEvent(this.TMI));
							
							this.Timer.scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									for(Channel cc : TMI.getConnectedChannels())
										try {
											cc.update();
										} catch(MessageSendFailureException | IOException e) {}
								}
							}, 5*60*1000, 5*60*1000);
						break;
						
						case "NOTICE":
							boolean enabled = false;
							switch(msgid) {
								case "SUBS_ON":
								case "SUBS_OFF":
									{
										if(msgid.equalsIgnoreCase("SUBS_ON"))
											enabled = true;
										this.TMI.getChannel(channel).subMode = enabled;
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Type.SUBSCRIBERS_ONLY);
										this.TMI.getEventListener().onSubMode(event);
										this.TMI.getEventListener().onSubOnlyMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "EMOTE_ONLY_ON":
								case "EMOTE_ONLY_OFF":
									{
										if(msgid.equalsIgnoreCase("EMOTE_ONLY_ON"))
											enabled = true;
										this.TMI.getChannel(channel).emoteOnly = enabled;
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Type.EMOTE_ONLY);
										this.TMI.getEventListener().onEmoteOnly(event);
										this.TMI.getEventListener().onEmoteOnlyMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "SLOW_ON":
								case "SLOW_OFF":
									{
										if(msgid.equalsIgnoreCase("SLOW_ON"))
											enabled = true;
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Type.SLOW);
										this.TMI.getEventListener().onSlowMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "FOLLOWERS_ON_ZERO":
								case "FOLLOWERS_ON":
								case "FOLLOWERS_OFF":
									{
										if(!msgid.equalsIgnoreCase("FOLLOWERS_OFF"))
											enabled = true;
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Type.FOLLOWERS_ONLY);
										this.TMI.getEventListener().onFollowersOnly(event);
										this.TMI.getEventListener().onFollowersOnlyMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "R9K_ON":
								case "R9K_OFF":
									{
										if(msgid.equalsIgnoreCase("R9K_ON"))
											enabled = true;
										this.TMI.getChannel(channel).R9KMode = enabled;
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Type.R9K);
										this.TMI.getEventListener().onR9KMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "ROOM_MODS":
									{
										Channel cc = this.TMI.getChannel(channel);
											cc.getMods().clear();
										String mods_str = msg.substring(msg.indexOf(":") + 1);
										String[] mods = mods_str.split(", ");
										for(String mod : mods) {
											mod = mod.replaceAll(" ", "").toLowerCase();
											cc.getMods().add(mod);
										}
										cc.getMods().add(cc.getName().toLowerCase());
									}
								break;
								
								case "NO_MODS":
									{
										Channel cc = this.TMI.getChannel(channel);
											cc.getMods().clear();
										cc.getMods().add(cc.getName().toLowerCase());
									}
								break;
								
								case "MSG_CHANNEL_SUSPENDED":
									break;
								
								case "ALREADY_BANNED":
								case "BAD_BAN_ADMIN":
								case "BAD_BAN_BROADCASTER":
								case "BAD_BAN_GLOBAL_MOD":
								case "BAD_BAN_SELF":
								case "BAD_BAN_STAFF":
								case "USAGE_BAN":
									break;
								
								case "BAN_SUCCESS":
									break;
								
								case "USAGE_CLEAR":
									break;
								
								case "USAGE_MODS":
									break;
								
								case "MOD_SUCCESS":
									break;
								
								case "USAGE_MOD":
								case "BAD_MOD_BANNED":
								case "BAD_MOD_MOD":
									break;
								
								case "UNMOD_SUCCESS":
									break;
								
								case "USAGE_UNMOD":
								case "BAD_UNMOD_MOD":
									break;
								
								case "COLOR_CHANGED":
									break;
								
								case "USAGE_COLOR":
								case "TURBO_ONLY_COLOR":
									break;
								
								case "COMMERCIAL_SUCCESS":
									break;
								
								case "USAGE_COMMERCIAL":
								case "BAD_COMMERCIAL_ERROR":
									break;
								
								case "HOSTS_REMAINING":
									break;
								
								case "BAD_HOST_HOSTING":
								case "BAD_HOST_RATE_EXCEEDED":
								case "BAD_HOST_ERROR":
								case "USAGE_HOST":
									break;
								
								case "ALREADY_R9K_OFF":
								case "USAGE_R9K_OFF":
									break;
								
								case "TIMEOUT_SUCCESS":
									break;
								
								case "ALREADY_SUBS_OFF":
								case "USAGE_SUBS_OFF":
									break;
								
								case "ALREADY_SUBS_ON":
								case "USAGE_SUBS_ON":
									break;
								
								case "ALREADY_EMOTE_ONLY_OFF":
								case "USAGE_EMOTE_ONLY_OFF":
									break;
								
								case "ALREADY_EMOTE_ONLY_ON":
								case "USAGE_EMOTE_ONLY_ON":
									break;
								
								case "USAGE_SLOW_OFF":
									break;
								
								case "USAGE_TIMEOUT":
								case "BAD_TIMEOUT_DURATION":
								case "BAD_TIMEOUT_ADMIN":
								case "BAD_TIMEOUT_BROADCASTER":
								case "BAD_TIMEOUT_GLOBAL_MOD":
								case "BAD_TIMEOUT_SELF":
								case "BAD_TIMEOUT_STAFF":
									break;
								
								case "UNTIMEOUT_SUCCESS":
								case "UNBAN_SUCCESS":
									break;
								
								case "USAGE_UNBAN":
								case "BAD_UNBAN_NO_BAN":
									break;
								
								case "USAGE_UNHOST":
								case "NOT_HOSTING":
									break;
								
								case "WHISPER_INVALID_LOGIN":
								case "WHISPER_INVALID_SELF":
								case "WHISPER_LIMIT_PER_MIN":
								case "WHISPER_LIMIT_PER_SEC":
								case "WHISPER_RESTRICTED_RECIPIENT":
									break;
								
								case "NO_PERMISSION":
								case "MSG_BANNED":
									break;
								
								case "UNRECOGNIZED_CMD":
									break;
								
								case "CMDS_AVAILABLE":
								case "HOST_TARGET_WENT_OFFLINE":
								case "MSG_CENSORED_BROADCASTER":
								case "MSG_DUPLICATE":
								case "MSG_EMOTEONLY":
								case "MSG_VERIFIED_EMAIL":
								case "MSG_RATELIMIT":
								case "MSG_SUBSONLY":
								case "MSG_TIMEOUT":
								case "NO_HELP":
								case "USAGE_DISCONNECT":
								case "USAGE_HELP":
								case "USAGE_ME":
									break;
								
								case "HOST_ON":
								case "HOST_OFF":
									if(msgid.equalsIgnoreCase("HOST_OFF"))
										this.TMI.getEventListener().onUnhost(new HostEvent(this.TMI, null, this.TMI.getChannel(channel), 0, false));
								break;
								
								default:
									if(msg.toLowerCase().contains("login authentication failed"))
										this.TMI.getEventListener().onError(new ErrorEvent(this.TMI, msg, ErrorEvent.ErrorType.AUTHENTICATION_FAILURE));
								break;
							}
						break;
						
						case "USERNOTICE":
							{
								switch(msgid) {
									case "RESUB":
										break;
									
									case "SUB":
										break;
									
									case "SUBGIFT":
										break;
									
									case "RAID":
										break;
								}
							}
						break;
						
						case "HOSTTARGET":
							{
								if(msg.split(" ")[0].equals("-"))
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, null, this.TMI.getChannel(channel), 0, false));
								else {
									int count = 0;
									try { count = Integer.parseInt(msg.split(" ")[1]); } catch(Exception e) {}
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, this.TMI.getChannel(msg.split(" ")[0]), this.TMI.getChannel(channel), count, msg.toLowerCase().contains("auto")));
								}
							}
						break;
						
						case "CLEARCHAT":
							{
								boolean timeout = false;
								String reason = null;
								if(rawData.getTags().containsKey("ban-duration"))
									timeout = true;
								reason = rawData.getTags().getOrDefault("ban-reason", null);
								if(reason != null) {
									BanEvent event = new BanEvent(this.TMI, this.TMI.getChannel(channel), msg, Integer.parseInt(rawData.getTags().getOrDefault("ban-duration", "-1")), reason);
									if(timeout)
										this.TMI.getEventListener().onTimeout(event);
									this.TMI.getEventListener().onBan(event);
								} else
									this.TMI.getEventListener().onChatCleared(new ClearChatEvent(this.TMI, this.TMI.getChannel(channel)));
							}
						break;
						
						case "RECONNECT":
							this.TMI.close();
							this.Timer.schedule(new TimerTask() {
								@Override
								public void run() {
									try {
										TMI.getEventListener().onReconnectRequest(TMI);
									} catch(Exception e) {}
								}
							}, this.TMI.getReconnectInterval() * 1000);
						break;
						
						case "SERVERCHANGE":
							break;
						
						case "USERSTATE":
							{
								if(this.TMI.getClientUser().getUserstates().containsKey(channel.toLowerCase()))
									this.TMI.getClientUser().getUserstates().remove(channel.toLowerCase());
								this.TMI.getClientUser().getUserstates().put(channel.toLowerCase(), new ClientUser.UserState(this.TMI, this.TMI.getUsername(), rawData.getTags()));
							}
						break;
						
						case "GLOBALUSERSTATE":
							this.TMI.clientUser = new ClientUser(this.TMI, new User(this.TMI, this.TMI.getUsername(), rawData.getTags()));
							this.TMI.getEventListener().onReady();
						break;
						
						case "ROOMSTATE":
							{
								if(!this.TMI.getChannel(channel).isConnected()) {
									this.TMI.getConnectedChannels().add(new Channel(this.TMI, channel, rawData.getTags()));
									this.TMI.getEventListener().onChannelJoin(new ChannelEvent(this.TMI, this.TMI.getChannel(channel), ChannelEvent.Type.JOIN, true));
									
									this.TMI.getChannel(channel).update();
								} else
									this.TMI.getChannel(channel).roomstate(rawData.getTags(), true);
							}
						break;
					}
				} else if(rawData.getPrefix().equalsIgnoreCase("jtv")) {
					switch(rawData.getCommand().toUpperCase()) {
						case "MODE":
							switch(msg.toUpperCase()) {
								case "+O":
									break;
								
								case "-O":
									break;
							}
						break;
					}
				} else {
					boolean self = false;
					String username = rawData.getPrefix().split("!")[0].toLowerCase();
					if(username.equalsIgnoreCase(this.TMI.getUsername()))
						self = true;
					switch(rawData.getCommand().toUpperCase()) {
						case "353":
						case "366":
							break;
						
						case "JOIN":
							if(!self)
								this.TMI.getEventListener().onChannelJoin(new ChannelEvent(this.TMI, this.TMI.getChannel(channel), ChannelEvent.Type.JOIN));
						break;
						
						case "PART":
							if(self) {
								for(Channel cc : this.TMI.getConnectedChannels())
									if(cc.getName().equalsIgnoreCase(channel)) {
										this.TMI.getConnectedChannels().remove(cc);
										if(this.TMI.getClientUser() != null) {
											if(this.TMI.getClientUser().getUserstates().containsKey(cc.getName().toLowerCase()))
												this.TMI.getClientUser().getUserstates().remove(cc.getName().toLowerCase());
										}
									}
							}
							this.TMI.getEventListener().onChannelLeave(new ChannelEvent(this.TMI, this.TMI.getChannel(channel), ChannelEvent.Type.LEAVE, self));
						break;
						
						case "WHISPER":
							{
								User sender = new User(this.TMI, username, rawData.getTags());
								Message message = new Message(this.TMI, this.TMI.getChannel("jtv"), sender, msg, rawData.getTags());
								MessageEvent event = new MessageEvent(this.TMI, sender, message, MessageEvent.Type.WHISPER);
								this.TMI.getEventListener().onWhisper(event);
								this.TMI.getEventListener().onMessage(event);
							}
						break;
						
						case "PRIVMSG":
							if(username.equalsIgnoreCase("jtv")) {
								if(msg.toLowerCase().contains("hosting you")) {
									int count = 0;
									if(msg.toLowerCase().contains("hosting you for"))
										count = Utils.extractNumber(msg);
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, this.TMI.getChannel(channel), this.TMI.getChannel(Utils.username(msg.split(" ")[0])), count, msg.toLowerCase().contains("auto")));
								}
							} else {
								MessageEvent.Type type = MessageEvent.Type.CHAT;
								User sender = new User(this.TMI, username, rawData.getTags());
								Message message = new Message(this.TMI, this.TMI.getChannel(channel), sender, msg, rawData.getTags());
								MessageEvent event = new MessageEvent(this.TMI, sender, message, type);
								if(msg.matches("^\\u0001ACTION ([^\\u0001]+)\\u0001$")) {
									type = MessageEvent.Type.ACTION;
									event = new MessageEvent(this.TMI, sender, message, type);
									this.TMI.getEventListener().onAction(event);
								} else if(rawData.getTags().containsKey("bits")) {
									type = MessageEvent.Type.CHEER;
									event = new MessageEvent(this.TMI, sender, message, type);
									this.TMI.getEventListener().onCheer(event, Integer.parseInt(rawData.getTags().get("bits")));
								}
								this.TMI.getEventListener().onMessage(event);
							}
						break;
					}
				}
			}
		}
	}
}
