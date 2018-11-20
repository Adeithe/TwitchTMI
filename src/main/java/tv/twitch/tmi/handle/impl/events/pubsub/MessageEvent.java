package tv.twitch.tmi.handle.impl.events.pubsub;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.MessagePacket;
import tv.twitch.tmi.pubsub.PubSub;

/**
 * Fired upon receiving a {@link MessagePacket} from the PubSub.
 *
 * You will likely not need this but it is available to allow for custom implementation.
 */
@Getter
public class MessageEvent extends Event {
	private PubSub pubSub;
	private MessagePacket packet;
	
	public MessageEvent(PubSub pubSub, MessagePacket packet) {
		this.pubSub = pubSub;
		this.packet = packet;
	}
}
