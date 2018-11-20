package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class OnsiteNotification {
	private Data data;
	private String type;
	
	@Getter
	public static class Data {
		private Notification notification;
		private Summary summary;
		private boolean persistent;
		private boolean toast;
		
		@Getter
		public static class Notification {
			private String id;
			@SerializedName("user_id") private String userId;
			private List<Action> actions;
			private String body;
			@SerializedName("body_md") private String bodyAsMarkdown;
			@SerializedName("thumbnail_url") private String thumbnailUrl;
			private List<Creator> creators;
			private List<Parameter> params;
			private boolean read;
			@SerializedName("created_at") private String createdAt;
			@SerializedName("updated_at") private String updatedAt;
			private String type;
			
			@Getter
			public static class Action {
				private String url;
				private String body;
				private String type;
			}
			
			@Getter
			public static class Creator {
				@SerializedName("user_id") private String userId;
				@SerializedName("user_name") private String username;
				@SerializedName("thumbnail_url") private String thumbnailUrl;
			}
			
			@Getter
			public static class Parameter {
				private String key;
				private String value;
			}
		}
		
		@Getter
		public static class Summary {
			@SerializedName("last_seen_at") private String lastSeenAt;
			@SerializedName("unseen_view_count") private int unseenCount;
		}
	}
}
