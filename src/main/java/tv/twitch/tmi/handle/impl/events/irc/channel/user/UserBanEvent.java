package tv.twitch.tmi.handle.impl.events.irc.channel.user;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class UserBanEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	
	private int userId;
	private String username;
	
	private String reason;
	private boolean timeout;
	
	public UserBanEvent(TwitchTMI TMI, Channel channel, int userId, String username, String reason, boolean perma) {
		this.TMI = TMI;
		this.channel = channel;
		
		this.userId = userId;
		this.username = username;
		
		this.reason = reason;
		this.timeout = !perma;
	}
}
