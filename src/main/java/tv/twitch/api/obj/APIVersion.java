package tv.twitch.api.obj;

import lombok.Getter;

@Getter
public enum APIVersion {
	KRAKEN("OAuth"),
	HELIX("Bearer");
	
	private String tokenPrefix;
	
	APIVersion(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}
}
