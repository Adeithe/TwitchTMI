package tv.twitch.tmi.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.pubsub.PubSub;

/**
 * Fired when the PubSub Client has disconnected and will attempt to reconnect.
 */
@Getter
public class ReconnectEvent extends Event {
	private PubSub pubSub;
	
	public ReconnectEvent(PubSub pubSub) {
		this.pubSub = pubSub;
	}
}
