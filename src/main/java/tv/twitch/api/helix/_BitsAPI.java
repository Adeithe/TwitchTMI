package tv.twitch.api.helix;

import tv.twitch.api.helix.obj.BitsLeaderboardResponse;

public class _BitsAPI {
	private API_Helix API;
	
	/**
	 * Not implemented.
	 */
	@Deprecated
	public BitsLeaderboardResponse getLeaderboard() {
		return null;
	}
	
	_BitsAPI(API_Helix API) { this.API = API; }
}
