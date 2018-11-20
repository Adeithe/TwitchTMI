package tv.twitch.tmi.handle.impl.events.irc.channel;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class ChatClearEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	
	public ChatClearEvent(TwitchTMI TMI, Channel channel) {
		this.TMI = TMI;
		this.channel = channel;
	}
}
