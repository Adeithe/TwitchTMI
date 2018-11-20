package tv.twitch.api.v.helix;

import tv.twitch.api.TwitchAPI;
import tv.twitch.api.obj.Method;
import tv.twitch.api.v.helix.obj.GameResponse;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.utils.Utils;

import java.io.IOException;
import java.util.List;

public class _GameAPI {
	private API_Helix API;
	
	public GameResponse getGamesByID(List<Integer> game_ids) throws TwitchAPI.APIException, IOException { return getGamesByID(Utils.toIntArray(game_ids)); }
	public GameResponse getGamesByID(int... game_ids) throws TwitchAPI.APIException, IOException {
		StringBuilder str = new StringBuilder();
		for(int id : game_ids)
			str.append("id="+ id +"&");
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "games?"+ str.toString().substring(0, str.toString().length() - 1)), GameResponse.class);
	}
	
	public GameResponse getGamesByName(List<String> names) throws TwitchAPI.APIException, IOException { return getGamesByName(names.toArray(new String[names.size()])); }
	public GameResponse getGamesByName(String... names) throws TwitchAPI.APIException, IOException {
		StringBuilder str = new StringBuilder();
		for(String name : names)
			str.append("name="+ name +"&");
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "games?"+ str.toString().substring(0, str.toString().length() - 1)), GameResponse.class);
	}
	
	public GameResponse getTopGames() throws TwitchAPI.APIException, IOException { return getTopGames(20); }
	public GameResponse getTopGames(int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "games/top?first="+ first), GameResponse.class); }
	public GameResponse getTopGamesAfter(Pagination pagination) throws TwitchAPI.APIException, IOException { return getTopGamesAfter(pagination, 20); }
	public GameResponse getTopGamesAfter(Pagination pagination, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "games/top?first="+ first +"&after="+ pagination.getCursor()), GameResponse.class); }
	public GameResponse getTopGamesBefore(Pagination pagination) throws TwitchAPI.APIException, IOException { return getTopGamesBefore(pagination, 20); }
	public GameResponse getTopGamesBefore(Pagination pagination, int first) throws TwitchAPI.APIException, IOException { return Utils.GSON.fromJson(API.CallAPI(Method.GET, "games/top?first="+ first +"&before="+ pagination.getCursor()), GameResponse.class); }
	
	_GameAPI(API_Helix API) { this.API = API; }
}
