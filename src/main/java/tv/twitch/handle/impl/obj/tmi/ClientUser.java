package tv.twitch.handle.impl.obj.tmi;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.utils.Parser;
import tv.twitch.utils.Utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

@Getter
public class ClientUser {
	private TwitchTMI TMI;
	
	private int userID;
	private String username;
	private String displayName;
	private List<Badge> badges;
	private Color color;
	
	public ClientUser(TwitchTMI TMI, String username) { this(TMI, username, null); }
	public ClientUser(TwitchTMI TMI, String username, HashMap<String, String> tags) {
		this.TMI = TMI;
		this.username = username.toLowerCase();
		
		if(tags != null) {
			if(!tags.getOrDefault("user-id", "").isEmpty())
				this.userID = Utils.extractNumber(tags.get("user-id"));
			
			if(!tags.getOrDefault("display-name", "").isEmpty())
				this.displayName = tags.get("display-name");
			if(!tags.getOrDefault("badges", "").isEmpty())
				this.badges = Parser.badges(tags.get("badges"));
			if(!tags.getOrDefault("color", "").isEmpty())
				this.color = Color.decode(tags.get("color"));
		}
	}
	
	@Getter
	public static class UserState {
		private TwitchTMI TMI;
		
		private int userID;
		private String username;
		private String displayName;
		private List<Badge> badges;
		private Color color;
		
		private boolean mod;
		private boolean subscriber;
		
		public UserState(TwitchTMI TMI, String username, Channel channel) { this(TMI, username, channel, null); }
		public UserState(TwitchTMI TMI, String username, Channel channel, HashMap<String, String> tags) {
			this.TMI = TMI;
			this.username = username.toLowerCase();
			
			if(tags != null) {
				if(!tags.getOrDefault("user-id", "").isEmpty())
					this.userID = Utils.extractNumber(tags.get("user-id"));
				
				if(!tags.getOrDefault("display-name", "").isEmpty())
					this.displayName = tags.get("display-name");
				if(!tags.getOrDefault("badges", "").isEmpty())
					this.badges = Parser.badges(tags.get("badges"));
				if(!tags.getOrDefault("color", "").isEmpty())
					this.color = Color.decode(tags.get("color"));
				
				if(!tags.getOrDefault("mod", "").isEmpty())
					this.mod = tags.get("mod").equals("1");
				if(!tags.getOrDefault("subscriber", "").isEmpty())
					this.subscriber = tags.get("subscriber").equals("1");
			}
		}
	}
}
