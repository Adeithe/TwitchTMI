package tv.twitch.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.events.Event;
import tv.twitch.tmi.TwitchTMI;

@Getter
public class ConnectEvent extends Event {
	private TwitchTMI TMI;
	
	private String IP;
	private int port;
	
	public ConnectEvent(TwitchTMI TMI, String ip, int port) {
		this.TMI = TMI;
		
		this.IP = ip;
		this.port = port;
	}
}
