package com.frankerfacez.obj;

import com.frankerfacez.FrankerFaceZ;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.HashMap;

@Getter
public class RoomResponse extends ResponseData {
	private FrankerFaceZ.Room room;
	private HashMap<Integer, FrankerFaceZ.Set> sets;
	
	@Getter
	public static class Room {
		private int _id;
		@SerializedName("twitch_id") private int id;
		@SerializedName("id") private String username;
		@SerializedName("display_name") private String displayName;
		@SerializedName("is_group") private boolean group;
		@SerializedName("mod_urls") private HashMap<Integer, String> modUrls;
		@SerializedName("moderator_badge") private String moderatorBadge;
		private int set;
		private String css;
	}
}
