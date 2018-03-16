package tv.twitch.tmi.obj;

import lombok.Getter;

import java.awt.Color;

public class User {
	@Getter private int id;
	@Getter private String displayName;
	@Getter private String username;
	@Getter private Color color;
	@Getter private boolean mod;
	@Getter private boolean subscriber;
	@Getter private boolean turbo;
	
	public User() {
		this.id = -1;
		this.displayName = null;
		this.username = null;
		this.color = Color.BLACK;
		this.mod = false;
		this.subscriber = false;
		this.turbo = false;
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
