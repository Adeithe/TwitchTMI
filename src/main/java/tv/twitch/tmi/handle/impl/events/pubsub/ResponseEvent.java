package tv.twitch.tmi.handle.impl.events.pubsub;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class ResponseEvent extends Event {
	private PubSub pubSub;
	private String nonce;
	private PubSubPacket.Error error;
	
	public ResponseEvent(PubSub pubSub, PubSubPacket packet) {
		this.pubSub = pubSub;
		
		this.nonce = packet.getNonce();
		this.error = packet.getError();
	}
	
	public boolean hasError() { return (this.getError() != null); }
}
