package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class Leaderboard {
	@SerializedName("entry_context") private Context entryContext;
	private Event event;
	private Identifier identifier;
	private List<Entry> top;
	
	@Getter
	public static class Context {
		private List<Entry> context;
		private Entry entry;
	}
	
	@Getter
	public static class Event {
		private String id;
		private String domain;
		@SerializedName("entry_key") private String entryKey;
		@SerializedName("event_value") private int value;
		@SerializedName("grouping_key") private String groupingKey;
		@SerializedName("time_of_event") private long timestamp;
	}
	
	@Getter
	public static class Identifier {
		private String domain;
		@SerializedName("grouping_key") private String groupingKey;
		@SerializedName("time_aggregation_unit") private TimeRange timeRange;
		@SerializedName("time_bucket") private String timeBucket;
	}
	
	@Getter
	public static class Entry {
		@SerializedName("entry_key") private String key;
		private int score;
		private int rank;
	}
	
	public enum TimeRange {
		WEEKLY("WEEKLY"),
		MONTHLY("MONTHLY"),
		ALL_TIME("ALL-TIME");
		
		private String name;
		
		TimeRange(String name) { this.name = name; }
		
		@Override
		public String toString() { return this.name; }
	}
}
