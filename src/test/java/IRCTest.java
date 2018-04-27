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
					System.out.println("	Mod: "+ (event.getChannel().isMod()?"Yes":"No")); //<-- event.getChannel().isMod() is ALWAYS false on join atm
				}
			}
			
			public void onChannelLeave(ChannelEvent event) {
				if(event.isSelf())
					System.out.println("Left Channel: "+ event.getChannel().getName());
			}
			
			public void onHost(HostEvent event) {
				System.out.println(event.getHoster().getName() +" hosted channel "+ event.getChannel().getName());
			}
			
			public void onUnhost(HostEvent event) {
				System.out.println(event.getHoster().getName() +" exited host mode");
			}
			
			public void onChatCleared(ClearChatEvent event) {
				System.out.println("A moderator cleared chat for channel "+ event.getChannel().getName());
			}
			
			public void onBan(BanEvent event) {
				if(!event.isTimeout())
					System.out.println("User "+ event.getUsername() +" has been banned from channel "+ event.getChannel().getName() +". Reason: "+ event.getReason());
			}
			
			public void onTimeout(BanEvent event) {
				System.out.println("User "+ event.getUsername() +" has been timed out for "+ event.getDuration() +" seconds in channel "+ event.getChannel().getName() +". Reason: "+ event.getReason());
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
