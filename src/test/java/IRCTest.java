import tv.twitch.ClientBuilder;
import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.events.IListener;
import tv.twitch.handle.impl.events.ConnectEvent;

public class IRCTest {
	public static void main(String[] args) throws Exception {
		ClientBuilder Builder = new ClientBuilder();
		Builder.withClientID(System.getenv("CLIENT_ID"));
		Builder.withClientSecret(System.getenv("CLIENT_SECRET"));
		Builder.withUsername(System.getenv("TWITCH_USERNAME"));
		Builder.withOAuth(System.getenv("TWITCH_OAUTH"));
		Builder.setVerbose(ClientSettings.VerboseLevel.INCOMING);
		
		TwitchClient Client = Builder.build();
		Client.getEventDispatcher().registerListener(new ConnectEventListener());
		Client.getTMI().login();
	}
	
	public static class ConnectEventListener implements IListener<ConnectEvent> {
		public void handle(ConnectEvent event) {
			System.out.println("Connected to "+ event.getIP() +":"+ event.getPort());
		}
	}
}
