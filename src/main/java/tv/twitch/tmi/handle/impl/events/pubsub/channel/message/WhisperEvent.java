package tv.twitch.tmi.handle.impl.events.pubsub.channel.message;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Whisper;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class WhisperEvent extends Event {
	private PubSub pubSub;
	private Whisper.Data whisper;
	private String topic;
	
	public WhisperEvent(PubSub pubSub, Whisper whisper, String topic) {
		this.pubSub = pubSub;
		this.whisper = whisper.getData();
		this.topic = topic;
	}
}
