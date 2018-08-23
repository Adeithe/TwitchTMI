package tv.twitch.pubsub.exceptions;

public class ShardNotFoundException extends Exception {
	public ShardNotFoundException() { super("Unable to find shard."); }
}
