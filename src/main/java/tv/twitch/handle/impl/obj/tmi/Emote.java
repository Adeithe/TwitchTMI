package tv.twitch.handle.impl.obj.tmi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
public class Emote {
	private int emoteID;
	private String code;
	private List<Position> positions;
	private Type type;
	
	/**
	 * @param size
	 * @return The emotes image URL.
	 */
	public String getURL(Size size) { return this.getType().getURL().replaceFirst(Pattern.quote("{{ID}}"), this.getEmoteID()+"").replaceFirst(Pattern.quote("{{SIZE}}"), size.toString(this.getType())); }
	
	public Emote(int id, String code, String[] positions, Type type) {
		this.emoteID = id;
		this.code = code;
		this.positions = new ArrayList<>();
		this.type = type;
		
		for(int i = 0; i < positions.length; i++) {
			int[] pos = Stream.of(positions[i].split("-")).mapToInt(Integer::parseInt).toArray();
			this.positions.add(new Position(pos[0], pos[1]));
		}
	}
	
	@Getter
	public static class Position {
		private int start;
		private int stop;
		
		Position(int start, int stop) {
			this.start = start;
			this.stop = stop;
		}
	}
	
	public enum Type {
		TWITCH("Twitch", "https://static-cdn.jtvnw.net/emoticons/v1/{{ID}}/{{SIZE}}"),
		BTTV("BetterTTV", "https://cdn.betterttv.net/emote/{{ID}}/{{SIZE}}"),
		FFZ("FrankerFaceZ", "https://cdn.frankerfacez.com/{{ID}}.PNG"); //FFZ urls may be invalid as emotes could end with .png or .PNG
		
		String name;
		String url;
		Type(String name, String url) {
			this.name = name;
			this.url = url;
		}
		
		public String getURL() { return this.url; }
		public String getName() { return this.name; }
		
		@Override
		public String toString() { return this.name; }
	}
	
	public enum Size {
		SMALL("1.0", "1x", ""),
		MEDIUM("2.0", "2x", ""),
		LARGE("3.0", "3x", "");
		
		String twitch, bttv, ffz;
		Size(String twitch, String bttv, String ffz) {
			this.twitch = twitch;
			this.bttv = bttv;
			this.ffz = ffz;
		}
		
		public String toString(Type type) {
			switch(type) {
				case BTTV:
					return this.bttv;
				case FFZ:
					return this.ffz;
				
				default:
					return this.twitch;
			}
		}
		
		@Override
		public String toString() { return this.toString(Type.TWITCH); }
	}
}
