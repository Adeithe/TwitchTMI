package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Commerce;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ChannelCommerceEvent extends Event {
	private PubSub pubSub;
	private Commerce commerce;
	private String topic;
	
	public ChannelCommerceEvent(PubSub pubSub, Commerce commerce, String topic) {
		this.pubSub = pubSub;
		this.commerce = commerce;
		this.topic = topic;
	}
}
