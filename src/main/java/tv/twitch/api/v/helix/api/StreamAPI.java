package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.StreamResponse;

import java.io.IOException;

public class StreamAPI {
	private TwitchAPI API;
	
	public StreamAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public StreamResponse getTopStreams() throws IOException { return this.getTopStreams(20); }
	public StreamResponse getTopStreams(int count) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?first="+ count), StreamResponse.class);
	}
	
	public StreamResponse getTopStreamsByGameID(String game_id) throws IOException { return this.getTopStreamsByGameID(game_id, 20); }
	public StreamResponse getTopStreamsByGameID(String game_id, int count) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?game_id="+ game_id +"&first="+ count), StreamResponse.class);
	}
	
	public StreamResponse getTopStreamsByLanguage(String language) throws IOException { return this.getTopStreamsByLanguage(language, 20); }
	public StreamResponse getTopStreamsByLanguage(String language, int count) throws IOException {
		if(count > 100)
			count = 100;
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?language="+ language +"&first="+ count), StreamResponse.class);
	}
	
	public StreamResponse getStreamByCommunityID(String community_id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?community_id="+ community_id), StreamResponse.class); }
	public StreamResponse getStreamByUsername(String username) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?user_login="+ username.toLowerCase()), StreamResponse.class); }
	public StreamResponse getStreamByUserID(String user_id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?user_id="+ user_id), StreamResponse.class); }
}
