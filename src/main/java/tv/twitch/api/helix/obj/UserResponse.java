package tv.twitch.api.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.utils.Utils;

import java.util.List;

@Getter
public class UserResponse {
	private List<UserData> data;
	
	@Getter
	public static class UserData {
		private String id;
		@SerializedName("login") private String username;
		@SerializedName("display_name") private String displayName;
		@SerializedName("profile_image_url") private String avatar;
		private String email;
		private String description;
		@SerializedName("offline_image_url") private String offlineImageURL;
		@SerializedName("view_count") private int viewCount;
		@SerializedName("broadcaster_type") private String broadcasterType;
		private String type;
		
		@Override
		public String toString() { return Utils.GSON.toJson(this); }
	}
	
	@Override
	public String toString() { return Utils.GSON.toJson(this); }
}
