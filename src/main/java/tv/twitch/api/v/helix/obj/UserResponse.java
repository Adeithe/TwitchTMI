package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class UserResponse {
	@SerializedName("data") private List<User> users;
	private Pagination pagination;
	
	@Getter
	public static class User {
		private String id;
		@SerializedName("login") private String username;
		@SerializedName("display_name") private String displayName;
		private String email;
		private String description;
		@SerializedName("view_count") private int views;
		@SerializedName("offline_image_url") private String offlineImageUrl;
		@SerializedName("profile_image_url") private String profileImageUrl;
		@SerializedName("broadcaster_type") private String broadcasterType;
		private String type;
	}
}
