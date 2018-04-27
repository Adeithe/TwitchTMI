package tv.twitch.api;

public enum Method {
	GET("GET"),
	POST("POST");
	
	String method;
	
	Method(String method) {
		this.method = method;
	}
	
	public String toString() {
		return this.method;
	}
}
