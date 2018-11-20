package tv.twitch.tmi.handle.impl.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Badge {
	@SerializedName("id") private Type type;
	@SerializedName("version") private String data;
	
	public Badge(Type type, String data) {
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
		VIP("VIP"),
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
