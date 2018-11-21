package com.frankerfacez.obj;

import com.frankerfacez.FrankerFaceZ;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.HashMap;
import java.util.List;

@Getter
public class UserResponse extends ResponseData {
	private HashMap<Integer, FrankerFaceZ.Badge> badges;
	private HashMap<Integer, FrankerFaceZ.Set> sets;
	private FrankerFaceZ.User user;
	
	@Getter
	public static class User {
		private int _id;
		@SerializedName("twitch_id") private int id;
		private String avatar;
		private List<Integer> badges;
		@SerializedName("name") private String username;
		@SerializedName("display_name") private String displayName;
		@SerializedName("emote_sets") private List<Integer> emoteSets;
		@SerializedName("is_donor") private boolean donor;
	}
}
