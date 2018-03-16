import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.obj.ChannelJoinEvent;
import tv.twitch.tmi.events.obj.CheerEvent;
import tv.twitch.tmi.events.obj.ConnectEvent;
import tv.twitch.tmi.events.EventListener;
import tv.twitch.tmi.events.obj.MessageEvent;

public class IRCTest {
	public static void main(String[] args) {
		String username = System.getenv("TWITCH_USERNAME");
		String oauth = System.getenv("TWITCH_OAUTH");
		String channel = System.getenv("TWITCH_CHANNEL");
		
		TwitchTMI TMI = new TwitchTMI();
			TMI.setUsername(username);
			TMI.setOAuth(oauth);
			TMI.setVerbose(true);
		TMI.connect();
		
		TMI.setEventListener(new EventListener() {
			public void onConnect(ConnectEvent event) throws Exception {
				TMI.join(channel);
			}
			
			public void onChannelJoin(ChannelJoinEvent event) throws Exception {
				TMI.sendMessage(channel, "/me test");
			}
			
			public void onAction(MessageEvent event) {
				System.out.println("ACTION - "+ event.getSender() +": "+ event.getMessage().getText());
			}
			
			public void onMessage(MessageEvent event) {
				System.out.println("MESSAGE - "+ event.getSender() +": "+ event.getMessage().getText());
			}
			
			public void onCheer(CheerEvent event) {
				System.out.println("CHEER "+ event.getAmount() +" - "+ event.getSender() +": "+ event.getMessage().getText());
			}
		});
	}
}
