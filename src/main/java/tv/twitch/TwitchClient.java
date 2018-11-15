package tv.twitch;

import lombok.Getter;
import tv.twitch.api.TwitchAPI;
import tv.twitch.tmi.events.EventDispatcher;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class TwitchClient {
	//API Details
	private String clientID;
	private String clientSecret;
	
	//TMI Details
	private String username;
	private String oAuth;
	
	//Internals
	private ClientSettings settings;
	private EventDispatcher eventDispatcher;
	private TwitchTMI TMI;
	
	TwitchClient(String clientID, String clientSecret, String username, String token, ClientSettings settings) {
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		
		this.username = username.toLowerCase();
		this.oAuth = token;
		
		this.settings = settings;
		this.eventDispatcher = new EventDispatcher(this, this.settings.piggyback, this.settings.minimumPoolSize, this.settings.maximumPoolSize, this.settings.overflow, this.settings.threadTimeout, this.settings.threadTimeoutUnit);
		
		this.TMI = new TwitchTMI(this);
	}
	
	/**
	 * Returns an instance of {@link TwitchAPI} that does not use an OAuth Token
	 *
	 * @return
	 */
	public TwitchAPI getAPI() { return this.getAPI(null); }
	
	/**
	 * Returns an instance of {@link TwitchAPI} using the given OAuth Token
	 *
	 * @param bearerToken
	 * @return
	 */
	public TwitchAPI getAPI(String bearerToken) { return new TwitchAPI(this, bearerToken); }
	
	public static class Builder extends TwitchClientBuilder {}
}
