import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.tmi.events.IListener;
import tv.twitch.tmi.handle.impl.events.pubsub.ResponseEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.channel.ModeratorActionEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.DisconnectEvent;
import tv.twitch.tmi.handle.impl.events.pubsub.status.PongEvent;
import tv.twitch.tmi.pubsub.PubSub;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PubSubTest {
	public static void main(String[] args) throws Exception {
		TwitchClient.Builder Builder = new TwitchClient.Builder();
				Builder.withUsername(System.getenv("TWITCH_USERNAME2"));
				Builder.withOAuth(System.getenv("TWITCH_OAUTH2"));
				Builder.setVerbose(ClientSettings.VerboseLevel.INCOMING);
		
		TwitchClient Client = Builder.build();
				Client.getEventDispatcher().registerListener(new PubSubConnectEventListener());
				Client.getEventDispatcher().registerListener(new PubSubPongEventListener());
				Client.getEventDispatcher().registerListener(new PubSubResponseEventListener());
				Client.getEventDispatcher().registerListener(new PubSubDisconnectEventListener());
				
				Client.getEventDispatcher().registerListener(new ModeratorActionEventListener());
				
				Client.getPubSub().connect();
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
			String nonce = event.getPubSub().listen(
					PubSub.Topic.getChannelModerationActionsTopic(128266588, 26301881)
			);
			System.out.println("Sent packet with nonce: "+ nonce);
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
