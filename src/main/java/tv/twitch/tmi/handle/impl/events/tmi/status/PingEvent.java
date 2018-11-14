package tv.twitch.tmi.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class PingEvent extends Event {
	private TwitchTMI TMI;
	
	public PingEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
