package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.Badge;
import tv.twitch.tmi.handle.impl.obj.Emote;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Whisper {
	@SerializedName("data_object") private Data data;
	private String type;
	
	@Getter
	public static class Data {
		private String body;
		@SerializedName("from_id") private int fromId;
		private int id;
		@SerializedName("message_id") private String messageId;
		private Recipient recipient;
		@SerializedName("sent_ts") private long timestamp;
		private Tags tags;
		@SerializedName("thread_id") private String threadId;
		
		public String getSenderName() { return this.getTags().getLogin(); }
		public String getSenderDisplayName() { return this.getTags().getDisplayName(); }
		public Color getSenderColor() { return Color.decode(this.getTags().getColorHex()); }
		public List<Badge> getSenderBadges() { return this.getTags().getBadges(); }
		
		public List<Emote> getEmotes() { return this.getTags().getEmotes(); }
	}
	
	@Getter
	public static class Recipient {
		private List<Badge> badges;
		private String color;
		private String username;
		@SerializedName("display_name") private String displayName;
		private int id;
		@SerializedName("profile_image") private String avatar;
		@SerializedName("user_type") private String type;
	}
	
	public static class Tags {
		@Getter private List<Badge> badges;
		private EmoteData[] emotes;
		@Getter @SerializedName("color") private String colorHex;
		@Getter private String login;
		@Getter @SerializedName("display_name") private String displayName;
		@Getter @SerializedName("user_type") private String type;
		
		@SuppressWarnings("Duplicates")
		public List<Emote> getEmotes() {
			HashMap<Integer, Emote> map = new HashMap<>();
			List<Emote.Position> positions = new ArrayList<>();
			for(EmoteData emote : this.emotes) {
				if(map.containsKey(emote.id))
					for(Emote.Position pos : map.get(emote.id).getPositions())
						positions.add(pos);
				positions.add(new Emote.Position(emote.start, (emote.end + 1)));
				map.put(emote.id, new Emote(emote.id, positions.toArray(new Emote.Position[positions.size()])));
				positions.clear();
			}
			return new ArrayList<>(map.values());
		}
		
		private class EmoteData {
			int id;
			int start;
			int end;
		}
	}
}
