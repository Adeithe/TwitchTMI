package tv.twitch.tmi.handle.impl.obj.irc;

import com.frankerfacez.FrankerFaceZ;
import lombok.Getter;
import net.betterttv.BetterTTV;
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
			
			if(tags.containsKey("emotes"))
				this.emotes = Parser.emotes(tags.get("emotes"), this.getText());
			
			for(BetterTTV.Emote emote : TMI.getGlobalBTTVEmotes()) {
				Matcher matcher = getEmoteMatcher(emote.getCode());
				if(matcher.find()) addEmote(emote, Emote.Type.BTTV);
			}
			for(FrankerFaceZ.Emote emote : TMI.getGlobalFFZEmotes()) {
				Matcher matcher = getEmoteMatcher(emote.getName());
				if(matcher.find()) addEmote(emote, Emote.Type.FFZ);
			}
			
			if(!getChannel().isBackendChannel()) {
				for(BetterTTV.Emote emote : getChannel().getBTTVEmotes()) {
					Matcher matcher = getEmoteMatcher(emote.getCode());
					if(matcher.find()) addEmote(emote, Emote.Type.BTTV);
				}
				for(FrankerFaceZ.Emote emote : getChannel().getFFZEmotes()) {
					Matcher matcher = getEmoteMatcher(emote.getName());
					if(matcher.find()) addEmote(emote, Emote.Type.FFZ);
				}
			}
		}
	}
	
	/**
	 * Deletes the message from chat.
	 */
	public void delete() {
		this.getChannel().sendMessage("/delete "+ this.getMessageID(), true);
	}
	
	private void addEmote(Object obj, Emote.Type type) {
		int pos = 0;
		List<Emote.Position> positions = new ArrayList<>();
		switch(type) {
			case BTTV:
				{
					BetterTTV.Emote emote = (BetterTTV.Emote) obj;
					for(String block : getText().split("(?<!\\w)("+ Pattern.quote(emote.getCode()) +")(?!\\w)")) {
						pos += block.length();
						positions.add(new Emote.Position(pos, pos + emote.getCode().length()));
						pos += emote.getCode().length();
					}
					if(positions.size() < 1) positions.add(new Emote.Position(0, emote.getCode().length()));
					this.getEmotes().add(new Emote(emote.getId(), positions.toArray(new Emote.Position[positions.size()]), type));
				}
			break;
			
			case FFZ:
				{
					FrankerFaceZ.Emote emote = (FrankerFaceZ.Emote) obj;
					for(String block : getText().split("(?<!\\w)("+ Pattern.quote(emote.getName()) +")(?!\\w)")) {
						pos += block.length();
						positions.add(new Emote.Position(pos, pos + emote.getName().length()));
						pos += emote.getName().length();
					}
					if(positions.size() < 1) positions.add(new Emote.Position(0, emote.getName().length()));
					this.getEmotes().add(new Emote(emote.getId(), positions.toArray(new Emote.Position[positions.size()]), type, emote.getUrls()));
				}
			break;
			
			default:
				break;
		}
	}
	
	private Matcher getEmoteMatcher(String code) {
		return Pattern.compile("(^|\\s)("+ Pattern.quote(code) +")($|\\s)").matcher(this.getText());
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
