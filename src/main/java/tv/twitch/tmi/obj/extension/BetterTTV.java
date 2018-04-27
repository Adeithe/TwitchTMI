package tv.twitch.tmi.obj.extension;

import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Getter
public class BetterTTV {
	private int status;
	private String urlTemplate;
	private List<String> bots;
	private List<Emote> emotes;
	
	@Getter
	public static class Emote {
		private String channel;
		private String code;
		private String id;
		private String imageType;
		
		/**
		 * Returns the URL to the emote image as a string.
		 *
		 * @param size
		 * @return
		 */
		public String getURLAsString(Size size) {
			return "https://cdn.betterttv.net/emote/"+ this.getId() +"/"+ size.toSize() +"x";
		}
		
		/**
		 * Returns the URL to the emote image.
		 *
		 * @param size
		 * @return
		 * @throws MalformedURLException
		 */
		public URL getURL(Size size) throws MalformedURLException {
			return new URL(this.getURLAsString(size));
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