package tv.twitch.tmi.handle.impl.obj.tmi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class RawData {
	private String data;
	private String prefix;
	private String command;
	private List<String> params;
	private HashMap<String, String> tags;
	
	private boolean parsed;
	
	public RawData(String rawData) {
		this.data = rawData;
		this.params = new ArrayList<>();
		this.tags = new HashMap<>();
	}
	
	/**
	 * Sets the prefix for the {@link RawData} object.
	 *
	 * @param prefix
	 * @return The current {@link RawData} object.
	 */
	public RawData setPrefix(String prefix) {
		if(!this.parsed)
			this.prefix = prefix;
		return this;
	}
	
	/**
	 * Sets the command for the {@link RawData} object.
	 *
	 * @param command
	 * @return The current {@link RawData} object.
	 */
	public RawData setCommand(String command) {
		if(!this.parsed)
			this.command = command;
		return this;
	}
	
	/**
	 * Sets the params for the {@link RawData} object.
	 *
	 * @param params
	 * @return The current {@link RawData} object.
	 */
	public RawData setParams(List<String> params) {
		if(!this.parsed)
			this.params = params;
		return this;
	}
	
	/**
	 * Sets the tags for the {@link RawData} object.
	 *
	 * @param tags
	 * @return The current {@link RawData} object.
	 */
	public RawData setTags(HashMap<String, String> tags) {
		if(!this.parsed)
			this.tags = tags;
		return this;
	}
	
	/**
	 * Finalizes the {@link RawData} object.
	 *
	 * @return Always returns true.
	 */
	public boolean isParsed() {
		if(!this.parsed)
			this.parsed = true;
		return this.parsed;
	}
}
