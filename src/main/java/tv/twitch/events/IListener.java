package tv.twitch.events;

@FunctionalInterface
public interface IListener<T extends Event> {
	void handle(T event);
}
