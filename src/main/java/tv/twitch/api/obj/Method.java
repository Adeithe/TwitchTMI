package tv.twitch.api.obj;

public enum Method {
	GET("GET"),
	POST("POST");
	
	private String name;
	
	Method(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
