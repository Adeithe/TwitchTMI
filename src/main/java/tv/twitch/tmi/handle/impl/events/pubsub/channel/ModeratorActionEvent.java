package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.ModeratorAction;
import tv.twitch.tmi.pubsub.PubSub;

/**
 * Fired when a moderator of a subscribed topic issues a moderation action.
 */
@Getter
public class ModeratorActionEvent extends Event {
	private PubSub pubSub;
	
	private String topic;
	private ModeratorAction.Action action;
	
	public ModeratorActionEvent(PubSub pubSub, ModeratorAction action, String topic) {
		this.pubSub = pubSub;
		
		this.topic = topic;
		this.action = action.getData();
	}
}
