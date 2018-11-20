package tv.twitch.tmi.events;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CallerRunPolicy implements RejectedExecutionHandler {
	long last = 0;
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		long now = System.currentTimeMillis();
		synchronized(this) {
			if(now - last >= 5000)
				last = now;
		}
		if(!executor.isShutdown())
			r.run();
	}
}
