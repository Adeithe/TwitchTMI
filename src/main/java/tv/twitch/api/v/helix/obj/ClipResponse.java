package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class ClipResponse extends ResponseData {
	private List<Clip> data;
	private Pagination pagination;
	
	@Getter
	public static class Clip extends ResponseData {
		private String id;
		private String title;
		private String url;
		private String language;
		private String pagination;
		@SerializedName("view_count") private int viewCount;
		@SerializedName("broadcaster_id") private String broadcasterId;
		@SerializedName("creator_id") private String creatorId;
		@SerializedName("game_id") private String gameId;
		@SerializedName("video_id") private String videoId;
		@SerializedName("thumbnail_url") private String thumbnailUrl;
		@SerializedName("embed_url") private String embedUrl;
		@SerializedName("created_at") private String createdAt;
	}
}
