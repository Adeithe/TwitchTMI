package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.events.IEvent;

public class ConnectEvent implements IEvent {
	@Getter private String IP;
	@Getter private int port;
	
	@Getter private String username;
	
	public ConnectEvent(String IP, int port, String username) {
		this.IP = IP;
		this.port = port;
		this.username = username;
	}
}
