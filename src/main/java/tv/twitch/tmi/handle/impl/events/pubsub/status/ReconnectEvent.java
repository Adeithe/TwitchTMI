package tv.twitch.tmi.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ReconnectEvent extends Event {
	private PubSub pubSub;
	
	public ReconnectEvent(PubSub pubSub) {
		this.pubSub = pubSub;
	}
}
