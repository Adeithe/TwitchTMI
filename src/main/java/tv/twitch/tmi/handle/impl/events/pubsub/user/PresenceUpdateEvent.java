package tv.twitch.tmi.handle.impl.events.pubsub.user;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Presence;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class PresenceUpdateEvent extends Event {
	private PubSub pubSub;
	private Presence presence;
	private String topic;
	
	public PresenceUpdateEvent(PubSub pubSub, Presence presence, String topic) {
		this.pubSub = pubSub;
		this.presence = presence;
		this.topic = topic;
	}
}
