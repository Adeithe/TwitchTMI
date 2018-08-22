package tv.twitch.handle.impl.obj.tmi;

import lombok.Getter;
import lombok.Setter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Channel {
	private TwitchTMI TMI;
	
	private int ID;
	private String name;
	@Setter private ClientUser.UserState userState;
	private boolean connected;
	
	private boolean emoteOnlyMode;
	private boolean R9KMode;
	private boolean subOnlyMode;
	private List<String> mods = new ArrayList<>();
	
	public Channel(TwitchTMI TMI, String name, boolean connected) { this(TMI, name, new HashMap<>(), connected); }
	
	public Channel(TwitchTMI TMI, String name, HashMap<String, String> tags, boolean connected) {
		if(name.startsWith("#"))
			name = name.substring(1);
		
		this.TMI = TMI;
		this.name = name.toLowerCase();
		this.connected = connected;
		
		if(tags != null) {
			if(tags.containsKey("room-id"))
				this.ID = Utils.extractNumber(tags.get("room-id"));
			
			if(tags.containsKey("emote-only"))
				this.emoteOnlyMode = tags.get("emote-only").equals("1");
			if(tags.containsKey("r9k"))
				this.R9KMode = tags.get("r9k").equals("1");
			if(tags.containsKey("subs-only"))
				this.subOnlyMode = tags.get("subs-only").equals("1");
		}
	}
	
	/**
	 * Returns true if the username provided is a mod for this channel
	 *
	 * @param username
	 * @return
	 */
	public boolean isMod(String username) { return this.getMods().contains(username.toLowerCase()); }
	
	/**
	 * Sends a message to the channel if connected.
	 *
	 * @param message
	 * @return Returns true if message was sent.
	 */
	public boolean sendMessage(String message) {
		if(!this.isConnected())
			return false;
		try {
			this.getTMI().sendRawData("PRIVMSG #" + this.getName() +" :"+ message);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Joins the channel if not already connected.
	 *
	 * @throws IOException
	 */
	public void join() throws IOException {
		if(this.isConnected())
			return;
		this.getTMI().sendRawData("JOIN #"+ this.getName());
	}
	
	/**
	 * Leaves the channel if connected.
	 *
	 * @throws IOException
	 */
	public void leave() throws IOException {
		if(!this.isConnected())
			return;
		this.getTMI().sendRawData("PART #"+ this.getName());
	}
}
