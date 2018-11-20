package tv.twitch.tmi.handle.impl.obj.pubsub.packet;

import com.google.gson.annotations.SerializedName;
import lombok.Setter;
import tv.twitch.tmi.pubsub.PubSubTopic;

import java.util.ArrayList;
import java.util.List;

public class UnlistenPacket extends PubSubPacket {
	private Data data;
	
	public UnlistenPacket(PubSubTopic... topics) {
		super(Type.UNLISTEN);
		
		List<String> _topics = new ArrayList<>();
		for(PubSubTopic topic : topics)
			_topics.add(topic.toString());
		
		this.data = new Data();
		this.data.topics = _topics;
	}
	
	@Setter
	public static class Data {
		private List<String> topics;
		@SerializedName("auth_token") private String auth;
	}
}