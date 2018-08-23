package tv.twitch.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.tmi.TwitchTMI;

public class DisconnectEvent extends Event {
	@Getter private TwitchTMI TMI;
	
	private boolean reconnect;
	
	public DisconnectEvent(TwitchTMI TMI, boolean reconnect) {
		this.TMI = TMI;
		
		this.reconnect = reconnect;
	}
	
	@Deprecated
	public boolean willReconnect() { return this.isReconnecting(); }
	
	public boolean isReconnecting() { return this.reconnect; }
}
