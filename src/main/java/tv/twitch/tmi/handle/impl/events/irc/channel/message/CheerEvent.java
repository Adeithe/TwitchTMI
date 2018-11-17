package tv.twitch.tmi.handle.impl.events.irc.channel.message;

import tv.twitch.tmi.events.Event;
import tv.twitch.tmi.handle.impl.obj.irc.Channel;
import tv.twitch.tmi.handle.impl.obj.irc.Message;
import tv.twitch.tmi.handle.impl.obj.irc.User;
import tv.twitch.tmi.irc.TwitchTMI;

public class CheerEvent extends Event {
	private TwitchTMI TMI;
	private Channel channel;
	private User sender;
	private Message message;
	private int bits;
	private boolean self;
	
	public CheerEvent(TwitchTMI TMI, Channel channel, User sender, Message message, int bits, boolean self) {
		this.TMI = TMI;
		this.channel = channel;
		this.sender = sender;
		this.message = message;
		this.bits = bits;
		this.self = self;
	}
}
