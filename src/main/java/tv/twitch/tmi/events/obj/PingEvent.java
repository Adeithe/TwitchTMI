package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

public class PingEvent implements IEvent {
	@Getter private RawData rawData;
	
	public PingEvent(RawData rawData) {
		this.rawData = rawData;
	}
}
