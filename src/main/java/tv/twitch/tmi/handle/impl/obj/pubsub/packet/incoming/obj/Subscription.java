package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.Emote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Subscription {
	@SerializedName("user_id") private String userId;
	@SerializedName("user_name") private String username;
	@SerializedName("display_name") private String displayName;
	@SerializedName("channel_name") private String channel;
	@SerializedName("channel_id") private String channelId;
	@SerializedName("sub_plan") private String subPlan;
	@SerializedName("sub_plan_name") private String subPlanName;
	private int months;
	private String context;
	@SerializedName("sub_message") private Message message;
	@SerializedName("recipient_id") private String recipientId;
	@SerializedName("recipient_user_name") private String recipientUserName;
	@SerializedName("recipient_display_name") private String recipientDisplayName;
	private String time;
	
	public static class Message {
		@Getter private String message;
		private EmoteData[] emotes;
		
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
