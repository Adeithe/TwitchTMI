package tv.twitch.handle.impl.events.tmi.channel.message;

import tv.twitch.events.Event;
import tv.twitch.handle.impl.obj.tmi.Channel;
import tv.twitch.handle.impl.obj.tmi.Message;
import tv.twitch.handle.impl.obj.tmi.User;
import tv.twitch.tmi.TwitchTMI;

public class ChatEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	private User sender;
	private Message message;
	private boolean self;
	
	public ChatEvent(TwitchTMI TMI, Channel channel, User sender, Message message, boolean self) {
		this.TMI = TMI;
		this.channel = channel;
		this.sender = sender;
		this.message = message;
		this.self = self;
	}
}
