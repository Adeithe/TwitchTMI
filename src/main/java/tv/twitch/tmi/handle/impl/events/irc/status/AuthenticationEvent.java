package tv.twitch.tmi.handle.impl.events.irc.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class AuthenticationEvent extends Event {
	private TwitchTMI TMI;
	
	public AuthenticationEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
