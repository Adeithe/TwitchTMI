package tv.twitch.api.helix;

import tv.twitch.api.helix.obj.AnalyticsResponse;

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
