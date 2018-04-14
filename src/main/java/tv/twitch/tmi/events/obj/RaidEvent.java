package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.obj.Channel;

public class RaidEvent {
	private TwitchTMI TMI;
	
	@Getter private Channel channel;
	@Getter private String source;
	@Getter private int viewers;
	
	public RaidEvent(TwitchTMI TMI, Channel channel, String source, int viewers) {
		this.TMI = TMI;
		
		this.channel = channel;
		this.source = source;
		this.viewers = viewers;
	}
}
