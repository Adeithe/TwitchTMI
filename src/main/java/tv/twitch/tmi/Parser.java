package tv.twitch.tmi;

import org.jetbrains.annotations.Nullable;
import tv.twitch.tmi.obj.RawData;

public class Parser {
	@Nullable
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
					key = key.replaceFirst("@", "");
				rawData.tags.put(key, tag.substring(tag.indexOf("=") + 1));
			}
			
			position = nextspace + 1;
		}
		
		while(Character.codePointAt(data, position) == 32)
			position++;
		
		if(Character.codePointAt(data, position) == 58) {
			nextspace = data.indexOf(" ", position);
			if(nextspace == -1)
				return null;
			
			rawData.prefix = data.substring(position + 1, nextspace);
			position = nextspace + 1;
			
			while(Character.codePointAt(data, position) == 32)
				position++;
		}
		
		nextspace = data.indexOf(" ", position);
		if(nextspace == -1) {
			if(data.length() > position) {
				rawData.command = data.substring(0, position);
				return rawData;
			}
			return null;
		}
		
		rawData.command = data.substring(position, nextspace);
		
		position = nextspace + 1;
		while(Character.codePointAt(data, position) == 32)
			position++;
		
		while(position < data.length()) {
			nextspace = data.indexOf(" ", position);
			
			if(Character.codePointAt(data, position) == 58) {
				rawData.params.add(data.substring(position + 1));
				break;
			}
			
			if(nextspace != -1) {
				rawData.params.add(data.substring(position, nextspace));
				position = nextspace + 1;
				while(Character.codePointAt(data, position) == 32)
					position++;
				continue;
			}
			
			if(nextspace == -1) {
				rawData.params.add(data.substring(position));
				break;
			}
		}
		return rawData;
	}
}
