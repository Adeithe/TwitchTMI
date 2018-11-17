import tv.twitch.ClientSettings;
import tv.twitch.TwitchClient;
import tv.twitch.tmi.events.IListener;
import tv.twitch.tmi.handle.impl.events.irc.channel.ChannelJoinEvent;
import tv.twitch.tmi.handle.impl.events.irc.channel.message.MessageEvent;
import tv.twitch.tmi.handle.impl.events.irc.status.ReadyEvent;
import tv.twitch.tmi.handle.impl.obj.irc.Emote;

import java.io.IOException;
import java.util.List;

public class IRCTest {
	public static void main(String[] args) throws Exception {
		TwitchClient.Builder Builder = new TwitchClient.Builder();
				Builder.withUsername(System.getenv("TWITCH_USERNAME2"));
				Builder.withOAuth(System.getenv("TWITCH_OAUTH2"));
				Builder.setVerbose(ClientSettings.VerboseLevel.INCOMING);
		
		TwitchClient Client = Builder.build();
				Client.getEventDispatcher().registerListener(new ReadyEventListener());
				Client.getEventDispatcher().registerListener(new ChannelJoinEventListener());
				Client.getEventDispatcher().registerListener(new MessageEventListener());
				Client.getTMI().login();
	}
	
	public static class ReadyEventListener implements IListener<ReadyEvent> {
		public void handle(ReadyEvent event) throws IOException {
			event.getClient().getTMI().getChannel(System.getenv("TWITCH_CHANNEL")).join();
		}
	}
	
	public static class ChannelJoinEventListener implements IListener<ChannelJoinEvent> {
		public void handle(ChannelJoinEvent event) {
			System.out.println(event.getUsername() +" joined chat.");
			//event.getChannel().sendMessage("test", false);
		}
	}
	
	public static class MessageEventListener implements IListener<MessageEvent> {
		public void handle(MessageEvent event) {
			System.out.println("	Message: "+ event.getMessage().getText());
			
			List<Emote> emotes = event.getMessage().getEmotes();
			if(emotes.size() > 0) {
				System.out.println("	Emotes:");
				for(int i = 0; i < emotes.size(); i++) {
					System.out.println("		- " + emotes.get(i).getCode());
				}
			}
		}
	}
}
