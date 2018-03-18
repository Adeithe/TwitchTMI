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
	
	@Getter private String name;
	@Getter private List<String> mods;
	@Getter private boolean connected;
	
	public Channel(TwitchTMI TMI, String channel, boolean connected) {
		this.TMI = TMI;
		
		this.name = channel.toLowerCase();
		this.mods = new ArrayList<String>();
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
}
