package tv.twitch.api.helix;

import lombok.Getter;
import tv.twitch.api.obj.APIVersion;
import tv.twitch.api.obj.Header;
import tv.twitch.api.obj.Method;
import tv.twitch.api.TwitchAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Supported.
 */
@Getter
public class API_Helix {
	public static final APIVersion VERSION = APIVersion.HELIX;
	
	private TwitchAPI API;
	
	private _AnalyticsAPI analytics;
	private _BitsAPI bits;
	private _UserAPI users;
	
	public API_Helix(TwitchAPI API) {
		this.API = API;
		
		this.analytics = new _AnalyticsAPI(this);
		this.bits = new _BitsAPI(this);
		this.users = new _UserAPI(this);
	}
	
	public String CallAPI(Method method, String path) throws TwitchAPI.APIException, IOException {
		if(!path.toLowerCase().startsWith("helix/"))
			path = "helix/"+ path;
		
		List<Header> headers = new ArrayList<>();
		if(API.getBearerToken() == null) {
			if(API.getClient().getClientID() != null)
				headers.add(new Header("Client-ID", API.getClient().getClientID()));
		} else
			headers.add(new Header("Authorization", VERSION.getTokenPrefix() +" "+ API.getBearerToken()));
		return API.CallAPI(method, path, headers);
	}
}
