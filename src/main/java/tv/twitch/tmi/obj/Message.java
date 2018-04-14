package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.Parser;
import tv.twitch.tmi.TwitchTMI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private String UID;
	@Getter private Channel channel;
	@Getter private String sender;
	@Getter private User user;
	@Getter private String text;
	@Getter private MessageType type;
	@Getter private List<Emote> emotes;
	
	public Message(TwitchTMI TMI, RawData rawData, Channel channel, String sender, MessageType type) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = channel;
		this.sender = sender;
		this.user = new User(this.TMI, this.getChannel());
		this.text = null;
		this.type = type;
		this.emotes = new ArrayList<Emote>();
		
		init();
	}
	
	
	public enum MessageType {
		WHISPER,
		MESSAGE,
		ACTION,
		CHEER
	}
	
	private void init() {
		for(String key : this.rawData.getTags().keySet()) {
			String val = this.rawData.getTags().get(key);
			this.user.setUserInfo(key, val);
		}
		
		this.UID = this.rawData.getTags().getOrDefault("id", "");
		this.emotes = Parser.emotes(this.rawData.getTags().getOrDefault("emotes", ""));
		
		if(this.rawData.getParams().size() >= 2)
			this.text = this.rawData.getParams().get(1);
		
		Matcher matcher = Pattern.compile("^\\u0001ACTION ([^\\u0001]+)\\u0001$").matcher(this.getText());
		if(matcher.find())
			this.text = matcher.group(1);
	}
}
