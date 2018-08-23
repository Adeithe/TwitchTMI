package tv.twitch.pubsub;

import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import tv.twitch.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.handle.impl.events.pubsub.status.DisconnectEvent;

import java.net.URI;

@Getter
public class ShardService extends WebSocketClient {
	private PubSubConnection pubSubConnection;
	
	@Override
	public void onOpen(ServerHandshake handshake) {
		this.getPubSubConnection().getPubSubManager().getClient().getEventDispatcher().dispatch(new ConnectEvent(this.getPubSubConnection()));
	}
	
	@Override
	public void onMessage(String message) {
		System.out.println(message); //TODO: Handle message
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		this.getPubSubConnection().getPubSubManager().getClient().getEventDispatcher().dispatch(new DisconnectEvent(this.getPubSubConnection(), false));
	}
	
	@Override
	public void onError(Exception e) {
		e.printStackTrace(); //TODO: Handle errors
	}
	
	ShardService(PubSubConnection pubSubConnection, URI uri) {
		super(uri);
		this.pubSubConnection = pubSubConnection;
	}
}
