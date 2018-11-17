package tv.twitch.api.obj;

import lombok.Getter;

@Getter
public class Header {
	private String name;
	private String value;
	
	public Header(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
