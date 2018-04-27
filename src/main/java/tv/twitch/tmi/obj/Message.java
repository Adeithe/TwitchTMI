package tv.twitch.tmi.obj;

import lombok.Getter;
import tv.twitch.tmi.Parser;
import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.obj.extension.BetterTTV;
import tv.twitch.tmi.obj.extension.FrankerFaceZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class Message {
	private TwitchTMI TMI;
	private Channel channel;
	private User sender;
	private String message;
	
	private List<Emote> emotes;
	private List<BetterTTV.Emote> BTTVEmotes;
	private List<FrankerFaceZ.Emote> FFZEmotes;
	
	public Message(TwitchTMI TMI, Channel channel, User sender, String message) { this(TMI, channel, sender, message, new HashMap<String, String>()); }
	public Message(TwitchTMI TMI, Channel channel, User sender, String message, HashMap<String, String> data) {
		this.TMI = TMI;
		this.channel = channel;
		this.sender = sender;
		this.message = message;
		
		if(data != null)
			this.emotes = Parser.emotes(data.getOrDefault("emotes", ""), message);
		
		this.BTTVEmotes = new ArrayList<BetterTTV.Emote>();
		this.FFZEmotes = new ArrayList<FrankerFaceZ.Emote>();
		
		this.emotify();
	}
	
	private void emotify() {
		String[] parts = this.getMessage().split(" ");
		for(String part : parts) {
			for(BetterTTV.Emote emote : this.getTMI().getGlobalBTTVEmotes())
				if(part.equals(emote.getCode()))
					this.getBTTVEmotes().add(emote);
			for(BetterTTV.Emote emote : this.getChannel().getBTTVEmotes())
				if(part.equals(emote.getCode()))
					this.getBTTVEmotes().add(emote);
			
			for(FrankerFaceZ.Emote emote : this.getTMI().getGlobalFFZEmotes())
				if(part.equals(emote.getName()))
					this.getFFZEmotes().add(emote);
			for(FrankerFaceZ.Emote emote : this.getChannel().getFFZEmotes())
				if(part.equals(emote.getName()))
					this.getFFZEmotes().add(emote);
		}
	}
}
