package tv.twitch;

import lombok.Getter;
import tv.twitch.events.CallerRunPolicy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

@Getter
public class ClientSettings {
	VerboseLevel verbose = VerboseLevel.NONE;
	
	RejectedExecutionHandler piggyback = new CallerRunPolicy();
	int minimumPoolSize = 1;
	int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 4;
	long threadTimeout = 60L;
	TimeUnit threadTimeoutUnit = TimeUnit.SECONDS;
	int overflow = 128;
	
	ClientSettings() {}
	
	public static enum VerboseLevel {
		NONE(0),
		INCOMING(1),
		ALL(2);
		
		@Getter int level;
		
		VerboseLevel(int level) {
			this.level = level;
		}
	}
}
