package tv.twitch.handle.impl.obj.tmi;

import lombok.Getter;

@Getter
public class Badge {
	private Type type;
	private int data;
	
	public Badge(Type type, int data) {
		this.type = type;
		this.data = data;
	}
	
	public enum Type {
		UNKNOWN("UNKNOWN"),
		ADMIN("ADMIN"),
		STAFF("STAFF"),
		GLOBAL_MOD("GLOBAL_MOD"),
		BROADCASTER("BROADCASTER"),
		MODERATOR("MODERATOR"),
		BITS("BITS"),
		BITS_LEADER("BITS_LEADER"),
		CLIP_CHAMP("CLIP_CHAMP"),
		SUB_GIFTER("SUB_GIFTER"),
		SUBSCRIBER("SUBSCRIBER"),
		PREMIUM("PREMIUM"),
		TURBO("TURBO"),
		TWITCHCON2017("TWITCHCON2017"),
		TWITCHCON2018("TWITCHCON2018");
		
		@Getter String name;
		Type(String name) { this.name = name; }
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
