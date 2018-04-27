package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class GameResponse {
	@SerializedName("data") private List<Game> games;
	private Pagination pagination;
	
	@Getter
	public static class Game {
		private String id;
		private String name;
		@SerializedName("box_art_url") private Object boxArtUrl;
	}
}
