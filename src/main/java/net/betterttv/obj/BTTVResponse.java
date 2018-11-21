package net.betterttv.obj;

import lombok.Getter;
import net.betterttv.BetterTTV;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class BTTVResponse extends ResponseData {
	private int status;
	private String urlTemplate;
	private List<String> bots;
	private List<BetterTTV.Emote> emotes;
}
