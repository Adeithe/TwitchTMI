package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class UserFollowsResponse {
	private int total;
	@SerializedName("data") private List<Follow> follows;
	private Pagination pagination;
	
	@Getter
	public static class Follow {
		@SerializedName("from_id") private String fromId;
		@SerializedName("to_id") private String toId;
		@SerializedName("followedAt") private String followedAt;
	}
}
