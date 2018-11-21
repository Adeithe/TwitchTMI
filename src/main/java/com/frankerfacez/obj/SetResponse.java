package com.frankerfacez.obj;

import com.frankerfacez.FrankerFaceZ;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.HashMap;
import java.util.List;

@Getter
public class SetResponse extends ResponseData {
	@SerializedName("default_sets") private List<Integer> defaults;
	private HashMap<Integer, FrankerFaceZ.Set> sets;
	private HashMap<Integer, List<String>> users;
	
	@Getter
	public static class Set {
		private int id;
		private String title;
		private String description;
		private List<FrankerFaceZ.Emote> emoticons;
		private String icon;
		private String css;
		private int _type;
	}
}
