package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.utils.annotation.TypeDependant;

import java.util.List;

@Getter
public class Presence {
	private Data data;
	private String type;
	
	/**
	 * The data available in this class varies depending on the type set in {@link Presence}
	 */
	@Getter
	public static class Data {
		// Presence Update
		@TypeDependant("presence") private int index;
		@TypeDependant("presence") private String availability;
		@TypeDependant("presence") private List<Activity> activities;
		@TypeDependant("presence") private Activity activity;
		@TypeDependant("presence") @SerializedName("user_id") private int userId;
		@TypeDependant("presence") @SerializedName("user_login") private String username;
		@TypeDependant("presence") @SerializedName("updated_at") private long updatedAt;
		
		// Settings Update
		@TypeDependant("settings") @SerializedName("availability_override") private String availabilityOverride;
		@TypeDependant("settings") @SerializedName("is_invisible") private boolean invisible;
		@TypeDependant("settings") private String share;
		@TypeDependant("settings") @SerializedName("share_activity") private boolean shareActivity;
		
		@Getter
		public static class Activity {
			@SerializedName("channel_id") private String channelId;
			@SerializedName("channel_display_name") private String channelDisplayName;
			@SerializedName("channel_login") private String channelUsername;
			@SerializedName("game_id") private String gameId;
			private String game;
			@SerializedName("stream_id") private String streamId;
			private String type;
		}
	}
}
