package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.Parser;
import tv.twitch.tmi.TwitchTMI;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

@Getter
public class ClientUser {
	private TwitchTMI TMI;
	private User user;
	private HashMap<String, UserState> userstates;
	
	public ClientUser(TwitchTMI TMI, User user) {
		this.TMI = TMI;
		this.user = user;
		this.userstates = new HashMap<String, UserState>();
	}
	
	public UserState getUserStateForChannel(Channel channel) {
		if(this.getUserstates().containsKey(channel.getName().toLowerCase()))
			return this.getUserstates().get(channel.getName().toLowerCase());
		return null;
	}
	
	@Getter
	public static class UserState {
		private TwitchTMI TMI;
		
		private int id;
		
		private String username;
		private String displayName;
		
		private Color color;
		private List<Badge> badges;
		
		public UserState(TwitchTMI TMI, String username, HashMap<String, String> data) {
			this.TMI = TMI;
			
			this.username = username.toLowerCase();
			this.displayName = data.getOrDefault("display-name", this.username);
			
			if(data.containsKey("user-id"))
				this.id = Integer.parseInt(data.get("user-id"));
			if(data.containsKey("color"))
				this.color = (data.get("color").length()>0)?Color.decode(data.get("color")):Color.BLACK;
			
			this.badges = Parser.badges(data.getOrDefault("badges", ""));
		}
		
		public boolean isAdmin() { return this.hasBadge(Badge.Type.ADMIN);	}
		public boolean isStaff() { return (this.hasBadge(Badge.Type.ADMIN) || this.hasBadge(Badge.Type.STAFF)); }
		public boolean isGlobalMod() { return this.hasBadge(Badge.Type.GLOBAL_MOD); }
		public boolean isBroadcaster() { return this.hasBadge(Badge.Type.BROADCASTER); }
		public boolean isMod() { return (this.hasBadge(Badge.Type.MODERATOR) || this.hasBadge(Badge.Type.BROADCASTER)); }
		
		public boolean isSubscriber() { return this.hasBadge(Badge.Type.SUBSCRIBER); }
		
		public boolean hasBadge(Badge.Type type) {
			for(Badge badge : this.badges)
				if(badge.getType().equals(type))
					return true;
			return false;
		}
	}
}
