package tv.twitch.utils;

import tv.twitch.handle.impl.obj.tmi.Badge;
import tv.twitch.handle.impl.obj.tmi.Emote;
import tv.twitch.handle.impl.obj.tmi.RawData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
	public static List<Badge> badges(String data) {
		List<Badge> badges = new ArrayList<>();
		if(data != null && !data.isEmpty()) {
			String[] explode = data.split(",");
			for(int i = 0; i < explode.length; i++) {
				String[] parts = explode[i].split("/");
				Badge.Type type = Badge.Type.UNKNOWN;
				try { type = Badge.Type.valueOf(parts[0].toUpperCase().replaceAll("-", "_")); } catch(Exception e) {}
				badges.add(new Badge(type, Integer.parseInt(parts[1])));
			}
		}
		return badges;
	}
	
	public static List<Emote> emotes(String data, String message) {
		List<Emote> emotes = new ArrayList<>();
		if(data != null && !data.isEmpty()) {
			String[] all = data.split("/");
			for(int i = 0; i < all.length; i++) {
				String[] parts = all[i].split(":");
				
				int id = Integer.parseInt(parts[0]);
				String[] positions = parts[1].split(",");
				
				int[] first_pos = Stream.of(positions[0].split("-")).mapToInt(Integer::parseInt).toArray();
				String code = message.substring(first_pos[0], first_pos[1]+1);
				
				emotes.add(new Emote(id, code, positions, Emote.Type.TWITCH));
			}
		}
		return emotes;
	}
	
	public static RawData msg(String data) {
		RawData rawData = new RawData(data);
		
		int position = 0;
		int nextspace = 0;
		if(Character.codePointAt(data, 0) == 64) {
			nextspace = data.indexOf(" ");
			if(nextspace == -1)
				return null;
			String[] rawTags = data.substring(0, nextspace).split(";");
			for(int i = 0; i < rawTags.length; i++) {
				String tag = rawTags[i];
				String[] pair = tag.split("=");
				String key = pair[0];
				if(key.startsWith("@"))
					key = key.substring(1);
				rawData.getTags().put(key, tag.substring(tag.indexOf("=") + 1));
			}
			position = nextspace + 1;
		}
		
		while(Character.codePointAt(data, position) == 32)
			position++;
		
		if(Character.codePointAt(data, position) == 58) {
			nextspace = data.indexOf(" ", position);
			if(nextspace == -1)
				return null;
			rawData.setPrefix(data.substring(position + 1, nextspace));
			position = nextspace + 1;
			while(Character.codePointAt(data, position) == 32)
				position++;
		}
		
		nextspace = data.indexOf(" ", position);
		if(nextspace == -1) {
			if(data.length() > position) {
				rawData.setCommand(data.substring(position));
				return rawData;
			}
			return null;
		}
		
		rawData.setCommand(data.substring(position, nextspace));
		
		position = nextspace + 1;
		while(Character.codePointAt(data, position) == 32)
			position++;
		
		while(position < data.length()) {
			nextspace = data.indexOf(" ", position);
			if(Character.codePointAt(data, position) == 58) {
				rawData.getParams().add(data.substring(position + 1));
				break;
			}
			
			if(nextspace != -1) {
				rawData.getParams().add(data.substring(position, nextspace));
				position = nextspace + 1;
				while(Character.codePointAt(data, position) == 32)
					position++;
				continue;
			}
			
			if(nextspace == -1) {
				rawData.getParams().add(data.substring(position));
				break;
			}
		}
		
		return rawData;
	}
}
