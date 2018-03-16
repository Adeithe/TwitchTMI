package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

public class ChannelModeEvent implements IEvent {
	private RawData rawData;
	
	@Getter private String channel;
	@Getter private boolean enabled;
	@Getter private Mode mode;
	
	public ChannelModeEvent(RawData rawData, String channel, boolean enabled, Mode mode) {
		this.rawData = rawData;
		
		this.channel = channel;
		this.enabled = enabled;
		this.mode = mode;
	}
	
	public enum Mode {
		SUBSCRIBER,
		EMOTE,
		R9K
	}
}
