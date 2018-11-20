package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class UserResponse extends ResponseData {
	private List<UserData> data;
	
	@Getter
	public static class UserData extends ResponseData {
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
	}
}
