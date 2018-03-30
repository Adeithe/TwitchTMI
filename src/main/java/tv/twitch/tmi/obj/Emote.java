package tv.twitch.tmi.obj;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Emote {
	@Getter private int ID;
	@Getter private List<Position> positions;
	@Getter private EmoteURL url;
	
	public Emote(int id, String[] positions) {
		this.ID = id;
		this.positions = new ArrayList<Position>();
		this.url = new EmoteURL(id);
		
		for(int i = 0; i < positions.length; i++) {
			String[] parts = positions[i].split("-");
			int start = Integer.parseInt(parts[0]);
			int stop = Integer.parseInt(parts[1]);
			this.positions.add(new Position(start, stop));
		}
	}
	
	public static class EmoteURL {
		@Getter private String small;
		@Getter private String medium;
		@Getter private String large;
		
		public EmoteURL(int id) {
			this.small = this.toURL(id, 1);
			this.medium = this.toURL(id, 2);
			this.large = this.toURL(id, 3);
		}
		
		private String toURL(int id, int size) {
			return "http://static-cdn.jtvnw.net/emoticons/v1/"+ id +"/"+ size +".0";
		}
	}
	
	public static class Position {
		@Getter private int start;
		@Getter private int stop;
		
		public Position(int start, int stop) {
			this.start = start;
			this.stop = stop;
		}
	}
}
