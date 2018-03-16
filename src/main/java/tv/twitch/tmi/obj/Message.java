package tv.twitch.tmi.obj;

import lombok.Getter;

public class Message {
	private RawData rawData;
	
	@Getter private String sender;
	@Getter private User user;
	@Getter private String channel;
	@Getter private String text;
	@Getter private MessageType type;
	
	public Message(RawData rawData, String sender, MessageType type) {
		this.rawData = rawData;
		
		this.sender = sender;
		this.user = new User();
		this.channel = null;
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
		
		if(this.rawData.getParams().size() >= 1)
			this.channel = this.rawData.getParams().get(0).replaceFirst("#", "");
		if(this.rawData.getParams().size() >= 2)
			this.text = this.rawData.getParams().get(1);
	}
}
