package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.api.v.helix.obj.VideoResponse;

import java.io.IOException;

public class VideoAPI {
	private TwitchAPI API;
	
	public VideoAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public VideoResponse getVideoByOwner(int owner_id) throws IOException { return this.getVideoByOwner(owner_id, 20); }
	public VideoResponse getVideoByOwner(int owner_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?user_id="+ owner_id +"&first="+ first), VideoResponse.class); }
	public VideoResponse getVideoByOwnerAfter(int owner_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?user_id="+ owner_id +"&first="+ first +"&after="+ pagination.getCursor()), VideoResponse.class); }
	public VideoResponse getVideoByOwnerBefore(int owner_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?user_id="+ owner_id +"&first="+ first +"&before="+ pagination.getCursor()), VideoResponse.class); }
	
	public VideoResponse getVideoByGame(int game_id) throws IOException { return this.getVideoByOwner(game_id, 20); }
	public VideoResponse getVideoByGame(int game_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?game_id="+ game_id +"&first="+ first), VideoResponse.class); }
	public VideoResponse getVideoByGameAfter(int game_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?game_id="+ game_id +"&first="+ first +"&after="+ pagination.getCursor()), VideoResponse.class); }
	public VideoResponse getVideoByGameBefore(int game_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?game_id="+ game_id +"&first="+ first +"&before="+ pagination.getCursor()), VideoResponse.class); }
	
	public VideoResponse getVideoByID(int id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/videos?id="+ id), VideoResponse.class); }
}
