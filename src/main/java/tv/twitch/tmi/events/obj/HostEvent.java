package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.RawData;

public class HostEvent implements IEvent {
	private TwitchTMI TMI;
	private RawData rawData;
	
	@Getter private Channel channel;
	@Getter private String hoster;
	@Getter private int viewers;
	@Getter private boolean autoHost;
	
	public HostEvent(TwitchTMI TMI, RawData rawData, String channel, String hoster, int viewers, boolean autoHost) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.channel = new Channel(TMI, channel);
		this.hoster = hoster;
		this.viewers = viewers;
		this.autoHost = autoHost;
	}
}
