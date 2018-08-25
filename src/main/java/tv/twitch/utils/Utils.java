package tv.twitch.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static boolean isNull(Object obj) { return (obj == null); }
	
	public static int extractNumber(String str) { return Integer.parseInt(str.replaceAll("[^0-9]", "")); }
}
