package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;

@Getter
public class BanEvent implements IEvent {
	private TwitchTMI TMI;
	private Channel channel;
	private String username;
	private int duration;
	private String reason;
	
	public BanEvent(TwitchTMI TMI, Channel channel, String username, int duration) { this(TMI, channel, username, duration, null); }
	public BanEvent(TwitchTMI TMI, Channel channel, String username, int duration, String reason) {
		this.TMI = TMI;
		this.channel = channel;
		this.username = username.toLowerCase();
		this.duration = duration;
		this.reason = reason;
	}
	
	/**
	 * Returns true if the ban is temporary
	 *
	 * @return
	 */
	public boolean isTimeout() { return (this.duration > 0); }
	
	/**
	 * Returns true if a ban reason was provided
	 *
	 * @return
	 */
	public boolean hasReason() { return (this.reason != null && !this.reason.isEmpty()); }
}
