import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.tmi.events.IListener;
import tv.twitch.tmi.handle.impl.events.pubsub.ResponseEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.ModeratorActionEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.message.WhisperEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.DisconnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.PongEvent;
import tv.twitch.tmi.handle.impl.obj.Emote;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Leaderboard;
import tv.twitch.tmi.pubsub.PubSub;

import java.util.*;

public class PubSubTest {
	public static void main(String[] args) throws Exception {
		TwitchClient.Builder Builder = new TwitchClient.Builder();
				Builder.withUsername(System.getenv("TWITCH_USERNAME2"));
				Builder.withOAuth(System.getenv("TWITCH_OAUTH3"));
				Builder.setVerbose(ClientSettings.VerboseLevel.INCOMING);
		
		TwitchClient Client = Builder.build();
				Client.getEventDispatcher().registerListener(new PubSubConnectEventListener());
				Client.getEventDispatcher().registerListener(new PubSubPongEventListener());
				Client.getEventDispatcher().registerListener(new PubSubResponseEventListener());
				Client.getEventDispatcher().registerListener(new PubSubDisconnectEventListener());
				
				Client.getEventDispatcher().registerListener(new WhisperEventListener());
				Client.getEventDispatcher().registerListener(new ModeratorActionEventListener());
				
				Client.getPubSub().connect();
	}
	
	public static class WhisperEventListener implements IListener<WhisperEvent> {
		@Override
		public void handle(WhisperEvent event) {
			System.out.println(event.getWhisper().getBody());
			if(event.getWhisper().getTags().getEmotes().size() > 0) {
				System.out.println("	Emotes: ");
				for(Emote emote : event.getWhisper().getTags().getEmotes())
					System.out.println("		- " + emote.getCode(event.getWhisper()));
			}
		}
	}
	
	/**
	 * For obvious reasons, this will only work if you are a moderator for the channel
	 */
	public static class ModeratorActionEventListener implements IListener<ModeratorActionEvent> {
		@Override
		public void handle(ModeratorActionEvent event) {
			System.out.println(event.getAction().getIssuer() +" used command /"+ event.getAction().getCommand() +" "+ String.join(" ", event.getAction().getArgs()));
		}
	}
	
	public static class PubSubConnectEventListener implements IListener<ConnectEvent> {
		@Override
		public void handle(ConnectEvent event) {
			System.out.println("Connected!");
			List<String> nonces = new ArrayList<>();
				nonces.add(event.getPubSub().listen(
						PubSub.Topic.getOnsiteNotificationsTopic(128266588),
						PubSub.Topic.getPresenceTopic(128266588),
						PubSub.Topic.getFollowsTopic(128266588),
						PubSub.Topic.getStreamChangeTopic(128266588),
						PubSub.Topic.getWhispersTopic(128266588),
						PubSub.Topic.getUserCampaignEventsTopic(128266588),
						PubSub.Topic.getUserSubscribeEventsTopic(128266588),
						PubSub.Topic.getFriendshipTopic(128266588),
						PubSub.Topic.getUserBitUpdatesTopic(128266588),
						PubSub.Topic.getUserCommerceEventsTopic(128266588),
						PubSub.Topic.getChatroomsUserTopic(128266588),
						PubSub.Topic.getUserPropertiesUpdateTopic(128266588),
						PubSub.Topic.getUserExtensionPurchaseEventsTopic(128266588)
				));
				nonces.add(event.getPubSub().listen(
						PubSub.Topic.getVideoPlaybackTopic(26301881),
						PubSub.Topic.getBroadcastSettingsUpdateTopic(26301881),
						PubSub.Topic.getChannelEventUpdatesTopic(26301881),
						PubSub.Topic.getStreamChatroomTopic(26301881),
						PubSub.Topic.getExtensionControlTopic(26301881),
						PubSub.Topic.getChannelSquadUpdatesTopic(26301881),
						PubSub.Topic.getChannelSubscriptionGiftsTopic(26301881),
						PubSub.Topic.getRaidsTopic(26301881),
						PubSub.Topic.getExtensionControlTopic("uaw3vx1k0ttq74u9b2zfvt768eebh1"),
						PubSub.Topic.getChatroomsChannelTopic(26301881),
						PubSub.Topic.getChannelBitEventsTopic(26301881),
						PubSub.Topic.getChannelModerationActionsTopic(128266588, 26301881),
						PubSub.Topic.getBitsExtensionTransactionTopic(26301881, "uaw3vx1k0ttq74u9b2zfvt768eebh1"),
						PubSub.Topic.getBitsExtensionUserTransactionTopic(128266588, 26301881, "uaw3vx1k0ttq74u9b2zfvt768eebh1"),
						PubSub.Topic.getLeaderboardEventsTopic(26301881, Leaderboard.TimeRange.WEEKLY)
				));
				nonces.add(event.getPubSub().listen(PubSub.Topic.getBitsCampaignTopic()));
			System.out.println("Sent packet with nonce codes: ");
			for(String nonce : nonces)
				System.out.println("	- "+ nonce);
		}
	}
	
	/**
	 * Twitch requests that we add a random jitter to our PING requests. We can do this from inside the {@link PongEvent}
	 */
	public static class PubSubPongEventListener implements IListener<PongEvent> {
		@Override
		public void handle(PongEvent event) {
			int delay = (5 * (60 * 1000)) - (new Random().nextInt(30) * 1000);
			System.out.println("Recieved PONG! Next PING will be sent in "+ (delay / 1000) +" seconds.");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() { event.getPubSub().ping(); }
			}, delay);
		}
	}
	
	public static class PubSubResponseEventListener implements IListener<ResponseEvent> {
		@Override
		public void handle(ResponseEvent event) {
			System.out.println("Recieved response with nonce: "+ event.getNonce());
			if(event.hasError())
				System.out.println("	Error: "+ event.getError().toString());
		}
	}
	
	public static class PubSubDisconnectEventListener implements IListener<DisconnectEvent> {
		@Override
		public void handle(DisconnectEvent event) {
			System.out.println("Disconnected!");
		}
	}
}
