package tv.twitch.utils;

public class Utils {
	public static boolean isNull(Object obj) { return (obj == null); }
	
	public static int extractNumber(String str) { return Integer.parseInt(str.replaceAll("[^0-9]", "")); }
}
