package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

public class ChannelJoinEvent implements IEvent {
	private RawData rawData;
	
	@Getter private String channel;
	@Getter private String user;
	@Getter private boolean self;
	
	public ChannelJoinEvent(RawData rawData, String channel, String user, boolean self) {
		this.rawData = rawData;
		
		this.channel = channel;
		this.user = user;
		this.self = self;
	}
}
