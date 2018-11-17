package tv.twitch.api.obj;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.EnumSet;

public enum Scope {
	// New Twitch API
	ANALYTICS_READ_EXTENSIONS("analytics:read:extensions", Type.NEW_TWITCH_API),
	ANALYTICS_READ_GAMES("analytics:read:games", Type.NEW_TWITCH_API),
	BITS_READ("bits:read", Type.NEW_TWITCH_API),
	CLIPS_EDIT("clips:edit", Type.NEW_TWITCH_API),
	USER_EDIT("user:edit", Type.NEW_TWITCH_API),
	
	USER_EDIT_BROADCAST("user:edit:broadcast", Type.NEW_TWITCH_API),
	USER_READ_BROADCAST("user:read:broadcast", Type.NEW_TWITCH_API),
	USER_READ_EMAIL("user:read:email", Type.NEW_TWITCH_API),
	
	// Twitch API v5
	CHANNEL_CHECK_SUBSCRIPTION("channel_check_subscription", Type.TWITCH_API_V5),
	CHANNEL_COMMERCIAL("channel_commercial", Type.TWITCH_API_V5),
	CHANNEL_EDITOR("channel_editor", Type.TWITCH_API_V5),
	CHANNEL_FEED_EDIT("channel_feed_edit", Type.TWITCH_API_V5),
	CHANNEL_FEED_READ("channel_feed_read", Type.TWITCH_API_V5),
	CHANNEL_READ("channel_read", Type.TWITCH_API_V5),
	CHANNEL_STREAM("channel_stream", Type.TWITCH_API_V5),
	CHANNEL_SUBSCRIPTIONS("channel_subscriptions", Type.TWITCH_API_V5),
	/**
	 * Can not be requested by new clients.
	 * Consider using CHANNEL_MODERATE, CHAT_EDIT, CHAT_READ, WHISPERS_READ and/or WHISPERS_EDIT instead.
	 */
	@Deprecated CHAT_LOGIN("chat_login", Type.TWITCH_API_V5),
	COLLECTIONS_EDIT("collections_edit", Type.TWITCH_API_V5),
	COMMUNITIES_EDIT("communities_edit", Type.TWITCH_API_V5),
	COMMUNITIES_MODERATE("communities_moderate", Type.TWITCH_API_V5),
	OPENID("openid", Type.TWITCH_API_V5),
	USER_BLOCKS_READ("user_blocks_read", Type.TWITCH_API_V5),
	USER_FOLLOWS_EDIT("user_follows_edit", Type.TWITCH_API_V5),
	USER_READ("user_read", Type.TWITCH_API_V5),
	USER_SUBSCRIPTIONS("user_subscriptions", Type.TWITCH_API_V5),
	VIEWING_ACTIVITY_READ("viewing_activity_read", Type.TWITCH_API_V5),
	
	// Chat and PubSub
	CHANNEL_MODERATE("channel:moderate", Type.CHAT_AND_PUBSUB),
	CHAT_EDIT("chat:edit", Type.CHAT_AND_PUBSUB),
	CHAT_READ("chat:read", Type.CHAT_AND_PUBSUB),
	WHISPERS_READ("whispers:read", Type.CHAT_AND_PUBSUB),
	WHISPERS_EDIT("whispers:edit", Type.CHAT_AND_PUBSUB);
	
	public static String encode(EnumSet<Scope> scopes) { return encode(scopes.toArray(new Scope[scopes.size()])); }
	public static String encode(Scope... scopes) { try { return encode("UTF-8", scopes); } catch(UnsupportedEncodingException e) { return ""; } }
	public static String encode(String encoding, Scope... scopes) throws UnsupportedEncodingException {
		StringBuilder str = new StringBuilder();
		for(Scope scope : scopes)
			str.append(scope.toString());
		return URLEncoder.encode(str.toString(), encoding);
	}
	
	private String name;
	private Type type;
	
	Scope(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	@Deprecated
	public String getName() { return toString(); }
	
	public Type getType() { return this.type; }
	
	@Override
	public String toString() { return this.name; }
	
	public enum Type {
		NEW_TWITCH_API,
		TWITCH_API_V5,
		CHAT_AND_PUBSUB
	}
}
