package tv.twitch.tmi.handle.impl.events.irc.status;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.irc.TwitchTMI;

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
