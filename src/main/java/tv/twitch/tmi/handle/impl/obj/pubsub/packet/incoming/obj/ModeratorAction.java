package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ModeratorAction {
	private Action data;
	private String type;
	
	@Getter
	public static class Action {
		private String[] args;
		@SerializedName("created_by") private String issuer;
		@SerializedName("created_by_user_id") private String issuerId;
		@SerializedName("moderation_action") private String command;
		@SerializedName("msg_id") private String messageId;
		@SerializedName("target_user_id") private String targetId;
		private String type;
	}
}
