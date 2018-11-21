package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.VideoPlayback;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class VideoPlaybackEvent extends Event {
	private PubSub pubSub;
	private VideoPlayback videoPlayback;
	private String topic;
	
	public VideoPlaybackEvent(PubSub pubSub, VideoPlayback videoPlayback, String topic) {
		this.pubSub = pubSub;
		this.videoPlayback = videoPlayback;
		this.topic = topic;
	}
}
