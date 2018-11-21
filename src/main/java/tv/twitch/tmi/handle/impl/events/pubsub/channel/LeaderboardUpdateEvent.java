package tv.twitch.tmi.handle.impl.events.pubsub.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Leaderboard;
import tv.twitch.tmi.pubsub.PubSub;

@Getter
public class LeaderboardUpdateEvent extends Event {
	private PubSub pubSub;
	private Leaderboard leaderboard;
	private String topic;
	
	public LeaderboardUpdateEvent(PubSub pubSub, Leaderboard leaderboard, String topic) {
		this.pubSub = pubSub;
		this.leaderboard = leaderboard;
		this.topic = topic;
	}
}
