package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class VideoResponse {
	@SerializedName("data") private List<Video> videos;
	private Pagination pagination;
	
	@Getter
	public static class Video {
		@SerializedName("user_id") private String userId;
		private String title;
		private String description;
		private String duration;
		private String id;
		private String language;
		private String url;
		@SerializedName("view_count") private int views;
		private String type;
		private String viewable;
		@SerializedName("thumbnail_url") private String thumbnailUrl;
		@SerializedName("created_at") private String createdAt;
		@SerializedName("published_at") private String publishedAt;
	}
}
