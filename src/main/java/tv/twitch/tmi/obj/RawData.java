package tv.twitch.tmi.obj;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class RawData {
	public String data;
	public String prefix;
	public String command;
	public List<String> params;
	public HashMap<String, String> tags;
	
	public RawData(String rawData) {
		this.data = rawData;
		this.prefix = null;
		this.command = null;
		this.params = new ArrayList<String>();
		this.tags = new HashMap<String, String>();
	}
}