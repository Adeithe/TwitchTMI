package tv.twitch.tmi.obj;

import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Emote {
	private int Id;
	private String name;
	private List<Position> positions;
	
	public Emote(int id, String[] positions) {
		this.Id = id;
		this.positions = new ArrayList<Position>();
		
		for(int i = 0; i < positions.length; i++) {
			String[] parts = positions[i].split("-");
			int start = Integer.parseInt(parts[0]);
			int stop = Integer.parseInt(parts[1]);
			this.positions.add(new Position(start, stop));
		}
	}
	
	/**
	 * Returns the URL to the emote image as a string.
	 *
	 * @param size
	 * @return
	 */
	public String getURLAsString(Size size) {
		return "https://static-cdn.jtvnw.net/emoticons/v1/"+ this.getId() +"/"+ size.toSize();
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
	
	/**
	 * <b>!!! DO NOT USE !!!</b>
	 * This method is used internally and may cause issues if you call this method.
	 */
	public void __setName(String name) {
		this.name = name;
	}
	
	public static class Position {
		@Getter private int start;
		@Getter private int stop;
		
		public Position(int start, int stop) {
			this.start = start;
			this.stop = stop;
		}
	}
	
	public enum Size {
		SMALL("SMALL", 1.0),
		MEDIUM("MEDIUM", 2.0),
		LARGE("LARGE", 3.0);
		
		private String size;
		private double d;
		
		Size(String size, double d) {
			this.size = size;
			this.d = d;
		}
		
		public String toString() {
			return this.size;
		}
		
		public double toSize() {
			return this.d;
		}
	}
}
