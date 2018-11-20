package tv.twitch.tmi.handle.impl.events.irc.channel.message;

import lombok.Getter;
import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Message;
import tv.twitch.tmi.handle.impl.obj.irc.User;
import tv.twitch.tmi.irc.TwitchTMI;

@Getter
public class WhisperEvent extends Event {
	private TwitchTMI TMI;
	
	private User sender;
	private Message message;
	private boolean self;
	
	public WhisperEvent(TwitchTMI TMI, User sender, Message message, boolean self) {
		this.TMI = TMI;
		
		this.sender = sender;
		this.message = message;
		this.self = self;
	}
}
