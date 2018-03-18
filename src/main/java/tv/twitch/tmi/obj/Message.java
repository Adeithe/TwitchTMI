package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;

public class Message {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private Channel channel;
	@Getter private String sender;
	@Getter private User user;
	@Getter private String text;
	@Getter private MessageType type;
	
	public Message(TwitchTMI TMI, RawData rawData, Channel channel, String sender, MessageType type) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = channel;
		this.sender = sender;
		this.user = new User(this.TMI);
		this.text = null;
		this.type = type;
		
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
		
		if(this.rawData.getParams().size() >= 2)
			this.text = this.rawData.getParams().get(1);
	}
}
