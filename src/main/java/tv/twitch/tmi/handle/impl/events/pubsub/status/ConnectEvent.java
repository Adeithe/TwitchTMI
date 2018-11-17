package tv.twitch.tmi.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ConnectEvent extends Event {
	private PubSub pubSub;
	
	public ConnectEvent(PubSub pubSub) {
		this.pubSub = pubSub;
	}
}
