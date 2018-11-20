package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class VideoResponse extends ResponseData {
	private List<Video> data;
	private Pagination pagination;
	
	@Getter
	public static class Video extends ResponseData {
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
