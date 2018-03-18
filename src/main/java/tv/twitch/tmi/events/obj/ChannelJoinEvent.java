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
	@Getter private String user;
	@Getter private boolean self;
	
	public ChannelJoinEvent(TwitchTMI TMI, RawData rawData, String channel, String user, boolean self) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = new Channel(TMI, channel);
		this.user = user;
		this.self = self;
	}
}
