package tv.twitch.api.v.helix.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;
import java.util.regex.Pattern;

@Getter
public class GameResponse extends ResponseData {
	private List<Game> data;
	private Pagination pagination;
	
	@Getter
	public static class Game extends ResponseData {
		private String id;
		private String name;
		@SerializedName("box_art_url") private String unmodifiedBoxArtUrl;
		
		public String getBoxArtUrl(int width, int height) {
			return getUnmodifiedBoxArtUrl().replaceFirst(Pattern.quote("{width}"), ""+width).replaceFirst(Pattern.quote("{height}"), ""+height);
		}
	}
}
