package tv.twitch.tmi.handle.impl.events.tmi.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.tmi.Channel;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class ChannelJoinEvent extends Event {
	private TwitchTMI TMI;
	
	private Channel channel;
	private String username;
	private boolean self;
	
	public ChannelJoinEvent(TwitchTMI TMI, Channel channel, String username, boolean self) {
		this.TMI = TMI;
		
		this.channel = channel;
		this.username = username.toLowerCase();
		this.self = self;
	}
}
