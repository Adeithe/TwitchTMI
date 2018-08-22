package tv.twitch.handle.impl.events.tmi.channel;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.handle.impl.obj.tmi.Channel;

@Getter
public class ChannelLeaveEvent extends Event {
	private Channel channel;
	private String username;
	private boolean self;
	
	public ChannelLeaveEvent(Channel channel, String username, boolean self) {
		this.channel = channel;
		this.username = username.toLowerCase();
		this.self = self;
	}
}
