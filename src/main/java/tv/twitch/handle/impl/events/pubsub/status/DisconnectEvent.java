package tv.twitch.handle.impl.events.pubsub.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.pubsub.PubSubConnection;

public class DisconnectEvent extends Event {
	@Getter	private PubSubConnection shard;
	
	private boolean reconnect;
	
	public DisconnectEvent(PubSubConnection shard, boolean reconnect) {
		this.shard = shard;
		
		this.reconnect = reconnect;
	}
	
	@Deprecated
	public boolean willReconnect() { return this.isReconnecting(); }
	
	public boolean isReconnecting() { return this.reconnect; }
}
