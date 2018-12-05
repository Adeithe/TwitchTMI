package tv.twitch.tmi.handle.impl.obj.irc;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.Emote;
import tv.twitch.tmi.irc.TwitchTMI;
import tv.twitch.utils.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Message {
	private TwitchTMI TMI;
	private Channel channel;
	
	private String messageID;
	private List<Emote> emotes = new ArrayList<>();
	private User sender;
	private String text;
	private Type type;
	
	public Message(TwitchTMI TMI, Channel channel, User sender, String message, Type type) { this(TMI, channel, sender, message, type, null); }
	public Message(TwitchTMI TMI, Channel channel, User sender, String message, Type type, HashMap<String, String> tags) {
		this.TMI = TMI;
		this.channel = channel;
		this.sender = sender;
		this.text = message;
		this.type = type;
		
		if(type.equals(Type.ACTION)) {
			Matcher matcher = Pattern.compile("^\\u0001ACTION ([^\\u0001]+)\\u0001$").matcher(this.getText());
			while(matcher.find())
				this.text = matcher.group(1);
		}
		
		if(tags != null) {
			if(!tags.getOrDefault("id", "").isEmpty())
				this.messageID = tags.get("id");
			
			this.emotes = Parser.emotes(tags.getOrDefault("emotes", ""), this.getText());
		}
	}
	
	/**
	 * Deletes the message from chat.
	 */
	public void delete() {
		this.getChannel().sendMessage("/delete "+ this.getMessageID(), true);
	}
	
	public enum Type {
		CHAT("CHAT"),
		ACTION("ACTION"),
		CHEER("CHEER"),
		WHISPER("WHISPER");
		
		String name;
		Type(String name) { this.name = name; }
		
		public String getName() { return this.name; }
		
		@Override
		public String toString() { return this.name; }
	}
}
