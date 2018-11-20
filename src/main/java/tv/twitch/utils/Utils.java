package tv.twitch.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class Utils {
	public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static boolean isNull(Object obj) { return (obj == null); }
	
	public static int extractNumber(String str) { return Integer.parseInt(str.replaceAll("[^0-9]", "")); }
	
	public static int[] toIntArray(List<Integer> list) {
		int[] ints = new int[list.size()];
		for(int i = 0; i < list.size(); i++)
			ints[i] = list.get(i);
		return ints;
	}
}
