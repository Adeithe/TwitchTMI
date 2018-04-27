package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;

@Getter
public class ChannelEvent implements IEvent {
	private TwitchTMI TMI;
	private Channel channel;
	private Type type;
	private boolean self;
	
	public ChannelEvent(TwitchTMI TMI, Channel channel, Type type) { this(TMI, channel, type, false); }
	public ChannelEvent(TwitchTMI TMI, Channel channel, Type type, boolean self) {
		this.TMI = TMI;
		this.channel = channel;
		this.type = type;
		this.self = self;
	}
	
	public enum Type {
		JOIN,
		LEAVE
	}
}
