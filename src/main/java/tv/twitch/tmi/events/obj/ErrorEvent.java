package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;

@Getter
public class ErrorEvent implements IEvent {
	private TwitchTMI TMI;
	private String message;
	private ErrorType type;
	
	public ErrorEvent(TwitchTMI TMI, String message, ErrorType type) {
		this.TMI = TMI;
		this.message = message;
		this.type = type;
	}
	
	public enum ErrorType {
		AUTHENTICATION_FAILURE
	}
}
