package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.exception.ChannelJoinFailureException;
import tv.twitch.tmi.exception.ChannelLeaveFailureException;
import tv.twitch.tmi.exception.MessageSendFailureException;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	private TwitchTMI TMI;
	
	@Getter private int ID;
	@Getter private String name;
	@Getter private List<String> mods;
	@Getter private boolean connected;
	
	@Getter private boolean mod;
	@Getter private boolean subscriber;
	@Getter private boolean emoteOnly;
	@Getter private boolean followersOnly;
	@Getter private boolean R9KMode;
	@Getter private boolean slowMode;
	@Getter private boolean subMode;
	
	@Getter private int followersOnlyDuration;
	@Getter private int slowModeDuration;
	
	public Channel(TwitchTMI TMI, String channel, boolean connected) {
		this.TMI = TMI;
		
		this.ID = -1;
		this.name = channel.toLowerCase();
		this.mods = new ArrayList<String>();
		
		this.mod = false;
		this.subscriber = false;
		this.emoteOnly = false;
		this.followersOnly = false;
		this.R9KMode = false;
		this.slowMode = false;
		this.subMode = false;
		
		this.followersOnlyDuration = -1;
		this.slowModeDuration = -1;
		
		this.connected = connected;
	}
	
	/**
	 * Joins the provided Twitch Chat and prepares the client to send messages
	 * This <b>MUST</b> be done after ConnectEvent has been fired
	 *
	 * @throws ChannelJoinFailureException
	 */
	public void join() throws ChannelJoinFailureException {
		String channel = this.getName();
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.TMI.isConnected() || this.TMI.getChannel(this.getName()).isConnected())
				throw new Exception("Unable to join channel!");
			this.TMI.sendRawData("JOIN " + channel);
		} catch(Exception e) {
			throw new ChannelJoinFailureException("Something went wrong while joining the channel!");
		}
	}
	
	/**
	 * Sends the provided message to the channel
	 * Client <b>MUST</b> join the channel before sending messages
	 *
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void sendMessage(String message) throws MessageSendFailureException {
		String channel = this.getName();
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.TMI.isConnected() || !this.isConnected())
				throw new Exception("Unable to send message!");
			this.TMI.sendRawData("PRIVMSG "+ channel +" :"+ message);
		} catch(Exception e) {
			throw new MessageSendFailureException("Something went wrong while sending your message!");
		}
	}
	
	/**
	 * Permanently bans the provided user from this channel
	 *
	 * @param username
	 * @throws MessageSendFailureException
	 */
	public void ban(String username) throws MessageSendFailureException {
		this.sendMessage("/ban "+ username.toLowerCase());
	}
	
	/**
	 * Times the given user out for 1 second
	 *
	 * @param username
	 * @throws MessageSendFailureException
	 */
	public void purge(String username) throws MessageSendFailureException {
		this.timeout(username, 1);
	}
	
	/**
	 * Bans the provided user from talking in chat for a set period of time
	 *
	 * @param username
	 * @param seconds
	 * @throws MessageSendFailureException
	 */
	public void timeout(String username, int seconds) throws MessageSendFailureException {
		this.timeout(username, seconds, null);
	}
	
	/**
	 * Bans the provided user from talking in chat for a set period of time
	 *
	 * @param username
	 * @param seconds
	 * @param reason
	 * @throws MessageSendFailureException
	 */
	public void timeout(String username, int seconds, String reason) throws MessageSendFailureException {
		if(reason == null)
			reason = "";
		this.sendMessage("/timeout "+ username +" "+ seconds +" "+ reason);
	}
	
	/**
	 * Unbans the the provided user from chat
	 *
	 * @param username
	 * @throws MessageSendFailureException
	 */
	public void unban(String username) throws MessageSendFailureException {
		this.sendMessage("/unban "+ username.toLowerCase());
	}
	
	/**
	 * Returns true if the bot is a moderator and is currently in chat
	 *
	 * @return
	 */
	public boolean isMod() {
		if(this.isConnected())
			return this.mod;
		return false;
	}
	
	/**
	 * Returns true if the given user is a moderator and is currently in chat
	 * <b>NOTE:</b> This is not 100% reliable and may return misinformation!
	 *
	 * @param username
	 * @return
	 */
	public boolean isMod(String username) {
		if(this.isConnected())
			if(this.getMods().contains(username.toLowerCase()))
				return true;
		return false;
	}
	
	/**
	 * Leaves the channel removing the ability to chat here until rejoined
	 *
	 * @throws ChannelLeaveFailureException
	 */
	public void leave() throws ChannelLeaveFailureException {
		String channel = this.getName();
		try {
			if(!channel.startsWith("#"))
				channel = "#" + channel;
			if(!this.TMI.isConnected() || !this.isConnected())
				throw new Exception("Unable to leave channel!");
			this.TMI.sendRawData("PART " + channel);
		} catch(Exception e) {
			throw new ChannelLeaveFailureException("Something went wrong while leaving the channel!");
		}
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setEmoteOnly(boolean isEmoteOnly) {
		this.emoteOnly = isEmoteOnly;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setFollowersOnly(boolean isFollowersOnly, int duration) {
		if(duration > -1)
			duration *= 60;
		this.followersOnly = isFollowersOnly;
		this.followersOnlyDuration = duration;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setR9KMode(boolean isR9KMode) {
		this.R9KMode = isR9KMode;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setSlowMode(boolean isSlowMode, int duration) {
		this.slowMode = isSlowMode;
		this.slowModeDuration = duration;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setSubOnly(boolean isSubMode) {
		this.subMode = isSubMode;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setRoomID(int roomID) { this.ID = roomID; }
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setMod(boolean mod) { this.mod = mod; }
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setSubscriber(boolean subscriber) { this.subscriber = subscriber; }
}
