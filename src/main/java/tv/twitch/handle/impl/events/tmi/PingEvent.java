package tv.twitch.handle.impl.events.tmi;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class PingEvent extends Event {
	private TwitchTMI TMI;
	
	public PingEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
