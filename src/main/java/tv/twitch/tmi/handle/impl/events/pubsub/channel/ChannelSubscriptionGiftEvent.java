package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.SubscriptionGift;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ChannelSubscriptionGiftEvent extends Event {
	private PubSub pubSub;
	private SubscriptionGift subscriptionGift;
	private String topic;
	
	public ChannelSubscriptionGiftEvent(PubSub pubSub, SubscriptionGift subscriptionGift, String topic) {
		this.pubSub = pubSub;
		this.subscriptionGift = subscriptionGift;
		this.topic = topic;
	}
}
