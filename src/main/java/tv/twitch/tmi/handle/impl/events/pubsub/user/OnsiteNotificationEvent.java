package tv.twitch.tmi.handle.impl.events.pubsub.user;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.OnsiteNotification;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class OnsiteNotificationEvent extends Event {
	private PubSub pubSub;
	private OnsiteNotification notification;
	private String topic;
	
	public OnsiteNotificationEvent(PubSub pubSub, OnsiteNotification notification, String topic) {
		this.pubSub = pubSub;
		this.notification = notification;
		this.topic = topic;
	}
}
