package tv.twitch.tmi.handle.impl.events.irc.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class ChannelLeaveEvent extends Event {
	private TwitchTMI TMI;
	
	private Channel channel;
	private String username;
	private boolean self;
	
	public ChannelLeaveEvent(TwitchTMI TMI, Channel channel, String username, boolean self) {
		this.TMI = TMI;
		
		this.channel = channel;
		this.username = username.toLowerCase();
		this.self = self;
	}
}
