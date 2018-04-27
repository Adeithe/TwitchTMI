package tv.twitch.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.v.APIVersions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TwitchAPI {
	public static final String BASE_URL = "https://api.twitch.tv/";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	@Getter private String id;
	@Getter private String secret;
	@Getter private APIVersions versions;
	@Getter private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public TwitchAPI(String clientID, String clientSecret) {
		this.id = clientID;
		this.secret = clientSecret;
		this.versions = new APIVersions(this);
	}
	
	public String CallAPI(Method method, String url) throws IOException {
		url = BASE_URL + url;
		
		StringBuilder response = new StringBuilder();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(method.toString());
			con.setRequestProperty("Client-ID", this.id);
			con.setRequestProperty("User-Agent", USER_AGENT);
		
		int response_code = con.getResponseCode();
		if(response_code != 200)
			return this.getGson().toJson(new ErrorResponse(response_code));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String line;
		while((line = br.readLine()) != null)
			response.append(line);
		br.close();
		
		return response.toString();
	}
	
	@Getter
	public static class ErrorResponse {
		@SerializedName("response_code") private int status;
		
		public ErrorResponse(int response_code) {
			this.status = response_code;
		}
	}
}
