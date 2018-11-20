package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.utils.annotation.TypeDependant;

@Getter
public class StreamChatroom {
	private Data data;
	private String type;
	
	@Getter
	public static class Data {
		// Hosts
		@TypeDependant("host_target_change_v2") @SerializedName("channel_id") private String channelId;
		@TypeDependant("host_target_change_v2") @SerializedName("channel_login") private String channelUsername;
		@TypeDependant("host_target_change_v2") @SerializedName("num_viewers") private int viewers;
		@TypeDependant("host_target_change_v2") @SerializedName("previous_target_channel_id") private String previousTargetChannelId;
		@TypeDependant("host_target_change_v2") @SerializedName("target_channel_id") private String targetChannelId;
		@TypeDependant("host_target_change_v2") @SerializedName("target_channel_login") private String targetChannelUsername;
	}
}
