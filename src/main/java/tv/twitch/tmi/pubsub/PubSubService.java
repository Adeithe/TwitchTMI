package tv.twitch.tmi.pubsub;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.events.pubsub.MessageEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.ResponseEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.*;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.message.WhisperEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.DisconnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.PongEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ReconnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.user.OnsiteNotificationEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.user.PresenceUpdateEvent;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.PubSubPacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.MessagePacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.ResponsePacket;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.*;
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
						case BITS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ChannelBitsEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Bits.class), packet.getData().getTopic()));
						break;
						
						case CHANNEL_SUBSCRIPTIONS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ChannelSubscriptionEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Subscription.class), packet.getData().getTopic()));
						break;
						
						case COMMERCE:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ChannelCommerceEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Commerce.class), packet.getData().getTopic()));
						break;
						
						case WHISPERS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new WhisperEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Whisper.class), packet.getData().getTopic()));
						break;
						
						// Undocumented
						case VIDEO_PLAYBACK:
						case VIDEO_PLAYBACK_BY_ID:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new VideoPlaybackEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), VideoPlayback.class), packet.getData().getTopic()));
						break;
						
						case STREAM_CHATROOM:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new StreamChatroomEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), StreamChatroom.class), packet.getData().getTopic()));
						break;
						
						case CHANNEL_SUB_GIFTS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ChannelSubscriptionGiftEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), SubscriptionGift.class), packet.getData().getTopic()));
						break;
						
						case CHATROOM_USER:
							// TODO: CHATROOM_USER
						break;
						
						case USER_BITS_UPDATES:
							// TODO: USER_BITS_UPDATES
						break;
						
						case USER_SUBSCRIBE_EVENTS:
							// TODO: USER_SUBSCRIBE_EVENTS
						break;
						
						case USER_PROPERTIES_UPDATE:
							// TODO: USER_PROPERTIES_UPDATE
						break;
						
						case FOLLOWS:
							// TODO: FOLLOWS
						break;
						
						case CHANNEL_MODERATOR_ACTIONS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new ModeratorActionEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), ModeratorAction.class), packet.getData().getTopic()));
						break;
						
						case LEADERBOARD_EVENTS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new LeaderboardUpdateEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Leaderboard.class), packet.getData().getTopic()));
						break;
						
						case ONSITE_NOTIFICATIONS:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new OnsiteNotificationEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), OnsiteNotification.class), packet.getData().getTopic()));
						break;
						
						case PRESENCE:
							this.getPubSub().getClient().getEventDispatcher().dispatch(new PresenceUpdateEvent(this.getPubSub(), Utils.GSON.fromJson(packet.getData().getMessage(), Presence.class), packet.getData().getTopic()));
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
