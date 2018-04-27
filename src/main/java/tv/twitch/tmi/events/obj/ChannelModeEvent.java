package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;

@Getter
public class ChannelModeEvent implements IEvent {
	private TwitchTMI TMI;
	private Channel channel;
	private boolean enabled;
	private Type type;
	
	public ChannelModeEvent(TwitchTMI TMI, Channel channel, boolean enabled, Type type) {
		this.TMI = TMI;
		this.channel = channel;
		this.enabled = enabled;
		this.type = type;
	}
	
	public enum Type {
		SUBSCRIBERS_ONLY,
		EMOTE_ONLY,
		SLOW,
		FOLLOWERS_ONLY,
		R9K
	}
}
