import tv.twitch.ClientBuilder;
import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.events.IListener;
import tv.twitch.handle.impl.events.tmi.channel.ChannelJoinEvent;
import tv.twitch.handle.impl.events.tmi.status.ReadyEvent;

import java.io.IOException;

public class IRCTest {
	public static void main(String[] args) throws Exception {
		ClientBuilder Builder = new ClientBuilder();
		Builder.withClientID(System.getenv("CLIENT_ID"));
		Builder.withClientSecret(System.getenv("CLIENT_SECRET"));
		Builder.withUsername(System.getenv("TWITCH_USERNAME"));
		Builder.withOAuth(System.getenv("TWITCH_OAUTH"));
		Builder.setVerbose(ClientSettings.VerboseLevel.ALL);
		
		TwitchClient Client = Builder.build();
		Client.getEventDispatcher().registerListener(new ReadyEventListener());
		Client.getEventDispatcher().registerListener(new ChannelJoinEventListener());
		Client.getTMI().login();
	}
	
	public static class ReadyEventListener implements IListener<ReadyEvent> {
		public void handle(ReadyEvent event) throws IOException {
			event.getClient().getTMI().getChannel(System.getenv("TWITCH_CHANNEL")).join();
		}
	}
	
	public static class ChannelJoinEventListener implements IListener<ChannelJoinEvent> {
		public void handle(ChannelJoinEvent event) throws IOException {
			System.out.println(event.getUsername() +" joined chat.");
		}
	}
}
