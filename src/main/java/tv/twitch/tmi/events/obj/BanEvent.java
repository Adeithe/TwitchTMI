package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.IEvent;

public class BanEvent implements IEvent {
	private TwitchTMI TMI;
	
	@Getter private String username;
	@Getter private String reason;
	@Getter private int duration;
	
	public BanEvent(TwitchTMI TMI, String username, String reason, int duration) {
		this.TMI = TMI;
		
		this.username = username;
		this.reason = reason;
		this.duration = duration;
	}
	
	@Deprecated
	public boolean isTemporary() {
		return this.isTimeout();
	}
	
	/**
	 * Returns true if the ban is temporary, false otherwise
	 *
	 * @return
	 */
	public boolean isTimeout() {
		return (this.duration < 0);
	}
}
