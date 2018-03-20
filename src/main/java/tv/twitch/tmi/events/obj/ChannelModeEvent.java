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
	@Getter private int duration;
	@Getter private Mode mode;
	
	public ChannelModeEvent(TwitchTMI TMI, RawData rawData, Channel channel, boolean enabled, Mode mode) {
		this(TMI, rawData, channel, enabled, -1, mode);
	}
	
	public ChannelModeEvent(TwitchTMI TMI, RawData rawData, Channel channel, boolean enabled, int duration, Mode mode) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = channel;
		this.enabled = enabled;
		this.duration = duration;
		this.mode = mode;
	}
	
	/**
	 * Returns true if the event uses a Mode that may provide a duration
	 *
	 * @return
	 */
	public boolean hasDuration() {
		if((this.getMode().equals(Mode.FOLLOWER) && this.isEnabled()) || (this.getMode().equals(Mode.SLOW) && this.isEnabled()))
			if(this.getDuration() > -1)
				return true;
		return false;
	}
	
	public enum Mode {
		EMOTE,
		FOLLOWER,
		R9K,
		SLOW,
		SUBSCRIBER,
		CLEAR
	}
}
