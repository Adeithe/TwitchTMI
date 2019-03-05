package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;
import java.util.regex.Pattern;

@Getter
public class StreamResponse extends ResponseData {
	private List<Stream> data;
	private Pagination pagination;
	
	@Getter
	public static class Stream extends ResponseData {
		private String id;
		private String language;
		private String title;
		private String type;
		@SerializedName("thumbnail_url") private String unmodifiedThumbnailUrl;
		@SerializedName("user_id") private String userId;
		@SerializedName("user_name") private String userName;
		@SerializedName("game_id") private String gameId;
		@SerializedName("community_ids") private List<String> communityIds;
		@SerializedName("tag_ids") private List<String> tagIds;
		@SerializedName("viewer_count") private int viewerCount;
		@SerializedName("started_at") private String startedAt;
		
		public String getThumbnailUrl(int width, int height) {
			return getUnmodifiedThumbnailUrl().replaceFirst(Pattern.quote("{width}"), ""+width).replaceFirst(Pattern.quote("{height}"), ""+height);
		}
	}
}
