package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class UserFollowResponse extends ResponseData {
	private int total;
	private List<Follow> data;
	private Pagination pagination;
	
	@Getter
	public static class Follow extends ResponseData {
		@SerializedName("from_id") private String fromId;
		@SerializedName("to_id") private String toId;
		@SerializedName("followedAt") private String followedAt;
	}
}
