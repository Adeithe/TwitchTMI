package tv.twitch.tmi;

import lombok.Getter;
import tv.twitch.TwitchClient;
import tv.twitch.handle.impl.obj.tmi.Channel;
import tv.twitch.handle.impl.obj.tmi.ClientUser;

import java.io.IOException;
import java.util.HashMap;

public class TwitchTMI {
	@Getter private TwitchClient client;
	private ChatService ChatService;
	
	@Getter private boolean anonymous;
	
	public TwitchTMI(TwitchClient client) {
		this.client = client;
		this.anonymous = (this.getClient().getUsername().startsWith("justinfan") && !this.getClient().getOAuth().toLowerCase().startsWith("oauth:"));
	}
	
	/**
	 * Gets all channels the {@link ChatService} is listening to.
	 *
	 * @return A {@link HashMap} of {@link Channel} objects.
	 */
	public HashMap<String, Channel> getConnectedChannels() {
		if(this.ChatService != null)
			return new HashMap<>(this.ChatService.getConnectedChannels());
		return new HashMap<>();
	}
	
	/**
	 * Gets the {@link Channel} object.
	 *
	 * @param name
	 * @return The {@link Channel} object.
	 */
	public Channel getChannel(String name) {
		if(name.startsWith("#"))
			name = name.substring(1);
		if(this.ChatService != null)
			return this.getConnectedChannels().getOrDefault(name.toLowerCase(), new Channel(this, name.toLowerCase(), false));
		return new Channel(this, name.toLowerCase(), false);
	}
	
	/**
	 * Send raw data to TwitchIRC via {@link ChatService}
	 *
	 * @param data
	 * @throws IOException
	 */
	public void sendRawData(String... data) throws IOException {
		if(this.ChatService != null && this.ChatService.isConnected())
			this.ChatService.sendRawData(data);
	}
	
	/**
	 * @return The current {@link ClientUser} object.
	 */
	public ClientUser getClientUser() {
		if(this.ChatService == null || this.ChatService.getClientUser() == null)
			return null;
		return this.ChatService.getClientUser();
	}
	
	/**
	 * Pings TwitchIRC if {@link ChatService} is connected.
	 *
	 * @throws IOException
	 */
	public void ping() throws IOException { this.sendRawData("PING"); }
	
	/**
	 * Starts the {@link ChatService} and connects to TwitchIRC
	 */
	public void login() {
		if(this.ChatService == null)
			this.ChatService = new ChatService(this);
		if(!this.ChatService.isConnected())
			this.ChatService.start();
	}
	
	public void reconnect() {
		if(this.ChatService == null) {
			this.login();
			return;
		}
		this.ChatService.reconnect();
	}
	
	/**
	 * Disconnects from the {@link ChatService}
	 */
	public void logout() {
		if(this.ChatService != null && this.ChatService.isConnected())
			this.ChatService.disconnect();
	}
}
