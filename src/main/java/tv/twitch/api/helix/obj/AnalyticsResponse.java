package tv.twitch.api.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsResponse {
	private List<Analytics> data;
	@Getter private Pagination pagination;
	
	/**
	 * Should only be called when attempting to get extension analytics.
	 */
	public List<ExtensionAnalytics> getAsExtensionData() {
		List<ExtensionAnalytics> data = new ArrayList<>();
		for(Analytics analytics : this.data)
			if(analytics instanceof ExtensionAnalytics)
				data.add((ExtensionAnalytics) analytics);
		return data;
	}
	
	/**
	 * Should only be called when attempting to get game analytics.
	 */
	public List<GameAnalytics> getAsGameData() {
		List<GameAnalytics> data = new ArrayList<>();
		for(Analytics analytics : this.data)
			if(analytics instanceof GameAnalytics)
				data.add((GameAnalytics) analytics);
		return data;
	}
	
	@Getter
	public static class ExtensionAnalytics extends Analytics {
		@SerializedName("extension_id") private String id;
		@SerializedName("URL") private String url;
		private ExtensionType type;
		@SerializedName("date_range") private DateRange dateRange;
	}
	
	@Getter
	public static class GameAnalytics extends Analytics {
		@SerializedName("game_id") private String id;
		@SerializedName("URL") private String url;
		private ExtensionType type;
		@SerializedName("date_range") private DateRange dateRange;
	}
	
	@Getter
	public static class DateRange {
		@SerializedName("started_at") private String startedAt;
		@SerializedName("ended_at") private String endedAt;
	}
	
	public static enum ExtensionType {
		OVERVIEW_V1("overview_v1"),
		OVERVIEW_V2("overview_v2");
		
		private String type;
		
		ExtensionType(String type) { this.type = type; }
		
		@Override
		public String toString() { return this.type; }
	}
	
	public abstract static class Analytics {}
}
