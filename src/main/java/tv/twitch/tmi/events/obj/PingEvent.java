package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.RawData;

@Getter
public class PingEvent implements IEvent {
	private TwitchTMI TMI;
	private RawData rawData;
	
	private String IP;
	private int port;
	
	public PingEvent(TwitchTMI TMI, RawData rawData) {
		this.TMI = TMI;
		this.rawData = rawData;
		
		this.IP = this.TMI.getIP();
		this.port = this.TMI.getPort();
	}
}
