package tv.twitch.tmi.handle.impl.obj;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Emote {
	@Getter private int emoteID;
	@Getter private List<Position> positions;
	@Getter private Type type;
	
	private String _emoteID;
	private HashMap<Integer, String> urls;
	
	/**
	 * @param size
	 * @return The emotes image URL.
	 */
	public String getURL(Size size) {
		if(getType().equals(Type.FFZ))
			return this.urls.getOrDefault(Integer.parseInt(size.toString(getType())), "");
		String id = this.getEmoteID()+"";
		if(getType().equals(Type.BTTV))
			id = this._emoteID;
		return this.getType().getURL().replaceFirst(Pattern.quote("{{ID}}"), id).replaceFirst(Pattern.quote("{{SIZE}}"), size.toString(this.getType()));
	}
	
	public String getCode(tv.twitch.tmi.handle.impl.obj.irc.Message message) {
		Position pos = positions.get(0);
		return message.getText().substring(pos.start, pos.stop);
	}
	
	public String getCode(tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Whisper.Data whisper) {
		Position pos = positions.get(0);
		return whisper.getBody().substring(pos.start, pos.stop);
	}
	
	public Emote(String id, Position[] positions, Type type) {
		this.emoteID = -1;
		this._emoteID = id;
		this.positions = new ArrayList<>();
		this.type = type;
		
		for(Position pos : positions)
			this.positions.add(pos);
	}
	
	public Emote(int id, Position[] positions) { this(id, positions, Type.TWITCH); }
	public Emote(int id, Position[] positions, Type type) { this(id, positions, type, null); }
	public Emote(int id, Position[] positions, Type type, HashMap<Integer, String> urls) {
		this.emoteID = id;
		this.positions = new ArrayList<>();
		this.type = type;
		
		if(urls != null) {
			this._emoteID = ""+id;
			this.urls = urls;
		}
		
		for(Position pos : positions)
			this.positions.add(pos);
	}
	
	public Emote(int id, String[] positions) { this(id, positions, Type.TWITCH); }
	public Emote(int id, String[] positions, Type type) {
		this.emoteID = id;
		this.positions = new ArrayList<>();
		this.type = type;
		
		for(int i = 0; i < positions.length; i++) {
			int[] pos = Stream.of(positions[i].split("-")).mapToInt(Integer::parseInt).toArray();
			this.positions.add(new Position(pos[0], (pos[1] + 1)));
		}
	}
	
	@Getter
	public static class Position {
		private int start;
		private int stop;
		
		public Position(int start, int stop) {
			this.start = start;
			this.stop = stop;
		}
	}
	
	public enum Type {
		TWITCH("Twitch", "https://static-cdn.jtvnw.net/emoticons/v1/{{ID}}/{{SIZE}}"),
		BTTV("BetterTTV", "https://cdn.betterttv.net/emote/{{ID}}/{{SIZE}}"),
		FFZ("FrankerFaceZ", null);
		
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
		SMALL("1.0", "1x", "1"),
		MEDIUM("2.0", "2x", "2"),
		LARGE("3.0", "3x", "4");
		
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
