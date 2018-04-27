package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.obj.Channel;

@Getter
public class HostEvent {
	private TwitchTMI TMI;
	private Channel channel;
	private Channel hoster;
	private int count;
	private boolean auto;
	
	public HostEvent(TwitchTMI TMI, Channel channel, Channel hoster, int count, boolean auto) {
		this.TMI = TMI;
		this.channel = channel;
		this.hoster = hoster;
		this.count = count;
		this.auto = auto;
	}
}
