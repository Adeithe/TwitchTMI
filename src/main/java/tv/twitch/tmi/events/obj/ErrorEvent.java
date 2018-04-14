package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;
import tv.twitch.tmi.obj.Channel;

public class ErrorEvent implements IEvent {
	private TwitchTMI TMI;
	
	@Getter private Channel channel;
	@Getter private String message;
	@Getter private ErrorType error;
	
	public ErrorEvent(TwitchTMI TMI, Channel channel, String message, ErrorType error) {
		this.TMI = TMI;
		
		this.channel = channel;
		this.message = message;
		this.error = error;
	}
	
	public enum ErrorType {
		CHANNEL_SUSPENDED,
		BAN_FAILURE,
		TIMEOUT_FAILURE,
		UNRECOGNIZED_CMD
	}
}
