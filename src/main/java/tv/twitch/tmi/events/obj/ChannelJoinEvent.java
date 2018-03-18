package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.RawData;

public class ChannelJoinEvent implements IEvent {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private Channel channel;
	@Getter private String username;
	@Getter private boolean self;
	
	public ChannelJoinEvent(TwitchTMI TMI, RawData rawData, Channel channel, String username, boolean self) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = channel;
		this.username = username;
		this.self = self;
	}
}
