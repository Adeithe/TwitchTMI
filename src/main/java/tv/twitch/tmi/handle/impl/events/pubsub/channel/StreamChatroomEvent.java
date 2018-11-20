package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.StreamChatroom;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class StreamChatroomEvent extends Event {
	private PubSub pubSub;
	private StreamChatroom streamChatroom;
	private String topic;
	
	public StreamChatroomEvent(PubSub pubSub, StreamChatroom streamChatroom, String topic) {
		this.pubSub = pubSub;
		this.streamChatroom = streamChatroom;
		this.topic = topic;
	}
}
