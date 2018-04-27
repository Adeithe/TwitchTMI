package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.obj.Channel;

@Getter
public class ClearChatEvent {
	private TwitchTMI TMI;
	private Channel channel;
	
	public ClearChatEvent(TwitchTMI TMI, Channel channel) {
		this.TMI = TMI;
		this.channel = channel;
	}
}