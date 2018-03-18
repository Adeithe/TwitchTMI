package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

public class PingEvent implements IEvent {
	private TwitchTMI TMI;
	@Getter private RawData rawData;
	
	public PingEvent(TwitchTMI TMI, RawData rawData) {
		this.TMI = TMI;
		this.rawData = rawData;
	}
}
