package tv.twitch.api.obj;

import tv.twitch.utils.Utils;

public class ResponseData {
	@Override
	public String toString() { return Utils.GSON.toJson(this); }
}
