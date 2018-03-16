package tv.twitch.tmi.obj;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RawData {
	@Getter public String data;
	@Getter public String prefix;
	@Getter public String command;
	@Getter public List<String> params;
	@Getter public HashMap<String, String> tags;
	
	public RawData(String rawData) {
		this.data = rawData;
		this.prefix = null;
		this.command = null;
		this.params = new ArrayList<String>();
		this.tags = new HashMap<String, String>();
	}
}