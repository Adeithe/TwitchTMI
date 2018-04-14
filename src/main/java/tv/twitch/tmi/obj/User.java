package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.Parser;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.exception.MessageSendFailureException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class User {
	private TwitchTMI TMI;
	
	@Getter private int id;
	@Getter private Channel channel;
	@Getter private String displayName;
	@Getter private String username;
	@Getter private Color color;
	@Getter private boolean broadcaster;
	@Getter private boolean mod;
	@Getter private boolean subscriber;
	@Getter private boolean turbo;
	@Getter private List<Badge> badges;
	
	public User(TwitchTMI TMI, Channel channel, String username) {
		this.TMI = TMI;
		
		this.id = -1;
		this.channel = channel;
		this.displayName = null;
		this.username = username.toLowerCase();
		this.color = Color.BLACK;
		this.broadcaster = false;
		this.mod = false;
		this.subscriber = false;
		this.turbo = false;
		this.badges = new ArrayList<Badge>();
	}
	
	/**
	 * Mentions the user with the given message
	 *
	 * @param message
	 * @throws MessageSendFailureException
	 */
	public void mention(String message) throws MessageSendFailureException {
		this.getChannel().sendMessage("@"+ this.getDisplayName() +", "+ message);
	}
	
	/**
	 * Sends a whisper to the user
	 *
	 * @param message
	 */
	public void sendWhisper(String message) throws MessageSendFailureException {
		this.TMI.sendWhisper(this.getUsername(), message);
	}
	
	/**
	 * Checks if the user has the provided badge in the current channel
	 *
	 * @param type
	 * @return
	 */
	public boolean hasBadge(Badge.Type type) {
		for(Badge badge : this.badges)
			if(badge.equals(type))
				return true;
		return false;
	}
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void setUserInfo(String name, String val) {
		if(name.startsWith("@"))
			name = name.replaceFirst("@", "");
		switch(name.toUpperCase()) {
			case "BADGES":
				this.badges = Parser.badges(val);
				this.broadcaster = this.hasBadge(Badge.Type.BROADCASTER);
			break;
			case "COLOR":
				try {
					this.color = Color.decode(val);
				} catch(NumberFormatException e) { this.color = Color.BLACK; }
			break;
			case "DISPLAY-NAME":
				this.displayName = val;
			break;
			case "MOD":
				this.mod = Boolean.parseBoolean(val);
			break;
			case "SUBSCRIBER":
				this.subscriber = Boolean.parseBoolean(val);
			break;
			case "TURBO":
				this.turbo = Boolean.parseBoolean(val);
			break;
			case "USER-ID":
				this.id = Integer.parseInt(val);
			break;
		}
	}
}
