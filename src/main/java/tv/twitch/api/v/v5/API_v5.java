package tv.twitch.api.v.v5;

import tv.twitch.api.obj.APIVersion;
import tv.twitch.api.obj.Header;
import tv.twitch.api.obj.Method;
import tv.twitch.api.TwitchAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated. No scheduled removal date.
 */
@Deprecated
public class API_v5 {
	public static final APIVersion VERSION = APIVersion.KRAKEN;
	
	private TwitchAPI API;
	
	public API_v5(TwitchAPI API) {
		this.API = API;
	}
	
	public String CallAPI(Method method, String path) throws TwitchAPI.APIException, IOException {
		if(!path.toLowerCase().startsWith("kraken/"))
			path = "kraken/"+ path;
		
		List<Header> headers = new ArrayList<>();
				headers.add(new Header("Accept", "application/vnd.twitchtv.v5+json"));
				if(API.getBearerToken() == null) {
					if(API.getClient().getClientID() != null)
						headers.add(new Header("Client-ID", API.getClient().getClientID()));
				} else
					headers.add(new Header("Authorization", VERSION.getTokenPrefix() +" "+ API.getBearerToken()));
		return API.CallAPI(method, path, headers);
	}
}
