package tv.twitch.api;

import lombok.Getter;
import tv.twitch.TwitchClient;
import tv.twitch.api.helix.API_Helix;
import tv.twitch.api.obj.Header;
import tv.twitch.api.obj.Method;
import tv.twitch.api.v5.API_v5;
import tv.twitch.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Getter
public class TwitchAPI {
	public static final String BASE_URL = "https://api.twitch.tv/";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	private TwitchClient client;
	private String bearerToken;
	
	private Versions versions;
	
	public TwitchAPI(TwitchClient client) { this(client, null); }
	public TwitchAPI(TwitchClient client, String bearerToken) {
		this.client = client;
		this.bearerToken = bearerToken;
		
		this.versions = new Versions(this);
	}
	
	public static class Versions {
		public API_Helix helix;
		
		@Deprecated public API_v5 v5;
		
		Versions(TwitchAPI API) {
			this.helix = new API_Helix(API);
			
			this.v5 = new API_v5(API);
		}
	}
	
	public String CallAPI(Method method, String path, List<Header> headers) throws APIException, IOException {
		StringBuilder response = new StringBuilder();
		
		URL url = new URL(BASE_URL + path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method.toString());
			con.setRequestProperty("User-Agent", USER_AGENT);
			for(Header header : headers)
				con.setRequestProperty(header.getName(), header.getValue());
		
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)?con.getInputStream():con.getErrorStream()));
		
		String line;
		while((line = br.readLine()) != null)
			response.append(line);
		br.close();
		
		if(con.getResponseCode() != 200)
			throw new APIException(response.toString());
		
		return response.toString();
	}
	
	@Getter
	public static class APIException extends Exception {
		private ErrorResponse error;
		
		APIException(String response) {
			super("API Error!");
			this.error = Utils.GSON.fromJson(response, ErrorResponse.class);
		}
		
		@Override
		public String toString() { return Utils.GSON.toJson(this.error); }
		
		@Getter
		public static class ErrorResponse {
			private int status;
			private String error;
			private String message;
			
			@Override
			public String toString() { return Utils.GSON.toJson(this); }
		}
	}
}
