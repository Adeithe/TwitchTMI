package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.api.Method;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.Utils;
import tv.twitch.tmi.exception.MessageSendFailureException;
import tv.twitch.tmi.obj.extension.BetterTTV;
import tv.twitch.tmi.obj.extension.FrankerFaceZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Channel {
	private TwitchTMI TMI;
	
	private boolean connected;
	
	private int id;
	private String name;
	
	private List<BetterTTV.Emote> BTTVEmotes;
	private List<FrankerFaceZ.Emote> FFZEmotes;
	
	private List<String> mods;
	
	public boolean ritualsEnabled;
	public boolean emoteOnly;
	public boolean R9KMode;
	public boolean subMode;
	
	public int followerMode = -1;
	public int slowMode = 0;
	
	public boolean clientSubscriber;
	
	public Channel(TwitchTMI TMI, String name) { this(TMI, name, new HashMap<String, String>()); }
	public Channel(TwitchTMI TMI, String name, HashMap<String, String> data) {
		this.TMI = TMI;
		
		this.name = name.toLowerCase().replaceFirst("#", "");
		
		this.BTTVEmotes = new ArrayList<BetterTTV.Emote>();
		this.FFZEmotes = new ArrayList<FrankerFaceZ.Emote>();
		
		this.mods = new ArrayList<String>();
		
		boolean connected = false;
		if(!data.isEmpty())
			connected = true;
		this.roomstate(data, connected);
	}
	
	/**
	 * Sends a message to the channel if it is connected
	 *
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void sendMessage(String message) throws MessageSendFailureException {
		if(this.isConnected())
			this.TMI.sendMessage(this.getName(), message);
	}
	
	/**
	 * Joins the channel if not already connected
	 *
	 * @throws Exception
	 */
	public void join() throws Exception {
		if(!this.isConnected())
			this.TMI.sendRawData("JOIN #"+ this.getName());
	}
	
	/**
	 * Checks if the client user is a mod in this channel
	 *
	 * @return
	 */
	public boolean isMod() {
		return this.isMod(this.TMI.getUsername());
	}
	
	/**
	 * Checks if the provided user is a mod in this channel
	 *
	 * @param username
	 * @return
	 */
	public boolean isMod(String username) {
		username = username.replaceAll(" ", "").toLowerCase();
		if(this.getMods().contains(username))
			return true;
		return false;
	}
	
	/**
	 * Leaves the channel if connected
	 *
	 * @throws Exception
	 */
	public void leave() throws Exception {
		if(this.isConnected())
			this.TMI.sendRawData("PART #" + this.getName());
		this.connected = false;
	}
	
	/**
	 * Returns true if Slow Mode is enabled for this channel
	 *
	 * @return
	 */
	public boolean isSlowMode() { return (this.slowMode > 0); }
	
	/**
	 * Returns true if Followers Only Mode is enabled for this channel
	 *
	 * @return
	 */
	public boolean isFollowersOnly() { return (this.followerMode > -1); }
	
	@Deprecated
	public boolean isFollowersOnlyMode() { return this.isFollowersOnly(); }
	
	/**
	 * !!! DO NOT USE !!!
	 * This method is used internally
	 *
	 * @param data
	 */
	public void roomstate(HashMap<String, String> data, boolean connected) {
		this.connected = connected;
		
		if(data.isEmpty())
			return;
		
		if(data.containsKey("room-id"))
			this.id = Integer.parseInt(data.get("room-id"));
		
		if(data.containsKey("rituals"))
			if(!data.get("rituals").equals("0"))
				this.ritualsEnabled = true;
		
		if(data.containsKey("emote-only"))
			if(!data.get("emote-only").equals("0"))
				this.emoteOnly = true;
		
		if(data.containsKey("r9k"))
			if(!data.get("r9k").equals("0"))
				this.R9KMode = true;
		
		if(data.containsKey("subs-only"))
			if(!data.get("subs-only").equals("0"))
				this.subMode = true;
		
		if(data.containsKey("followers-only"))
			this.followerMode = Integer.parseInt(data.get("followers-only"));
		
		if(data.containsKey("slow"))
			this.slowMode = Integer.parseInt(data.get("slow"));
	}
	
	/**
	 * !!! DO NOT USE !!!
	 * This method is used internally
	 */
	public void update() throws MessageSendFailureException, IOException {
		this.sendMessage("/mods");
		
		this.getBTTVEmotes().clear();
		BetterTTV bttv = Utils.GSON.fromJson(Utils.CallAPI(Method.GET, TwitchTMI.BTTV_BASE_URL +"channels/"+ getName().toLowerCase()), BetterTTV.class);
		for(BetterTTV.Emote emote : bttv.getEmotes()) {
			this.getBTTVEmotes().add(emote);
			if(this.getTMI().isVerbose())
				System.out.println("[BetterTTV] Registered emote "+ emote.getCode() +" for channel "+ this.getName().toLowerCase() +"!");
		}
		
		this.getFFZEmotes().clear();
		FrankerFaceZ ffz = Utils.GSON.fromJson(Utils.CallAPI(Method.GET, TwitchTMI.FFZ_BASE_URL +"room/"+ getName().toLowerCase()), FrankerFaceZ.class);
		for(int key : ffz.getSets().keySet()) {
			FrankerFaceZ.Set set = ffz.getSets().get(key);
			for(FrankerFaceZ.Emote emote : set.getEmotes()) {
				this.getFFZEmotes().add(emote);
				if(this.getTMI().isVerbose())
					System.out.println("[FrankerFaceZ] Registered emote "+ emote.getName() +" for channel "+ this.getName().toLowerCase() +"!");
			}
		}
	}
}
