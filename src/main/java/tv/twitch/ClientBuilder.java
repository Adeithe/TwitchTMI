package tv.twitch;

import java.util.Random;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

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
	 * Sets a {@link RejectedExecutionHandler} to be used for event handling.
	 *
	 * @param piggyBack
	 * @return
	 */
	public ClientBuilder withPiggyBack(RejectedExecutionHandler piggyBack) {
		this.settings.piggyback = piggyBack;
		return this;
	}
	
	/**
	 * Sets a new minimum pool size for event handling.
	 *
	 * @param minimumPoolSize
	 * @return
	 */
	public ClientBuilder setMinimumPoolSize(int minimumPoolSize) {
		this.settings.minimumPoolSize = minimumPoolSize;
		return this;
	}
	
	/**
	 * Sets a new maximum pool size for event handling.
	 *
	 * @param maximumPoolSize
	 * @return
	 */
	public ClientBuilder setMaximumPoolSize(int maximumPoolSize) {
		this.settings.maximumPoolSize = maximumPoolSize;
		return this;
	}
	
	/**
	 * Sets a new thread timeout for event handling.
	 *
	 * @param threadTimeout
	 * @param threadTimeoutUnit
	 * @return
	 */
	public ClientBuilder setThreadTimeout(long threadTimeout, TimeUnit threadTimeoutUnit) {
		this.settings.threadTimeout = threadTimeout;
		this.settings.threadTimeoutUnit = threadTimeoutUnit;
		return this;
	}
	
	/**
	 * Sets a new overflow for event handling.
	 *
	 * @param overflow
	 * @return
	 */
	public ClientBuilder setOverflow(int overflow) {
		this.settings.overflow = overflow;
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
