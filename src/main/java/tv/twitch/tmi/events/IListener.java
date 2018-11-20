package tv.twitch.tmi.events;

@FunctionalInterface
public interface IListener<T extends Event> {
	void handle(T event) throws Throwable;
}
