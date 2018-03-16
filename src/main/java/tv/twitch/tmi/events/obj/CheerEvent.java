package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.RawData;

public class CheerEvent implements IEvent {
	private RawData rawData;
	
	@Getter private String sender;
	@Getter private Message message;
	@Getter private String channel;
	@Getter private int amount;
	
	public CheerEvent(RawData rawData, Message message) {
		this.rawData = rawData;
		
		this.sender = message.getSender();
		this.message = message;
		this.channel = this.message.getChannel();
		this.amount = Integer.parseInt(this.rawData.getTags().getOrDefault("bits", "0"));
	}
}
