package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.ClipResponse;
import tv.twitch.api.v.helix.obj.Pagination;

import java.io.IOException;
import java.util.List;

public class ClipAPI {
	private TwitchAPI API;
	
	public ClipAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public ClipResponse getClipByID(String id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?id="+ id), ClipResponse.class); }
	public ClipResponse getClipsByID(List<String> ids) throws IOException, ArrayIndexOutOfBoundsException { return this.getClipsByID(ids.toArray(new String[0])); }
	public ClipResponse getClipsByID(String... id) throws IOException, ArrayIndexOutOfBoundsException {
		if(id.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 clips!");
		StringBuilder ids = new StringBuilder();
		for(String s : id) ids.append("id="+ s +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?"+ ids.toString()), ClipResponse.class);
	}
	
	public ClipResponse getClipsByBroadcaster(int broadcaster_id) throws IOException { return this.getClipsByBroadcaster(broadcaster_id, 20); }
	public ClipResponse getClipsByBroadcaster(int broadcaster_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&first="+ first), ClipResponse.class); }
	
	public ClipResponse getClipsByBroadcasterAfter(int broadcaster_id, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&after="+ pagination.getCursor()), ClipResponse.class); }
	public ClipResponse getClipsByBroadcasterBefore(int broadcaster_id, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&before="+ pagination.getCursor()), ClipResponse.class); }
	
	public ClipResponse getClipsByGame(int game_id) throws IOException { return this.getClipsByGame(game_id, 20); }
	public ClipResponse getClipsByGame(int game_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&first="+ first), ClipResponse.class); }
	
	public ClipResponse getClipsByGameAfter(int game_id, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&after="+ pagination.getCursor()), ClipResponse.class); }
	public ClipResponse getClipsByGameBefore(int game_id, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&before="+ pagination.getCursor()), ClipResponse.class); }
}
