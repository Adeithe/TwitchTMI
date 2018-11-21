package com.frankerfacez.obj;

import com.frankerfacez.FrankerFaceZ;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.HashMap;

@Getter
public class EmoteResponse extends ResponseData {
	private FrankerFaceZ.Emote emote;
	
	@Getter
	public static class Emote {
		private int id;
		private String name;
		private String css;
		private int width;
		private int height;
		private boolean hidden;
		private Object margins;
		private boolean modifier;
		private Object offset;
		private FrankerFaceZ.Emote.Owner owner;
		@SerializedName("public") private boolean publicEmote;
		private HashMap<Integer, String> urls;
		@SerializedName("usage_count") private int usage;
		
		@Getter
		public static class Owner {
			private int _id;
			private String name;
			@SerializedName("display_name") private String displayName;
		}
	}
}
