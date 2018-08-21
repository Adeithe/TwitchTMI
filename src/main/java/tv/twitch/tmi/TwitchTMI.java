package tv.twitch.tmi;

import lombok.Getter;
import tv.twitch.TwitchClient;

public class TwitchTMI {
	@Getter private TwitchClient client;
	private ChatService ChatService;
	
	@Getter private boolean anonymous;
	
	public TwitchTMI(TwitchClient client) {
		this.client = client;
		
		this.anonymous = (this.getClient().getUsername().startsWith("justinfan") && !this.getClient().getOAuth().toLowerCase().startsWith("oauth:"));
	}
	
	/**
	 * Starts the {@link ChatService} and connects to TwitchIRC
	 */
	public void login() {
		if(this.ChatService == null)
			this.ChatService = new ChatService(this);
		if(!this.ChatService.isConnected())
			this.ChatService.start();
	}
	
	/**
	 * Disconnects from the {@link ChatService}
	 */
	public void logout() {
		if(this.ChatService != null && this.ChatService.isConnected())
			this.ChatService.disconnect();
	}
}
