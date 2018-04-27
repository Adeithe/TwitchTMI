package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.GameResponse;
import tv.twitch.api.v.helix.obj.Pagination;

import java.io.IOException;

public class GameAPI {
	private TwitchAPI API;
	
	public GameAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public GameResponse getGameByID(String game_id) throws IOException {
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?id="+ game_id), GameResponse.class);
	}
	
	public GameResponse getGameByName(String game) throws IOException {
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?name="+ game), GameResponse.class);
	}
	
	public GameResponse getGameByNameAndID(String game_id, String game_name) throws IOException {
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?id="+ game_id +"&name="+ game_name), GameResponse.class);
	}
	
	public GameResponse getTopGames() throws IOException { return this.getTopGames(20); }
	public GameResponse getTopGames(int count) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?first="+ count), GameResponse.class);
	}
	
	public GameResponse getTopGamesBefore(Pagination pagination) throws IOException { return this.getTopGamesBefore(20, pagination); }
	public GameResponse getTopGamesBefore(int count, Pagination pagination) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?first="+ count +"&before="+ pagination.getCursor()), GameResponse.class);
	}
	
	public GameResponse getTopGamesAfter(Pagination pagination) throws IOException { return this.getTopGamesAfter(20, pagination); }
	public GameResponse getTopGamesAfter(int count, Pagination pagination) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?first="+ count +"&after="+ pagination.getCursor()), GameResponse.class);
	}
}
