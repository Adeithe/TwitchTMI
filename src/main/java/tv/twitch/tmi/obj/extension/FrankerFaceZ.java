package tv.twitch.tmi.obj.extension;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Getter
public class FrankerFaceZ {
	private Room room;
	private HashMap<Integer, Set> sets;
	
	@Getter
	public static class Room {
		@SerializedName("twitch_id") private int twitchId;
		private String id;
		@SerializedName("display_name") private String displayName;
		@SerializedName("moderator_badge") private String moderatorBadge;
	}
	
	@Getter
	public static class Set {
		@SerializedName("emoticons") private List<Emote> emotes;
	}
	
	@Getter
	public static class Emote {
		private int id;
		private String name;
		@SerializedName("public") private boolean shared;
		private boolean modifier;
		private boolean hidden;
		private int height;
		private int width;
		
		/**
		 * Returns the URL to the emote image as a string.
		 *
		 * @param size
		 * @return
		 */
		public String getURLAsString(BetterTTV.Emote.Size size) {
			return "https://cdn.frankerfacez.com/emoticon/"+ this.getId() +"/"+ size.toSize();
		}
		
		/**
		 * Returns the URL to the emote image.
		 *
		 * @param size
		 * @return
		 * @throws MalformedURLException
		 */
		public URL getURL(BetterTTV.Emote.Size size) throws MalformedURLException {
			return new URL(this.getURLAsString(size));
		}
		
		@Getter
		public static class Owner {
			private String name;
			@SerializedName("display_name") private String displayName;
		}
		
		public enum Size {
			SMALL("SMALL", 1),
			MEDIUM("MEDIUM", 2),
			LARGE("LARGE", 3);
			
			private String size;
			private int i;
			
			Size(String size, int i) {
				this.size = size;
				this.i = i;
			}
			
			public String toString() {
				return this.size;
			}
			
			public int toSize() {
				return this.i;
			}
		}
	}
}