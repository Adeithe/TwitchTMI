package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;

@Getter
public class ConnectEvent implements IEvent {
	private TwitchTMI TMI;
	
	private String IP;
	private int port;
	private String username;
	
	public ConnectEvent(TwitchTMI TMI) {
		this.TMI = TMI;
		
		this.IP = TMI.getIP();
		this.port = TMI.getPort();
		this.username = TMI.getUsername();
	}
}
