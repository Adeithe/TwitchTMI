package net.betterttv;

import lombok.Getter;
import net.betterttv.obj.BTTVResponse;
import tv.twitch.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BetterTTV {
	public static final String BASE_URL = "https://api.betterttv.net/2/";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	public static BTTVResponse getGlobalEmotes() throws IOException { return Utils.GSON.fromJson(CallAPI("emotes"), BTTVResponse.class); }
	public static BTTVResponse getChannelEmotes(String channel) throws IOException { return Utils.GSON.fromJson(CallAPI("channels/"+ channel), BTTVResponse.class); }
	
	@Getter
	public static class Emote {
		private String channel;
		private String code;
		private String id;
		private Restrictions restrictions;
		private String imageType;
		
		@Getter
		public static class Restrictions {
			private List<String> channels;
			private List<String> games;
			private String emoticonSet;
		}
	}
	
	@SuppressWarnings("Duplicates")
	private static String CallAPI(String path) throws IOException {
		StringBuilder response = new StringBuilder();
		
		URL url = new URL(BASE_URL + path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", USER_AGENT);
		
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)?con.getInputStream():con.getErrorStream()));
		
		String line;
		while((line = br.readLine()) != null)
			response.append(line);
		br.close();
		
		return response.toString();
	}
}
