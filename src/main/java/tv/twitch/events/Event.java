package tv.twitch.events;

import lombok.Getter;
import tv.twitch.TwitchClient;

public abstract class Event {
	@Getter protected TwitchClient client;
}
