package tv.twitch.tmi.irc;

import com.frankerfacez.FrankerFaceZ;
import lombok.Getter;
import lombok.Setter;
import net.betterttv.BetterTTV;
import tv.twitch.TwitchClient;
import tv.twitch.tmi.handle.impl.obj.Emote;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.handle.impl.obj.irc.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TwitchTMI {
	@Getter private TwitchClient client;
	private ChatService ChatService;
	
	@Getter private boolean anonymous;
	@Getter private boolean usingExtras = true;
	
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
	 * @return The current {@link User} object.
	 */
	public User getClientUser() {
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
	
	/**
	 * Reconnects to the {@link ChatService}.
	 */
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
	
	public void setUsingExtras(boolean useExtras) {
		try {
			usingExtras = useExtras;
			ChatService.BetterTTV();
			ChatService.FrankerFaceZ();
		} catch(IOException e) {
			if(useExtras)
				e.printStackTrace();
		}
	}
	
	/**
	 * Gets a clone BetterTTV Emote List. Modifications to this are not saved.
	 *
	 * @return
	 */
	public List<BetterTTV.Emote> getGlobalBTTVEmotes() {
		List<BetterTTV.Emote> copy = new ArrayList<>(ChatService.getGlobalBTTVEmotes().size());
		for(BetterTTV.Emote emote : ChatService.getGlobalBTTVEmotes())
			copy.add(emote);
		return copy;
	}
	
	/**
	 * Gets a clone FrankerFaceZ Emote List. Modifications to this are not saved.
	 *
	 * @return
	 */
	public List<FrankerFaceZ.Emote> getGlobalFFZEmotes() {
		List<FrankerFaceZ.Emote> copy = new ArrayList<>(ChatService.getGlobalFFZEmotes().size());
		for(FrankerFaceZ.Emote emote : ChatService.getGlobalFFZEmotes())
			copy.add(emote);
		return copy;
	}
}
