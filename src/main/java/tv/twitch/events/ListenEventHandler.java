package tv.twitch.events;

import java.util.concurrent.Executor;

public class ListenEventHandler<T extends Event> implements IEventHandler {
	private final Class<?> type;
	private final IListener<T> listener;
	private final Executor executor;
	
	public ListenEventHandler(Class<?> type, IListener<T> listener, Executor executor) {
		this.type = type;
		this.listener = listener;
		this.executor = executor;
	}
	
	@Override
	public boolean accepts(Event event) {
		return type.isInstance(event);
	}
	
	@Override
	public Executor getExecutor() {
		return this.executor;
	}
	
	@Override
	public void handle(Event event) throws Throwable {
		this.listener.handle((T) event);
	}
	
	@Override
	public String toString() {
		return this.listener.getClass().getSimpleName();
	}
}
