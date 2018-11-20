package tv.twitch.api.v.helix;

import tv.twitch.api.v.helix.obj.AnalyticsResponse;

public class _AnalyticsAPI {
	private API_Helix API;
	
	/**
	 * Not implemented.
	 */
	@Deprecated
	public AnalyticsResponse getExtensionAnalytics() {
		return null;
	}
	
	/**
	 * Not implemented.
	 */
	@Deprecated
	public AnalyticsResponse getGameAnalytics() {
		return null;
	}
	
	_AnalyticsAPI(API_Helix API) { this.API = API; }
}
