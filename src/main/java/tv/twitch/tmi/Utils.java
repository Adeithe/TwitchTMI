package tv.twitch.tmi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tv.twitch.api.Method;
import tv.twitch.api.TwitchAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Utils {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static boolean isNull(Object obj) {
		return (obj == null);
	}
	
	public static Object get(Object obj1, Object obj2) { return isNull(obj1)?obj2:obj1; }
	
	public static int extractNumber(String str) {
		return Integer.parseInt(str.replaceAll("[^0-9]", ""));
	}
	
	public static String username(String str) {
		String username = (isNull(str))? "" : str;
		return (username.charAt(0) == '#')? username.substring(1).toLowerCase() : username.toLowerCase();
	}
	
	public static String replaceAll(String str, HashMap<String, String> replacements) {
		if(str == null)
			return str;
		for(String key : replacements.keySet())
			str = str.replaceAll(key, replacements.getOrDefault("key", ""));
		return str;
	}
	
	
	public static String CallAPI(Method method, String url) throws IOException {
		StringBuilder response = new StringBuilder();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(method.toString());
			con.setRequestProperty("User-Agent", TwitchAPI.USER_AGENT);
		
		int response_code = con.getResponseCode();
		if(response_code == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while((line = br.readLine()) != null)
				response.append(line);
			br.close();
		}
		
		return response.toString();
	}
	
	public static int[] toIntArray(List<Integer> list) {
		int[] ints = new int[list.size()];
		for(int i = 0; i < list.size(); i++)
			ints[i] = list.get(i);
		return ints;
	}
}
