package tv.twitch.tmi.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class AuthenticationEvent extends Event {
	private TwitchTMI TMI;
	
	public AuthenticationEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
