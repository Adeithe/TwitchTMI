package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;

@Getter
public class ResponsePacket extends PubSubPacket {
	private Error error;
	
	public ResponsePacket() { super(Type.RESPONSE); }
}
