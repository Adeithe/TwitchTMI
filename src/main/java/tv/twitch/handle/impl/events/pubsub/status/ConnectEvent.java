package tv.twitch.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.pubsub.PubSubConnection;

@Getter
public class ConnectEvent extends Event {
	private PubSubConnection shard;
	
	public ConnectEvent(PubSubConnection shard) {
		this.shard = shard;
	}
}
