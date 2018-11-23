package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Subscription;
import tv.twitch.tmi.pubsub.PubSub;

public class ChannelSubscriptionEvent extends Event {
	private PubSub pubSub;
	private Subscription subscription;
	private String topic;
	
	public ChannelSubscriptionEvent(PubSub pubSub, Subscription subscription, String topic) {
		this.pubSub = pubSub;
		this.subscription = subscription;
		this.topic = topic;
	}
}
