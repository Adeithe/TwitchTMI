package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.RawData;

public class ChannelModeEvent implements IEvent {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private Channel channel;
	@Getter private boolean enabled;
	@Getter private Mode mode;
	
	public ChannelModeEvent(TwitchTMI TMI, RawData rawData, String channel, boolean enabled, Mode mode) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = new Channel(TMI, channel);
		this.enabled = enabled;
		this.mode = mode;
	}
	
	public enum Mode {
		SUBSCRIBER,
		EMOTE,
		R9K
	}
}
