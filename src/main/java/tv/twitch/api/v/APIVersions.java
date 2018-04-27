package tv.twitch.api.v;

import lombok.Getter;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.HelixAPI;

@Getter
public class APIVersions {
	private TwitchAPI API;
	
	private HelixAPI helix;
	
	public APIVersions(TwitchAPI API) {
		this.API = API;
		
		this.helix = new HelixAPI(this.API);
	}
}
