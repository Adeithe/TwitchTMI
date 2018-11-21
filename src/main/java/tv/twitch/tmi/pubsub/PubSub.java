package tv.twitch.tmi.pubsub;

import lombok.Getter;
import tv.twitch.TwitchClient;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.ListenPacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.UnlistenPacket;

import java.util.List;

public class PubSub {
	@Getter private TwitchClient client;
	private PubSubService service;
	
	public PubSub(TwitchClient client) { this.client = client; }
	
	/**
	 * Twitch requires that the PubSub sends a LISTEN request within 15 seconds of connecting to the socket.
	 *
	 * This can easily be done by registering a {@link tv.twitch.tmi.handle.impl.events.pubsub.status.ConnectEvent} listener.
	 */
	public void connect() {
		if(this.service == null)
			this.service = new PubSubService(this);
		if(!this.isConnected())
			this.service.start();
	}
	
	public String listen(List<PubSub.Topic> topics) { return listen(topics.toArray(new PubSub.Topic[topics.size()])); }
	public String listen(PubSub.Topic... topics) {
		String auth = getClient().getOAuth();
		if(auth.toLowerCase().startsWith("oauth:")) auth = auth.substring(6);
		
		return service.send(new ListenPacket(auth, topics));
	}
	
	public String unlisten(List<PubSub.Topic> topics) { return unlisten(topics.toArray(new PubSub.Topic[topics.size()])); }
	public String unlisten(PubSub.Topic... topics) { return service.send(new UnlistenPacket(topics)); }
	
	/**
	 * Twitch requires that the PubSub sends a PING request at least once every 5 minutes with a random jitter to prevent packet flooding.
	 *
	 * If the PubSub does not fire a {@link tv.twitch.tmi.handle.impl.events.pubsub.status.PongEvent} within 10 seconds of the ping, a reconnect is suggested.
	 */
	public void ping() {
		if(this.isConnected())
			this.service.send(new PubSubPacket(PubSubPacket.Type.PING));
	}
	
	public void disconnect() {
		if(this.isConnected())
			this.service.disconnect();
	}
	
	public boolean isConnected() { return (this.service != null && this.service.isConnected()); }
	
	public static class Topic extends PubSubTopic {
		Topic(String topic) { super(topic); }
	}
}
