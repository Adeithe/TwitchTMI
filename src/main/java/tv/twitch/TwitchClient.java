package tv.twitch;

import lombok.Getter;
import tv.twitch.events.EventDispatcher;
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
}
