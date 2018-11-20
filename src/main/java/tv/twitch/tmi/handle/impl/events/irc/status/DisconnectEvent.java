package tv.twitch.tmi.handle.impl.events.irc.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class DisconnectEvent extends Event {
	private TwitchTMI TMI;
	private boolean reconnecting;
	
	public DisconnectEvent(TwitchTMI TMI, boolean reconnect) {
		this.TMI = TMI;
		this.reconnecting = reconnect;
	}
	
	@Deprecated
	public boolean willReconnect() { return this.isReconnecting(); }
}
