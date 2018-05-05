package tv.twitch.api;

import lombok.Getter;

@Getter
public class Authorization {
	private Type type;
	private String token;
	
	public Authorization(Type type, String token) {
		this.type = type;
		this.token = token;
	}
	
	public String toString() {
		return this.getType().toString() +" "+ this.getToken();
	}
	
	public enum Type {
		BEARER("Bearer");
		
		String type;
		
		Type(String type) {
			this.type = type;
		}
		
		public String toString() {
			return this.type;
		}
	}
}
