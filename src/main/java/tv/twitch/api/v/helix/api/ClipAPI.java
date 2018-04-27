package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.ClipResponse;
import tv.twitch.api.v.helix.obj.Pagination;

import java.io.IOException;

public class ClipAPI {
	private TwitchAPI API;
	
	public ClipAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public ClipResponse getClip(String id) throws IOException {
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?id="+ id), ClipResponse.class);
	}
	
	public ClipResponse getClipsForBroadcaster(String broadcaster_id) throws IOException { return this.getClipsForBroadcaster(broadcaster_id, 20); }
	public ClipResponse getClipsForBroadcaster(String broadcaster_id, int first) throws IOException {
		if(first > 100)
			first = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&first="+ first), ClipResponse.class);
	}
	
	public ClipResponse getClipsForBroadcasterBefore(String broadcaster_id, Pagination pagination) throws IOException { return this.getClipsForBroadcasterBefore(broadcaster_id, 20, pagination); }
	public ClipResponse getClipsForBroadcasterBefore(String broadcaster_id, int first, Pagination pagination) throws IOException {
		if(first > 100)
			first = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&first="+ first +"&before="+ pagination.getCursor()), ClipResponse.class);
	}
	
	public ClipResponse getClipsForBroadcasterAfter(String broadcaster_id, Pagination pagination) throws IOException { return this.getClipsForBroadcasterAfter(broadcaster_id, 20, pagination); }
	public ClipResponse getClipsForBroadcasterAfter(String broadcaster_id, int first, Pagination pagination) throws IOException {
		if(first > 100)
			first = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?broadcaster_id="+ broadcaster_id +"&first="+ first +"&after="+ pagination.getCursor()), ClipResponse.class);
	}
	
	public ClipResponse getClipsForGame(String game_id) throws IOException { return this.getClipsForGame(game_id, 20); }
	public ClipResponse getClipsForGame(String game_id, int count) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&first="+ count), ClipResponse.class);
	}
	
	public ClipResponse getClipsForGameBefore(String game_id, Pagination pagination) throws IOException { return this.getClipsForGameBefore(game_id, 20, pagination); }
	public ClipResponse getClipsForGameBefore(String game_id, int count, Pagination pagination) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&first="+ count +"&before="+ pagination.getCursor()), ClipResponse.class);
	}
	
	public ClipResponse getClipsForGameAfter(String game_id, Pagination pagination) throws IOException { return this.getClipsForGameAfter(game_id, 20, pagination); }
	public ClipResponse getClipsForGameAfter(String game_id, int count, Pagination pagination) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/clips?game_id="+ game_id +"&first="+ count +"&after="+ pagination.getCursor()), ClipResponse.class);
	}
}
