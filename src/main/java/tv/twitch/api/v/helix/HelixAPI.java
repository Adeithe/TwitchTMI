package tv.twitch.api.v.helix;

import lombok.Getter;
import tv.twitch.api.TwitchAPI;
import tv.twitch.api.v.helix.api.ClipAPI;
import tv.twitch.api.v.helix.api.GameAPI;
import tv.twitch.api.v.helix.api.StreamAPI;

@Getter
public class HelixAPI {
	private TwitchAPI API;
	
	private ClipAPI clips;
	private GameAPI games;
	private StreamAPI stream;
	
	public HelixAPI(TwitchAPI API) {
		this.API = API;
		
		this.clips = new ClipAPI(this.API);
		this.games = new GameAPI(this.API);
		this.stream = new StreamAPI(this.API);
	}
}
