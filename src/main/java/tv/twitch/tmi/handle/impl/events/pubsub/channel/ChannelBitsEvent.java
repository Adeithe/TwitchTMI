package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Bits;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ChannelBitsEvent extends Event {
	private PubSub pubSub;
	private Bits bits;
	private String topic;
	
	public ChannelBitsEvent(PubSub pubSub, Bits bits, String topic) {
		this.pubSub = pubSub;
		this.bits = bits;
		this.topic = topic;
	}
}
