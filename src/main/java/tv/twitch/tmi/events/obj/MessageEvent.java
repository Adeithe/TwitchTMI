package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Message;
import tv.twitch.tmi.obj.User;

@Getter
public class MessageEvent implements IEvent {
	private TwitchTMI TMI;
	private User sender;
	private Message message;
	private Type type;
	private boolean self;
	
	public MessageEvent(TwitchTMI TMI, User sender, Message message, Type type) { this(TMI, sender, message, type, false); }
	public MessageEvent(TwitchTMI TMI, User sender, Message message, Type type, boolean self) {
		this.TMI = TMI;
		this.sender = sender;
		this.message = message;
		this.type = type;
		this.self = self;
	}
	
	public enum Type {
		CHEER,
		ACTION,
		CHAT,
		WHISPER
	}
}
