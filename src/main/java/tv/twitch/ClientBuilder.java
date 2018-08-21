package tv.twitch;

import java.util.Random;

public class ClientBuilder {
	private String id;
	private String secret;
	private String username;
	private String token;
	
	private ClientSettings settings;
	
	public ClientBuilder() {
		this.settings = new ClientSettings();
		
		this.username = "justinfan"+ (10000 + new Random().nextInt(89999));
		this.token = "Kappa";
	}
	
	/**
	 * Configures the Client ID used for TwitchAPI calls.
	 *
	 * @param clientID
	 * @return The builder instance.
	 */
	public ClientBuilder withClientID(String clientID) {
		this.id = clientID;
		return this;
	}
	
	/**
	 * Configures the Client Secret used for TwitchAPI calls.
	 *
	 * @param clientSecret
	 * @return The builder instance.
	 */
	public ClientBuilder withClientSecret(String clientSecret) {
		this.secret = clientSecret;
		return this;
	}
	
	/**
	 * Configures the username used for the {@link tv.twitch.tmi.TwitchTMI} connection.
	 *
	 * @param username
	 * @return The builder instance.
	 */
	public ClientBuilder withUsername(String username) {
		this.username = username;
		return this;
	}
	
	/**
	 * Configures the OAuth Token used for the {@link tv.twitch.tmi.TwitchTMI} connection.
	 *
	 * @param oAuth
	 * @return The builder instance.
	 */
	public ClientBuilder withOAuth(String oAuth) {
		if(!oAuth.toLowerCase().startsWith("oauth:"))
			oAuth = "oauth:"+ oAuth;
		this.token = oAuth;
		return this;
	}
	
	/**
	 * When true, the {@link TwitchClient} will print extra details to console for debugging.
	 * WARNING: This will show sensitive data if {@link tv.twitch.ClientSettings.VerboseLevel} is set to ALL!!!
	 *
	 * @param verbose
	 * @return The builder instance.
	 */
	public ClientBuilder setVerbose(ClientSettings.VerboseLevel verbose) {
		this.settings.verbose = verbose;
		return this;
	}
	
	/**
	 * Creates an instance of {@link TwitchClient} with the builder configuration.
	 *
	 * @return The configured client.
	 */
	public TwitchClient build() {
		return new TwitchClient(this.id, this.secret, this.username, this.token, this.settings);
	}
}
