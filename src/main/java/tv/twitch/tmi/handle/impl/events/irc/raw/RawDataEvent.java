package tv.twitch.tmi.handle.impl.events.irc.raw;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.RawData;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class RawDataEvent extends Event {
	private TwitchTMI TMI;
	
	private RawData rawData;
	
	public RawDataEvent(TwitchTMI TMI, RawData rawData) {
		this.TMI = TMI;
		
		this.rawData = rawData;
	}
}
