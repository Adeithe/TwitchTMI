package tv.twitch.tmi.handle.impl.events.irc.channel.message;

import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.handle.impl.obj.irc.Message;
import tv.twitch.tmi.handle.impl.obj.irc.User;
import tv.twitch.tmi.irc.TwitchTMI;

public class ActionEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	private User sender;
	private Message message;
	private boolean self;
	
	public ActionEvent(TwitchTMI TMI, Channel channel, User sender, Message message, boolean self) {
		this.TMI = TMI;
		this.channel = channel;
		this.sender = sender;
		this.message = message;
		this.self = self;
	}
}
