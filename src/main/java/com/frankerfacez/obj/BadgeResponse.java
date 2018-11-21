package com.frankerfacez.obj;

import com.frankerfacez.FrankerFaceZ;
import lombok.Getter;
import tv.twitch.api.obj.ResponseData;

import java.util.List;

@Getter
public class BadgeResponse extends ResponseData {
	private List<FrankerFaceZ.Badge> badges;
	private List<List<String>> users;
	
	@Getter
	public static class Badge {
		private int id;
		private String name;
		private int slot;
		private String title;
		private String replaces;
		private String image;
		private String color;
		private String css;
		private List<String> urls;
	}
}
