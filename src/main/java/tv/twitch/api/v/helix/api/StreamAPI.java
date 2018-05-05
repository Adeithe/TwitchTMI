package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.api.v.helix.obj.StreamResponse;
import tv.twitch.tmi.Utils;

import java.io.IOException;
import java.util.List;

public class StreamAPI {
	private TwitchAPI API;
	
	public StreamAPI(TwitchAPI API) {
		this.API = API;
	}
	
	public StreamResponse getTopStreams() throws IOException { return this.getTopStreams(20); }
	public StreamResponse getTopStreams(int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?first="+ first), StreamResponse.class); }
	public StreamResponse getTopStreamsAfter(int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?first="+ first +"&after="+ pagination.getCursor()), StreamResponse.class); }
	public StreamResponse getTopStreamsBefore(int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?first="+ first +"&before="+ pagination.getCursor()), StreamResponse.class); }
	
	public StreamResponse getStreamByUsername(String username) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?user_login="+ username.toLowerCase()), StreamResponse.class); }
	public StreamResponse getStreamsByUsername(List<String> usernames) throws IOException, ArrayIndexOutOfBoundsException { return this.getStreamsByUsername(usernames.toArray(new String[0])); }
	public StreamResponse getStreamsByUsername(String... username) throws IOException, ArrayIndexOutOfBoundsException {
		if(username.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 streams!");
		StringBuilder usernames = new StringBuilder();
		for(String u : username) usernames.append("user_login="+ u.toLowerCase() +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?"+ usernames.toString()), StreamResponse.class);
	}
	
	public StreamResponse getStreamByUserID(int user_id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?user_id="+ user_id), StreamResponse.class); }
	public StreamResponse getStreamsByUserID(List<Integer> user_ids) throws IOException, ArrayIndexOutOfBoundsException { return this.getStreamsByUserID(Utils.toIntArray(user_ids)); }
	public StreamResponse getStreamsByUserID(int... user_id) throws IOException, ArrayIndexOutOfBoundsException {
		if(user_id.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 streams!");
		StringBuilder usernames = new StringBuilder();
		for(int id : user_id) usernames.append("user_id="+ id +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/streams?"+ usernames.toString()), StreamResponse.class);
	}
}
