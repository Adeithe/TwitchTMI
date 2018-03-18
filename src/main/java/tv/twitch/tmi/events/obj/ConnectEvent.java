package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;

public class ConnectEvent implements IEvent {
	private TwitchTMI TMI;
	
	@Getter private String IP;
	@Getter private int port;
	@Getter private String username;
	
	public ConnectEvent(TwitchTMI TMI) {
		this.TMI = TMI;
		
		this.IP = TMI.getIP();
		this.port = TMI.getPort();
		this.username = TMI.getUsername();
	}
}
