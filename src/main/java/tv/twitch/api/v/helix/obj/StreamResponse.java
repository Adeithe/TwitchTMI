package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class StreamResponse {
	@SerializedName("data") private List<Stream> stream;
	private Pagination pagination;
	
	@Getter public static class Stream {
		private String id;
		private String languge;
		private String title;
		private String type;
		@SerializedName("thumbnail_url") private String thumbnailUrl;
		@SerializedName("user_id") private String userId;
		@SerializedName("game_id") private String gameId;
		@SerializedName("community_ids") private List<String> communityIds;
		@SerializedName("viewer_count") private int viewerCount;
		@SerializedName("started_at") private String startedAt;
	}
}
