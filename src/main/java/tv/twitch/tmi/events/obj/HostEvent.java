package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

public class HostEvent implements IEvent {
	private RawData rawData;
	
	@Getter private String channel;
	@Getter private String hoster;
	@Getter private int viewers;
	@Getter private boolean autoHost;
	
	public HostEvent(RawData rawData, String channel, String hoster, int viewers, boolean autoHost) {
		this.rawData = rawData;
		
		this.channel = channel;
		this.hoster = hoster;
		this.viewers = viewers;
		this.autoHost = autoHost;
	}
}
