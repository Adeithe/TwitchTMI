package tv.twitch.handle.impl.events.tmi.raw;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.handle.impl.obj.tmi.RawData;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class RawDataEvent extends Event {
	private TwitchTMI TMI;
	
	private RawData rawData;
	
	public RawDataEvent(TwitchTMI TMI, RawData rawData) {
		this.TMI = TMI;
		
		this.rawData = rawData;
	}
}
