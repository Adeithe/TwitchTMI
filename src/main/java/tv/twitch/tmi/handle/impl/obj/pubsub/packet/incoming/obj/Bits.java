package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class Bits {
	@SerializedName("message_id") private String id;
	private Data data;
	@SerializedName("message_type") private String type;
	private String version;
	
	@Getter
	public static class Data {
		@SerializedName("user_id") private String userId;
		@SerializedName("user_name") private String username;
		@SerializedName("channel_id") private String channelId;
		@SerializedName("channel_name") private String channelName;
		@SerializedName("chat_message") private String message;
		@SerializedName("bits_used") private int bitsUsed;
		@SerializedName("total_bits_used") private int totalBitsUsed;
		@SerializedName("badge_entitlement") private BadgeEntitlement entitlement;
		private String context;
		private String time;
		
		@Getter
		public static class BadgeEntitlement {
			@SerializedName("new_version") private int now;
			@SerializedName("previous_version") private int last;
		}
	}
}
