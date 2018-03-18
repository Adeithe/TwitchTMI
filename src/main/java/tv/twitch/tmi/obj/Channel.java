package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.exception.ChannelJoinFailureException;
import tv.twitch.tmi.exception.ChannelLeaveFailureException;
import tv.twitch.tmi.exception.MessageSendFailureException;

public class Channel {
	private TwitchTMI TMI;
	
	@Getter private String channel;
	
	public Channel(TwitchTMI TMI, String channel) {
		this.TMI = TMI;
		
		this.channel = channel.toLowerCase();
	}
	
	/**
	 * Joins the provided Twitch Chat and prepares the client to send messages
	 * This <b>MUST</b> be done after ConnectEvent has been fired
	 *
	 * @throws ChannelJoinFailureException
	 */
	public void join() throws ChannelJoinFailureException {
		this.TMI.join(this.channel);
	}
	
	/**
	 * Sends the provided message to the channel
	 * Client <b>MUST</b> join the channel before sending messages
	 *
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void sendMessage(String message) throws MessageSendFailureException {
		this.TMI.sendMessage(this.channel, message);
	}
	
	/**
	 * Returns true if the given user is a moderator and is currently in chat
	 * <b>NOTE:</b> This is not 100% reliable and may return misinformation!
	 *
	 * @param username
	 * @return
	 */
	public boolean isMod(String username) {
		return this.TMI.isMod(this.channel, username);
	}
	
	/**
	 * Leaves the channel removing the ability to chat there until rejoined
	 *
	 * @throws ChannelLeaveFailureException
	 */
	public void leave() throws ChannelLeaveFailureException {
		this.TMI.leave(this.channel);
	}
}
