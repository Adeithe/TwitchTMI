package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SubscriptionGift {
	private Data data;
	private String type;
	
	@Getter
	public static class Data {
		@SerializedName("uuid") private String UUID;
		@SerializedName("channel_id") private String channelId;
		@SerializedName("user_id") private String userId;
		private int count;
		private String tier;
		private String type;
	}
}
