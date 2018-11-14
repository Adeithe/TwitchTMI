package tv.twitch.tmi.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class ReadyEvent extends Event {
	private TwitchTMI TMI;
	
	public ReadyEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
