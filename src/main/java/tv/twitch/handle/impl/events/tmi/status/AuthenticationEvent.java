package tv.twitch.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class AuthenticationEvent extends Event {
	private TwitchTMI TMI;
	
	public AuthenticationEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
