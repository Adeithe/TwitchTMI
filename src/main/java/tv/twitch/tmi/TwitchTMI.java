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
import java.util.*;

public class TwitchTMI {
	private ChatService Chat;
	
	@Getter private String IP;
	@Getter private int port;
	
	@Getter private HashMap<String, Channel> connectedChannels;
	@Getter @Setter private int reconnectInterval;
	@Getter @Setter private EventListener eventListener;
	@Getter @Setter private String username;
	@Getter @Setter private String oAuth;
	@Getter @Setter private boolean verbose;
	
	public TwitchTMI() {
		this.IP = "irc.chat.twitch.tv";
		this.port = 6667;
		
		this.connectedChannels = new HashMap<String, Channel>();
		this.reconnectInterval = 5;
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
									if(true) {
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.SUBSCRIBER);
										this.TMI.getEventListener().onSubMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "EMOTE_ONLY_ON":
								case "EMOTE_ONLY_OFF":
									if(msgid.equalsIgnoreCase("EMOTE_ONLY_ON"))
										enabled = true;
									if(true) {
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.EMOTE);
										this.TMI.getEventListener().onEmoteMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "SLOW_ON":
								case "SLOW_OFF":
									break;
								
								case "FOLLOWERS_ON_ZERO":
								case "FOLLOWERS_ON":
								case "FOLLOWERS_OFF":
									break;
								
								case "R9K_ON":
								case "R9K_OFF":
									if(msgid.equalsIgnoreCase("R9K_ON"))
										enabled = true;
									if(true) {
										ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), enabled, ChannelModeEvent.Mode.R9K);
										this.TMI.getEventListener().onR9KMode(event);
										this.TMI.getEventListener().onChannelMode(event);
									}
								break;
								
								case "ROOM_MODS":
									String[] mods = msg.split(":")[1].replaceAll(",", "").split(" ");
									for(String mod : mods)
										if(!mod.isEmpty() &&!this.TMI.getChannel(channel).isMod(mod))
											this.TMI.getChannel(channel).getMods().add(mod.toLowerCase());
									if(!this.TMI.getChannel(channel).isMod(channel))
										this.TMI.getChannel(channel).getMods().add(channel.toLowerCase());
								break;
								
								case "NO_MODS":
									for(String mod : this.TMI.getChannel(channel).getMods())
										this.TMI.getChannel(channel).getMods().remove(mod);
									if(!this.TMI.getChannel(channel).isMod(channel))
										this.TMI.getChannel(channel).getMods().add(channel.toLowerCase());
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
								case "BAD_TIMEOUT_ADMIN":
								case "BAD_TIMEOUT_BROADCASTER":
								case "BAD_TIMEOUT_DURATION":
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
									break;
								
								default:
									break;
							}
						break;
						
						case "USERNOTICE":
							switch(msgid) {
								case "RESUB":
									break;
								
								case "SUB":
									break;
							}
						break;
						
						case "HOSTTARGET":
							if(true) {
								if(msg.split(" ")[0].equals("-"))
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, rawData, null, this.TMI.getChannel(channel), -1, false));
								else {
									int viewers = 0;
									try { viewers = Integer.parseInt(msg.split(" ")[1]); } catch(Exception e) {}
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, rawData, this.TMI.getChannel(msg.split(" ")[0]), this.TMI.getChannel(channel), viewers, msg.toLowerCase().contains("auto")));
								}
							}
						break;
						
						case "CLEARCHAT":
							if(rawData.params.size() > 1) {
								HashMap<String, String> replacements = new HashMap<String, String>();
									replacements.put("\\\\s", " ");
									replacements.put("\\\\:", ";");
									replacements.put("\\\\\\\\", "\\");
									replacements.put("\\r", "\r");
									replacements.put("\\n", "\n");
								
								int duration = Integer.parseInt(rawData.tags.getOrDefault("ban-duration", "-1"));
								String reason = Utils.replaceAll(rawData.tags.getOrDefault("ban-reason", null), replacements);
								BanEvent event = new BanEvent(this.TMI, msg, reason, duration);
								if(duration >= 0)
									this.TMI.getEventListener().onTimeout(event);
								this.TMI.getEventListener().onBan(event);
							} else
								this.TMI.getEventListener().onChannelMode(new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), false, ChannelModeEvent.Mode.CLEAR));
						break;
						
						case "RECONNECT":
							this.TMI.reconnect();
						break;
						
						case "SERVERCHANGE":
							break;
						
						case "USERSTATE":
							if(rawData.getTags().getOrDefault("mod", "0").equals("1")) {
								this.TMI.getChannel(channel).__setMod(true);
								if(!this.TMI.getChannel(channel).isMod(this.TMI.getUsername()))
									this.TMI.getChannel(channel).getMods().add(this.TMI.getUsername().toLowerCase());
							}
							
							if(rawData.getTags().getOrDefault("subscriber", "0").equals("1"))
								this.TMI.getChannel(channel).__setSubscriber(true);
						break;
						
						case "GLOBALUSERSTATE":
							break;
						
						case "ROOMSTATE":
							this.TMI.getChannel(channel).__setRoomID(Integer.parseInt(rawData.tags.getOrDefault("room-id", "-1")));
							
							if(rawData.tags.containsKey("slow") && !rawData.tags.containsKey("subs-only")) {
								if(Integer.parseInt(rawData.tags.get("slow")) > 0) {
									ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), true, Integer.parseInt(rawData.tags.get("slow")), ChannelModeEvent.Mode.SLOW);
									this.TMI.getChannel(channel).__setSlowMode(event.isEnabled(), event.getDuration());
									this.TMI.getEventListener().onSlowMode(event);
									this.TMI.getEventListener().onChannelMode(event);
								} else {
									ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), false, ChannelModeEvent.Mode.SLOW);
									this.TMI.getChannel(channel).__setSlowMode(event.isEnabled(), event.getDuration());
									this.TMI.getEventListener().onSlowMode(event);
									this.TMI.getEventListener().onChannelMode(event);
								}
							}
							
							if(rawData.tags.containsKey("followers-only") && !rawData.tags.containsKey("subs-only")) {
								if(Integer.parseInt(rawData.tags.get("followers-only")) > -1) {
									ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), true, (Integer.parseInt(rawData.tags.get("followers-only")) * 60), ChannelModeEvent.Mode.FOLLOWER);
									this.TMI.getChannel(channel).__setFollowersOnly(event.isEnabled(), event.getDuration());
									this.TMI.getEventListener().onFollowMode(event);
									this.TMI.getEventListener().onChannelMode(event);
								} else {
									ChannelModeEvent event = new ChannelModeEvent(this.TMI, rawData, this.TMI.getChannel(channel), false, ChannelModeEvent.Mode.FOLLOWER);
									this.TMI.getChannel(channel).__setFollowersOnly(event.isEnabled(), event.getDuration());
									this.TMI.getEventListener().onFollowMode(event);
									this.TMI.getEventListener().onChannelMode(event);
								}
							}
						break;
					}
				} else if(rawData.getPrefix().equalsIgnoreCase("jtv")) {
					switch(rawData.getCommand().toUpperCase()) {
						case "MODE":
							switch(msg.toUpperCase()) {
								case "+O":
									this.TMI.getChannel(channel).sendMessage("/mods");
								break;
								
								case "-O":
									this.TMI.getChannel(channel).sendMessage("/mods");
								break;
							}
						break;
					}
				} else {
					String username = rawData.getPrefix().split("!")[0].toLowerCase();
					switch(rawData.getCommand().toUpperCase()) {
						case "353":
						case "366":
							break;
						
						case "JOIN":
							if(username.equalsIgnoreCase(this.TMI.getUsername())) {
								if(!this.TMI.getChannel(channel).isConnected())
									this.TMI.getConnectedChannels().put(channel, new Channel(this.TMI, channel, true));
								
								this.Timer.scheduleAtFixedRate(new TimerTask() {
									@Override
									public void run() {
										if(TMI.getChannel(channel).isConnected()) {
											try {
												TMI.getChannel(channel).sendMessage("/mods");
											} catch(MessageSendFailureException e) {
												e.printStackTrace();
											}
										} else
											this.cancel();
									}
								}, 0, 5*60*1000);
							}
							
							this.TMI.getEventListener().onChannelJoin(new ChannelJoinEvent(this.TMI, rawData, this.TMI.getChannel(channel), username, username.equalsIgnoreCase(this.TMI.getUsername())));
						break;
						
						case "PART":
							if(username.equalsIgnoreCase(this.TMI.getUsername()))
								if(this.TMI.getChannel(channel).isConnected())
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
									this.TMI.getEventListener().onHost(new HostEvent(this.TMI, rawData, this.TMI.getChannel(channel), this.TMI.getChannel(Utils.username(msg.split(" ")[0])), count, msg.toLowerCase().contains("auto")));
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
