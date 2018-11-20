package tv.twitch.api.v.helix.obj;

import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

@Getter
public class Pagination extends ResponseData {
	private String cursor;
}
