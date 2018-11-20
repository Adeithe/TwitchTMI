package tv.twitch.api.v.helix;

import tv.twitch.api.TwitchAPI;
import tv.twitch.api.obj.Method;
import tv.twitch.api.v.helix.obj.ClipResponse;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.utils.Utils;

import java.io.IOException;
import java.util.List;

public class _ClipAPI {
	private API_Helix API;
	
	public ClipResponse getClipsByID(List<String> ids) throws TwitchAPI.APIException, IOException { return getClipsByID(ids.toArray(new String[ids.size()])); }
	public ClipResponse getClipsByID(String... ids) throws TwitchAPI.APIException, IOException {
		StringBuilder str = new StringBuilder();
		for(String id : ids)
			str.append("id="+ id +"&");
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?"+ str.toString().substring(0, str.toString().length() - 1)), ClipResponse.class);
	}
	
	public ClipResponse getClipsByBroadcaster(int broadcaster_id) throws TwitchAPI.APIException, IOException { return getClipsByBroadcaster(broadcaster_id, 20); }
	public ClipResponse getClipsByBroadcaster(int broadcaster_id, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?broadcaster_id="+ broadcaster_id +"&first="+ first), ClipResponse.class); }
	public ClipResponse getClipsByBroadcasterAfter(int broadcaster_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?broadcaster_id="+ broadcaster_id +"&after="+ pagination.getCursor()), ClipResponse.class); }
	public ClipResponse getClipsByBroadcasterBefore(int broadcaster_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?broadcaster_id="+ broadcaster_id +"&before="+ pagination.getCursor()), ClipResponse.class); }
	
	public ClipResponse getClipsByGame(int game_id) throws TwitchAPI.APIException, IOException { return getClipsByGame(game_id, 20); }
	public ClipResponse getClipsByGame(int game_id, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?game_id="+ game_id +"&first="+ first), ClipResponse.class); }
	public ClipResponse getClipsByGameAfter(int game_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?game_id="+ game_id +"&after="+ pagination.getCursor()), ClipResponse.class); }
	public ClipResponse getClipsByGameBefore(int game_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "clips?game_id="+ game_id +"&before="+ pagination), ClipResponse.class); }
	
	_ClipAPI(API_Helix API) { this.API = API; }
}
