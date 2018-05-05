package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.GameResponse;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.tmi.Utils;

import java.io.IOException;
import java.util.List;

public class GameAPI {
	private TwitchAPI API;
	
	public GameAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public GameResponse getGameByID(int game_id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?id="+ game_id), GameResponse.class); }
	public GameResponse getGamesByID(List<Integer> ids) throws IOException, ArrayIndexOutOfBoundsException { return this.getGamesByID(Utils.toIntArray(ids)); }
	public GameResponse getGamesByID(int... id) throws IOException, ArrayIndexOutOfBoundsException {
		if(id.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 games!");
		StringBuilder ids = new StringBuilder();
		for(int s : id) ids.append("id="+ s +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?"+ ids.toString()), GameResponse.class);
	}
	
	public GameResponse getGameByName(String name) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?name="+ name), GameResponse.class); }
	public GameResponse getGamesByName(List<String> names) throws IOException, ArrayIndexOutOfBoundsException { return this.getGamesByName(names.toArray(new String[0])); }
	public GameResponse getGamesByName(String... name) throws IOException, ArrayIndexOutOfBoundsException {
		if(name.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 games!");
		StringBuilder names = new StringBuilder();
		for(String s : name) names.append("name="+ s +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games?"+ names.toString()), GameResponse.class);
	}
	
	public GameResponse getTopGames() throws IOException { return this.getTopGames(20); }
	public GameResponse getTopGames(int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?first="+ first), GameResponse.class); }
	public GameResponse getTopGamesAfter(Pagination pagination) throws IOException { return this.getTopGamesAfter(pagination, 20); }
	public GameResponse getTopGamesAfter(Pagination pagination, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?after="+ pagination.getCursor() +"&first="+ first), GameResponse.class); }
	public GameResponse getTopGamesBefore(Pagination pagination) throws IOException { return this.getTopGamesBefore(pagination, 20); }
	public GameResponse getTopGamesBefore(Pagination pagination, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/games/top?before="+ pagination.getCursor() +"&first="+ first), GameResponse.class); }
}
