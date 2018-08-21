package tv.twitch.events;

import lombok.Getter;
import net.jodah.typetools.TypeResolver;
import tv.twitch.TwitchClient;
import tv.twitch.utils.ThreadUtils;

import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class EventDispatcher {
	private final AtomicReference<HashSet<IEventHandler>> listenersRegistry = new AtomicReference<>(new HashSet<>());
	private ExecutorService defaultEventExecutor;
	
	@Getter private TwitchClient client;
	
	public EventDispatcher(TwitchClient client, RejectedExecutionHandler piggyback, int min, int max, int overflow, long timeout, TimeUnit unit) {
		this.client = client;
		this.defaultEventExecutor = new ThreadPoolExecutor(min, max, timeout, unit, new ArrayBlockingQueue<>(overflow), ThreadUtils.createThreadFactoryDaemon("Event Dispatcher"), piggyback);
	}
	
	public <T extends Event> void registerListener(IListener<T> listener) throws IllegalArgumentException { this.registerListener(listener, this.defaultEventExecutor); }
	
	public <T extends Event> void registerListener(IListener<T> listener, Executor executor) throws IllegalArgumentException {
		Class<?> type = TypeResolver.resolveRawArgument(IListener.class, listener.getClass());
		if(!Event.class.isAssignableFrom(type))
			throw new IllegalArgumentException("Type "+ type +" must be a subclass of Event.");
		
		ListenEventHandler handler = new ListenEventHandler(type, listener, executor);
		listenersRegistry.updateAndGet((set) -> {
			HashSet<IEventHandler> update = (HashSet<IEventHandler>) set.clone();
			update.add(handler);
			return update;
		});
	}
	
	public void dispatch(Event event) {
		event.client = this.getClient();
		listenersRegistry.get().stream().filter((e) -> e.accepts(event)).forEach((handler) -> {
			handler.getExecutor().execute(() -> {
				try {
					handler.handle(event);
				} catch(Throwable e) {
					e.printStackTrace();
				}
			});
		});
	}
}
