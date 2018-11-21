package com.frankerfacez;

import com.frankerfacez.obj.*;
import tv.twitch.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FrankerFaceZ {
	public static final String BASE_URL = "https://api.frankerfacez.com/v1/";
	public static final String USER_AGENT = "Mozilla/5.0";
	
	// Badges
	public static BadgeResponse getBadge(String badge_name) throws IOException { return getBadge(badge_name, false); }
	public static BadgeResponse getBadge(int badge_id) throws IOException { return getBadge(badge_id, false); }
	public static BadgeResponse getBadge(int badge_id, boolean include_users) throws IOException { return Utils.GSON.fromJson(CallAPI((include_users?"":"_") +"badges/"+ badge_id), BadgeResponse.class); }
	public static BadgeResponse getBadge(String badge_name, boolean include_users) throws IOException { return Utils.GSON.fromJson(CallAPI((include_users?"":"_") +"badges/"+ badge_name), BadgeResponse.class); }
	
	public static BadgeResponse getBadges() throws IOException { return getBadges(false); }
	public static BadgeResponse getBadges(boolean include_users) throws IOException { return Utils.GSON.fromJson(CallAPI((include_users?"":"_") +"badges"), BadgeResponse.class); }
	
	// Emotes
	public static EmoteResponse getEmote(int emote_id) throws IOException { return Utils.GSON.fromJson(CallAPI("emote/"+ emote_id), EmoteResponse.class); }
	
	// Room
	public static RoomResponse getRoom(int channel_id) throws IOException { return getRoom(channel_id, false); }
	public static RoomResponse getRoom(int channel_id, boolean include_sets) throws IOException { return Utils.GSON.fromJson(CallAPI((include_sets?"":"_") +"room/id/"+ channel_id), RoomResponse.class); }
	public static RoomResponse getRoom(String channel) throws IOException { return getRoom(channel, false); }
	public static RoomResponse getRoom(String channel, boolean include_sets) throws IOException { return Utils.GSON.fromJson(CallAPI((include_sets?"":"_") +"room/"+ channel), RoomResponse.class); }
	
	// Sets
	public static SetResponse getDefaultSet() throws IOException { return Utils.GSON.fromJson(CallAPI("set/global"), SetResponse.class); }
	public static SetResponse getSet(int id) throws IOException { return Utils.GSON.fromJson(CallAPI("set/"+ id), SetResponse.class); }
	
	// Users
	public static UserResponse getUser(int user_id) throws IOException { return getUser(user_id, false); }
	public static UserResponse getUser(int user_id, boolean include_badges) throws IOException { return Utils.GSON.fromJson(CallAPI((include_badges?"":"_") +"user/id/"+ user_id), UserResponse.class); }
	public static UserResponse getUser(String username) throws IOException { return getUser(username, false); }
	public static UserResponse getUser(String username, boolean include_badges) throws IOException { return Utils.GSON.fromJson(CallAPI((include_badges?"":"_") +"user/"+ username), UserResponse.class); }
	
	public static class Badge extends BadgeResponse.Badge {}
	public static class Emote extends EmoteResponse.Emote {}
	public static class Room extends RoomResponse.Room {}
	public static class Set extends SetResponse.Set {}
	public static class User extends UserResponse.User {}
	
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
