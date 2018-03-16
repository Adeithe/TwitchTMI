package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.RawData;

public class MessageEvent implements IEvent {
	private RawData rawData;
	
	@Getter private String sender;
	@Getter private Message message;
	@Getter private String channel;
	
	public MessageEvent(RawData rawData, String sender, Message.MessageType type) {
		this(rawData, new Message(rawData, sender, type));
	}
	
	public MessageEvent(RawData rawData, Message message) {
		this.rawData = rawData;
		
		this.sender = message.getSender();
		this.message = message;
		this.channel = this.message.getChannel();
	}
}
