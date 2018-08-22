package tv.twitch.handle.impl.events.tmi.raw;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.handle.impl.obj.tmi.RawData;

@Getter
public class RawDataEvent extends Event {
	private RawData rawData;
	
	public RawDataEvent(RawData rawData) {
		this.rawData = rawData;
	}
}
