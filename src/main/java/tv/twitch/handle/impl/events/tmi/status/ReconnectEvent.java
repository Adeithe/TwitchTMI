package tv.twitch.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class ReconnectEvent extends Event {
	private TwitchTMI TMI;
	
	public ReconnectEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
