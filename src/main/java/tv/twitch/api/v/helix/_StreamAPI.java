package tv.twitch.api.v.helix;

import tv.twitch.api.TwitchAPI;
import tv.twitch.api.obj.Method;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.api.v.helix.obj.StreamResponse;
import tv.twitch.utils.Utils;

import java.io.IOException;
import java.util.List;

public class _StreamAPI {
	private API_Helix API;
	
	public StreamResponse getTopStreams() throws TwitchAPI.APIException, IOException { return getTopStreams(20); }
	public StreamResponse getTopStreams(int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "streams?first="+ first), StreamResponse.class); }
	public StreamResponse getTopStreamsAfter(int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "streams?first="+ first +"&after="+ pagination.getCursor()), StreamResponse.class); }
	public StreamResponse getTopStreamsBefore(int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "streams?first="+ first +"&before="+ pagination.getCursor()), StreamResponse.class); }
	
	public StreamResponse getStreamsByUsername(List<String> usernames) throws TwitchAPI.APIException, IOException { return getStreamsByUsername(usernames.toArray(new String[usernames.size()])); }
	public StreamResponse getStreamsByUsername(String... usernames) throws TwitchAPI.APIException, IOException {
		StringBuilder str = new StringBuilder();
		for(String username : usernames)
			str.append("user_login="+ username +"&");
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "streams?"+ str.toString().substring(0, str.toString().length() - 1)), StreamResponse.class);
	}
	
	public StreamResponse getStreamsByUserID(List<Integer> user_ids) throws TwitchAPI.APIException, IOException { return getStreamsByUserID(Utils.toIntArray(user_ids)); }
	public StreamResponse getStreamsByUserID(int... user_ids) throws TwitchAPI.APIException, IOException {
		StringBuilder str = new StringBuilder();
		for(int id : user_ids)
			str.append("user_id="+ id +"&");
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "streams?"+ str.toString().substring(0, str.toString().length() - 1)), StreamResponse.class);
	}
	
	_StreamAPI(API_Helix API) { this.API = API; }
}
