package tv.twitch.tmi;

import java.util.HashMap;

public class Utils {
	public static boolean isNull(Object obj) {
		return (obj == null);
	}
	
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
			str = str.replaceAll(key, replacements.get("key"));
		return str;
	}
}
