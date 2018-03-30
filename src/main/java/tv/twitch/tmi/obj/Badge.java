package tv.twitch.tmi.obj;

import lombok.Getter;

public class Badge {
	@Getter private Type type;
	@Getter private int data;
	
	public Badge(Type type, int data) {
		this.type = type;
		this.data = data;
	}
	
	public enum Type {
		ADMIN("ADMIN"),
		STAFF("STAFF"),
		GLOBAL_MOD("GLOBAL_MOD"),
		BROADCASTER("BROADCASTER"),
		MODERATOR("MODERATOR"),
		BITS("BITS"),
		CLIP_CHAMP("CLIP_CHAMP"),
		SUBSCRIBER("SUBSCRIBER"),
		PREMIUM("PREMIUM"),
		TURBO("TURBO"),
		UNKNOWN(null);
		
		private String type;
		
		Type(String type) {
			this.type = type;
		}
		
		public String toString() {
			return this.type;
		}
	}
}
