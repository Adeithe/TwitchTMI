package tv.twitch.api.v.helix;

import tv.twitch.api.TwitchAPI;
import tv.twitch.api.obj.Method;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.api.v.helix.obj.VideoResponse;
import tv.twitch.utils.Utils;

import java.io.IOException;

public class _VideoAPI {
	private API_Helix API;
	
	public VideoResponse getVideoByOwner(int owner_id) throws TwitchAPI.APIException, IOException { return getVideoByOwner(owner_id, 20); }
	public VideoResponse getVideoByOwner(int owner_id, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?user_id="+ owner_id +"&first="+ first), VideoResponse.class); }
	public VideoResponse getVideoByOwnerAfter(int owner_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return getVideoByOwnerAfter(owner_id, 20, pagination); }
	public VideoResponse getVideoByOwnerAfter(int owner_id, int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?user_id="+ owner_id +"&first="+ first +"&after="+ pagination.getCursor()), VideoResponse.class); }
	public VideoResponse getVideoByOwnerBefore(int owner_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return getVideoByOwnerBefore(owner_id, 20, pagination); }
	public VideoResponse getVideoByOwnerBefore(int owner_id, int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?user_id="+ owner_id +"&first="+ first +"&before="+ pagination.getCursor()), VideoResponse.class); }
	
	public VideoResponse getVideoByGame(int game_id) throws TwitchAPI.APIException, IOException { return getVideoByGame(game_id, 20); }
	public VideoResponse getVideoByGame(int game_id, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?game_id="+ game_id +"&first="+ first), VideoResponse.class); }
	public VideoResponse getVideoByGameAfter(int game_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return getVideoByGameAfter(game_id, 20, pagination); }
	public VideoResponse getVideoByGameAfter(int game_id, int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?game_id="+ game_id +"&first="+ first +"&after="+ pagination.getCursor()), VideoResponse.class); }
	public VideoResponse getVideoByGameBefore(int game_id, Pagination pagination) throws TwitchAPI.APIException, IOException { return getVideoByGameBefore(game_id, 20, pagination); }
	public VideoResponse getVideoByGameBefore(int game_id, int first, Pagination pagination) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?game_id="+ game_id +"&first="+ first +"&before="+ pagination.getCursor()), VideoResponse.class); }
	
	public VideoResponse getVideoByID(int id) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "videos?id="+ id), VideoResponse.class); }
	
	_VideoAPI(API_Helix API) { this.API = API; }
}
