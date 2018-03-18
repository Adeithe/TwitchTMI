package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.exception.MessageSendFailureException;

import java.awt.Color;

public class User {
	private TwitchTMI TMI;
	
	@Getter private int id;
	@Getter private String displayName;
	@Getter private String username;
	@Getter private Color color;
	@Getter private boolean mod;
	@Getter private boolean subscriber;
	@Getter private boolean turbo;
	
	public User(TwitchTMI TMI) {
		this.TMI = TMI;
		
		this.id = -1;
		this.displayName = null;
		this.username = null;
		this.color = Color.BLACK;
		this.mod = false;
		this.subscriber = false;
		this.turbo = false;
	}
	
	/**
	 * Sends a whisper to the user
	 *
	 * @param message
	 */
	public void sendWhisper(String message) throws MessageSendFailureException {
		this.TMI.sendWhisper(this.username, message);
	}
	
	protected void setUserInfo(String name, String val) {
		if(name.startsWith("@"))
			name = name.replaceFirst("@", "");
		switch(name.toUpperCase()) {
			case "COLOR":
				try {
					this.color = Color.decode(val);
				} catch(NumberFormatException e) { this.color = Color.BLACK; }
			break;
			case "DISPLAY-NAME":
				this.displayName = val;
				this.username = val.toLowerCase();
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
