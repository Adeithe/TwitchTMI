import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.obj.*;
import tv.twitch.tmi.events.EventListener;

public class IRCTest {
	public static void main(String[] args) {
		String username = System.getenv("TWITCH_USERNAME");
		String oauth = System.getenv("TWITCH_OAUTH");
		String channel = System.getenv("TWITCH_CHANNEL");
		
		TwitchTMI TMI = new TwitchTMI();
			TMI.setUsername(username);
			TMI.setOAuth(oauth);
			TMI.setAPI(System.getenv("CLIENT_ID"), System.getenv("CLIENT_SECRET"));
			TMI.setVerbose(true);
		TMI.connect();
		
		TMI.setEventListener(new EventListener() {
			public void onConnect(ConnectEvent event) throws Exception {
				System.out.println("Connected to "+ event.getIP() +":"+ event.getPort());
				System.out.println("	Username: "+ event.getUsername());
			}
			
			public void onReady() throws Exception {
				TMI.getChannel(channel).join();
			}
			
			public void onChannelJoin(ChannelEvent event) {
				if(event.isSelf()) {
					System.out.println("Joined Channel: "+ event.getChannel().getName());
					System.out.println("	Mod: "+ (event.getChannel().isMod()?"Yes":"No")); //This event.getChannel().isMod() is always false on join atm
				}
			}
			
			public void onChannelLeave(ChannelEvent event) {
				if(event.isSelf())
					System.out.println("Left Channel: "+ event.getChannel().getName());
			}
			
			public void onMessage(MessageEvent event) {
				if(!event.getType().equals(MessageEvent.Type.WHISPER)) {
					System.out.println("[#"+ event.getMessage().getChannel().getName() +"] "+ event.getSender().getDisplayName() + ": " + event.getMessage().getMessage());
					System.out.println("	Message Type: " + event.getType().toString());
					System.out.println("	BTTV Emotes: "+ event.getMessage().getBTTVEmotes().size());
					System.out.println("	FFZ Emotes: "+ event.getMessage().getFFZEmotes().size());
					System.out.println("	Total Emotes: "+ (event.getMessage().getEmotes().size() + event.getMessage().getBTTVEmotes().size() + event.getMessage().getFFZEmotes().size()));
				}
			}
			
			public void onWhisper(MessageEvent event) {
				System.out.println("[#jtv] "+ event.getSender().getDisplayName() + ": " + event.getMessage().getMessage());
				System.out.println("	Message Type: " + event.getType().toString());
				System.out.println("	BTTV Emotes: "+ event.getMessage().getBTTVEmotes().size());
				System.out.println("	FFZ Emotes: "+ event.getMessage().getFFZEmotes().size());
				System.out.println("	Total Emotes: "+ (event.getMessage().getEmotes().size() + event.getMessage().getBTTVEmotes().size() + event.getMessage().getFFZEmotes().size()));
			}
			
			public void onPing(PingEvent event) {
				System.out.println("Received ping from "+ event.getIP() +":"+ event.getPort());
			}
			
			public void onError(ErrorEvent event) {
				System.out.println("An error occurred: "+ event.getType().toString());
			}
		});
	}
}
