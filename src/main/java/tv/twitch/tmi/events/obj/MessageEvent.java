package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.RawData;

public class MessageEvent implements IEvent {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private String sender;
	@Getter private Message message;
	@Getter private Channel channel;
	
	public MessageEvent(TwitchTMI TMI, RawData rawData, String sender, Message.MessageType type) {
		this(TMI, rawData, new Message(TMI, rawData, sender, type));
	}
	
	public MessageEvent(TwitchTMI TMI, RawData rawData, Message message) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.sender = message.getSender();
		this.message = message;
		this.channel = this.message.getChannel();
	}
}
