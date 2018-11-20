package tv.twitch.tmi.handle.impl.events.irc.channel.user;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class UserTimeoutEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	
	private int userId;
	private String username;
	
	private String reason;
	private int duration;
	
	public UserTimeoutEvent(TwitchTMI TMI, Channel channel, int userId, String username, String reason, int duration) {
		this.TMI = TMI;
		this.channel = channel;
		
		this.userId = userId;
		this.username = username;
		
		this.reason = reason;
		this.duration = duration;
	}
}
