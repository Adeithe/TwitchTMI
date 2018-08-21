package tv.twitch.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadUtils {
	public static ThreadFactory createThreadFactoryDaemon() { return createThreadFactoryDaemon(null); }
	public static ThreadFactory createThreadFactoryDaemon(String name) {
		return (runnable) -> {
			Thread thread = Executors.defaultThreadFactory().newThread(runnable);
			if(name != null)
				thread.setName(name);
			thread.setDaemon(true);
			return thread;
		};
	}
}
