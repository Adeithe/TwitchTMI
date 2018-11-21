package tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class VideoPlayback {
	private int viewers;
	@SerializedName("server_time") private double serverTime;
	private String type;
}
