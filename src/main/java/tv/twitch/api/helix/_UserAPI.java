package tv.twitch.api.helix;

import tv.twitch.api.obj.Method;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.helix.API_Helix;
import tv.twitch.api.helix.obj.UserResponse;
import tv.twitch.utils.Utils;

import java.io.IOException;

public class _UserAPI {
	private API_Helix API;
	
	/**
	 * Gets the {@link tv.twitch.api.helix.obj.UserResponse.UserData} for the current authenticated user
	 *
	 * Requires bearerToken to be set when creating the {@link TwitchAPI} instance
	 *
	 * @return
	 * @throws TwitchAPI.APIException
	 * @throws IOException
	 */
	public UserResponse getOwnUser() throws TwitchAPI.APIException, IOException {
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "users"), UserResponse.class);
	}
	
	/**
	 * Gets up to 100 users by their UserID
	 *
	 * @param ids
	 * @return
	 * @throws TwitchAPI.APIException
	 * @throws IOException
	 */
	public UserResponse getUsersByID(int... ids) throws TwitchAPI.APIException, IOException {
		String params = "?";
		for(int id : ids)
			params += "id="+ id +"&";
		params = params.substring(0, params.length() - 1);
		
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "users"+ params), UserResponse.class);
	}
	
	/**
	 * Gets up to 100 users by their username
	 *
	 * @param usernames
	 * @return
	 * @throws TwitchAPI.APIException
	 * @throws IOException
	 */
	public UserResponse getUsersByLogin(String... usernames) throws TwitchAPI.APIException, IOException {
		String params = "?";
		for(String username : usernames)
			params += "login="+ username +"&";
		params = params.substring(0, params.length() - 1);
		
		return Utils.GSON.fromJson(API.CallAPI(Method.GET, "users"+ params), UserResponse.class);
	}
	
	_UserAPI(API_Helix API) { this.API = API; }
}
