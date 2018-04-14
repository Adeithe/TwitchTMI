package tv.twitch.tmi.events.obj;

import lombok.Getter;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.obj.Channel;
import tv.twitch.tmi.obj.User;

public class SubEvent {
	private TwitchTMI TMI;
	
	@Getter private User user;
	@Getter private Channel channel;
	@Getter private boolean resub;
	@Getter private int streak;
	@Getter private Plan plan;
	
	public SubEvent(TwitchTMI TMI, User user, Channel channel, Plan plan) { this(TMI, user, channel, false, 1, plan); }
	
	public SubEvent(TwitchTMI TMI, User user, Channel channel, boolean resub, int streak, Plan plan) {
		this.TMI = TMI;
		
		this.user = user;
		this.channel = channel;
		this.resub = resub;
		this.streak = streak;
		this.plan = plan;
	}
	
	public boolean isPrimeSub() {
		return this.getPlan().equals(Plan.PRIME);
	}
	
	public enum Plan {
		SUB("Subscription"),
		PRIME("Prime");
		
		private String plan;
		
		Plan(String plan) {
			this.plan = plan;
		}
		
		public String toString() {
			return this.plan;
		}
	}
}
