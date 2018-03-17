package tv.twitch.tmi;

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
}
