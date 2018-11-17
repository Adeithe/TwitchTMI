package tv.twitch.tmi.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class DisconnectEvent extends Event {
	private PubSub pubSub;
	
	private boolean reconnecting;
	
	public DisconnectEvent(PubSub pubSub, boolean reconnecting) {
		this.pubSub = pubSub;
		this.reconnecting = reconnecting;
	}
	
	@Deprecated
	public boolean willReconnect() { return this.isReconnecting(); }
}
