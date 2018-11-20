package tv.twitch.api.v.helix;

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
	private _ClipAPI clips;
	private _GameAPI games;
	private _StreamAPI streams;
	private _UserAPI users;
	private _VideoAPI video;
	
	public API_Helix(TwitchAPI API) {
		this.API = API;
		
		this.analytics = new _AnalyticsAPI(this);
		this.bits = new _BitsAPI(this);
		this.clips = new _ClipAPI(this);
		this.games = new _GameAPI(this);
		this.streams = new _StreamAPI(this);
		this.users = new _UserAPI(this);
		this.video = new _VideoAPI(this);
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
