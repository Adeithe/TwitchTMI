package tv.twitch.handle.impl.events.tmi.status;

import lombok.Getter;
import tv.twitch.events.Event;

@Getter
public class ConnectEvent extends Event {
	private String IP;
	private int port;
	
	public ConnectEvent(String ip, int port) {
		this.IP = ip;
		this.port = port;
	}
}
