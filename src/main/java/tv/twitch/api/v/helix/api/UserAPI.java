package tv.twitch.api.v.helix.api;

import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.obj.Pagination;
import tv.twitch.api.v.helix.obj.UserFollowsResponse;
import tv.twitch.api.v.helix.obj.UserResponse;
import tv.twitch.tmi.Utils;

import java.io.IOException;
import java.util.List;

public class UserAPI {
	private TwitchAPI API;
	
	public UserAPI(TwitchAPI API) { this.API = API; }
	
	public UserResponse getUserByUsername(String username) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users?login="+ username), UserResponse.class); }
	public UserResponse getUserByUsername(List<String> usernames) throws IOException, ArrayIndexOutOfBoundsException { return this.getUserByUsername(usernames.toArray(new String[0])); }
	public UserResponse getUserByUsername(String... username) throws IOException, ArrayIndexOutOfBoundsException {
		if(username.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 streams!");
		StringBuilder usernames = new StringBuilder();
		for(String u : username) usernames.append("login="+ u.toLowerCase() +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users?"+ usernames.toString()), UserResponse.class);
	}
	
	public UserResponse getUserByID(int user_id) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users?id="+ user_id), UserResponse.class); }
	public UserResponse getUserByID(List<Integer> user_ids) throws IOException, ArrayIndexOutOfBoundsException { return this.getUserByID(Utils.toIntArray(user_ids)); }
	public UserResponse getUserByID(int... user_id) throws IOException, ArrayIndexOutOfBoundsException {
		if(user_id.length > 100)
			throw new ArrayIndexOutOfBoundsException("You can only request a max of 100 streams!");
		StringBuilder usernames = new StringBuilder();
		for(int id : user_id) usernames.append("id="+ id +"&");
		return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users?"+ usernames.toString()), UserResponse.class);
	}
	
	public UserFollowsResponse getUserFollowers(int user_id) throws IOException { return this.getUserFollowers(user_id, 20); }
	public UserFollowsResponse getUserFollowers(int user_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users/follows?to_id="+ user_id +"&first="+ first), UserFollowsResponse.class); }
	public UserFollowsResponse getUserFollowers(int user_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users/follows?to_id="+ user_id +"&first="+ first +"after="+ pagination.getCursor()), UserFollowsResponse.class); }
	
	public UserFollowsResponse getUserFollowing(int user_id) throws IOException { return this.getUserFollowing(user_id, 20); }
	public UserFollowsResponse getUserFollowing(int user_id, int first) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users/follows?from_id="+ user_id +"&first="+ first), UserFollowsResponse.class); }
	public UserFollowsResponse getUserFollowing(int user_id, int first, Pagination pagination) throws IOException { return this.API.getGson().fromJson(this.API.CallAPI(Method.GET, "helix/users/follows?from_id="+ user_id +"&first="+ first +"after="+ pagination.getCursor()), UserFollowsResponse.class); }
}
