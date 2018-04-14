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
	
	@Getter private String recipient;
	
	public SubEvent(TwitchTMI TMI, User user, Channel channel, Plan plan) { this(TMI, user, channel, false, 1, plan); }
	
	public SubEvent(TwitchTMI TMI, User user, Channel channel, boolean resub, int streak, Plan plan) { this(TMI, user, channel, null, resub, streak, plan); }
	public SubEvent(TwitchTMI TMI, User user, Channel channel, String recipient, boolean resub, int streak, Plan plan) {
		this.TMI = TMI;
		
		this.user = user;
		this.channel = channel;
		this.resub = resub;
		this.streak = streak;
		this.plan = plan;
		
		this.recipient = recipient;
	}
	
	public boolean isPrimeSub() {
		return this.getPlan().equals(Plan.PRIME);
	}
	
	public enum Plan {
		TIER_1("Tier 1"),
		TIER_2("Tier 2"),
		TIER_3("Tier 3"),
		PRIME("Prime"),
		GIFT("Gift"),
		UNKNOWN("Unknown");
		
		private String plan;
		
		Plan(String plan) {
			this.plan = plan;
		}
		
		public String toString() {
			return this.plan;
		}
	}
}
