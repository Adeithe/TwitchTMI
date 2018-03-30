import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.obj.*;
import tv.twitch.tmi.events.EventListener;
import tv.twitch.tmi.obj.Badge;
import tv.twitch.tmi.obj.Emote;

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
				TMI.getChannel(channel).join();
			}
			
			public void onChannelJoin(ChannelJoinEvent event) throws Exception {
				//TMI.sendMessage(channel, "/me test");
				System.out.println("JOIN - "+ event.getUsername());
			}
			
			public void onChannelLeave(ChannelLeaveEvent event) throws Exception {
				System.out.println("PART - "+ event.getUser());
			}
			
			public void onHost(HostEvent event) throws Exception {
				System.out.println("HOST - "+ event.getHoster().getName() +" is now hosting channel "+ event.getChannel().getName() +" for "+ event.getViewers() +" viewers!");
			}
			
			public void onWhisper(MessageEvent event) throws Exception {
				System.out.println("WHISPER - "+ event.getSender() +": "+ event.getMessage().getText());
			}
			
			public void onAction(MessageEvent event) {
				System.out.println("ACTION - "+ event.getSender() +": "+ event.getMessage().getText());
			}
			
			public void onMessage(MessageEvent event) {
				System.out.println("MESSAGE - "+ event.getSender() +": "+ event.getMessage().getText());
				System.out.println("	BADGES:");
				for(Badge b : event.getMessage().getUser().getBadges())
					System.out.println("		- "+ b.getType().toString() +" "+ b.getData());
				System.out.println("	EMOTES:");
				for(Emote e : event.getMessage().getEmotes())
					System.out.println("		- "+ e.getID() +" "+ e.getURL(Emote.Size.LARGE));
			}
			
			public void onCheer(CheerEvent event) {
				System.out.println("CHEER x"+ event.getAmount() +" - "+ event.getSender() +": "+ event.getMessage().getText());
			}
		});
	}
}
