import tv.twitch.ClientBuilder;
import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.events.IListener;
import tv.twitch.handle.impl.events.pubsub.status.ConnectEvent;
import tv.twitch.handle.impl.events.pubsub.status.DisconnectEvent;

public class PubSubTest {
	public static void main(String[] args) throws Exception {
		ClientBuilder Builder = new ClientBuilder();
		Builder.withClientID(System.getenv("CLIENT_ID"));
		Builder.withClientSecret(System.getenv("CLIENT_SECRET"));
		Builder.withUsername(System.getenv("TWITCH_USERNAME2"));
		Builder.withOAuth(System.getenv("TWITCH_OAUTH2"));
		Builder.setVerbose(ClientSettings.VerboseLevel.ALL);
		
		TwitchClient Client = Builder.build();
		Client.getEventDispatcher().registerListener(new PubSubConnectEventListener());
		Client.getEventDispatcher().registerListener(new PubSubDisconnectEventListener());
		Client.getPubSubManager().getNextShard();
	}
	
	public static class PubSubConnectEventListener implements IListener<ConnectEvent> {
		@Override
		public void handle(ConnectEvent event) {
			System.out.println("Connected to PubSub!");
		}
	}
	
	public static class PubSubDisconnectEventListener implements IListener<DisconnectEvent> {
		@Override
		public void handle(DisconnectEvent event) {
			System.out.println("Disconnected from PubSub!");
		}
	}
}
