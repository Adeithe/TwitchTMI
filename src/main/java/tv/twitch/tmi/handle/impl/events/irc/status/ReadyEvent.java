package tv.twitch.tmi.handle.impl.events.irc.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class ReadyEvent extends Event {
	private TwitchTMI TMI;
	
	public ReadyEvent(TwitchTMI TMI) {
		this.TMI = TMI;
	}
}
