package tv.twitch.tmi.events;

import java.util.concurrent.Executor;

interface IEventHandler {
	boolean accepts(Event event);
	
	Executor getExecutor();
	
	void handle(Event event) throws Throwable;
}
