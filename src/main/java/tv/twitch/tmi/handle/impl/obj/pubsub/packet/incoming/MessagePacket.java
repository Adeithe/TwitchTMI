package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;

@Getter
public class MessagePacket extends PubSubPacket {
	private Data data;
	
	public MessagePacket() { super(Type.MESSAGE); }
	
	@Getter
	public static class Data {
		private String topic;
		private String message;
	}
}
