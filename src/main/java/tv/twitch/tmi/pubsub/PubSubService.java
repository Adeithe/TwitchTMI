package tv.twitch.tmi.pubsub;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.events.pubsub.MessageEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.ResponseEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.ModeratorActionEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.DisconnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.PongEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ReconnectEvent;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.MessagePacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.ResponsePacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.ModeratorAction;
import tv.twitch.utils.Utils;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class PubSubService extends Thread {
	public static final String IP = "wss://pubsub-edge.twitch.tv";
	
	@Getter private PubSub pubSub;
	@Getter private Session session;
	
	@Getter private boolean connected;
	private boolean reconnect;
	
	PubSubService(PubSub pubSub) { this.pubSub = pubSub; }
	
	String send(PubSubPacket packet) {
		if(this.connected) {
			String message = Utils.GSON.toJson(packet).replaceAll("\\s\\s+", " ").trim();
			if(this.getPubSub().getClient().getSettings().getVerbose().getLevel() > 1)
				System.out.println("PubSub: < "+ message);
			this.session.getAsyncRemote().sendText(message);
			return packet.getNonce();
		}
		return null;
	}
	
	void disconnect() { this.disconnect(false); }
	void disconnect(boolean reconnect) {
		try {
			if(this.connected) {
				this.reconnect = reconnect;
				this.session.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() { this.onStart(); }
	
	private void onStart() {
		try {
			if(!this.connected) {
				WebSocketContainer webSocket = ContainerProvider.getWebSocketContainer();
				webSocket.connectToServer(this, new URI(IP));
				
				while(this.connected)
					Thread.sleep(250);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		this.connected = true;
		this.getPubSub().getClient().getEventDispatcher().dispatch(new ConnectEvent(this.getPubSub()));
		this.send(new PubSubPacket(PubSubPacket.Type.PING));
	}
	
	@OnMessage
	public void onMessage(String message) {
		message = message.replaceAll("\\s\\s+", " ").trim();
		if(this.getPubSub().getClient().getSettings().getVerbose().getLevel() > 0)
			System.out.println("PubSub: > "+ message);
		
		switch(Utils.GSON.fromJson(message, PubSubPacket.class).getType()) {
			case MESSAGE:
				{
					MessagePacket packet = Utils.GSON.fromJson(message, MessagePacket.class);
					switch(PubSubTopic.TopicInfo.find(packet.getData().getTopic().split("\\.")[0])) {
						case CHANNEL_MODERATOR_ACTIONS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ModeratorActionEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), ModeratorAction.class), packet.getData().getTopic()));
						break;
						
						default:
							System.out.println("Got an unhandled message! Topic: "+ packet.getData().getTopic());
					}
					this.getPubSub().getClient().getEventDispatcher().dispatch(new MessageEvent(this.getPubSub(), packet));
				}
			break;
			
			case RESPONSE:
				this.getPubSub().getClient().getEventDispatcher().dispatch(new ResponseEvent(this.getPubSub(), Utils.GSON.fromJson(message, ResponsePacket.class)));
			break;
			
			case PONG:
				this.getPubSub().getClient().getEventDispatcher().dispatch(new PongEvent(this.getPubSub()));
			break;
			
			case RECONNECT:
				this.disconnect(true);
			break;
			
			default: {
				System.out.println("Got an unhandled packet! Heres the raw data:");
				System.out.println("	"+ message);
			}
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		this.connected = false;
		this.session = null;
		this.getPubSub().getClient().getEventDispatcher().dispatch(new DisconnectEvent(this.getPubSub(), this.reconnect));
		if(this.reconnect) {
			this.getPubSub().getClient().getEventDispatcher().dispatch(new ReconnectEvent(this.getPubSub()));
			this.onStart();
		}
	}
	
	@OnError
	public void onError(Throwable e) {
		if(this.getPubSub().getClient().getSettings().getVerbose().getLevel() > 0)
			e.printStackTrace();
	}
}
