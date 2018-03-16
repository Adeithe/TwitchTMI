package tv.twitch.tmi.exception;

public class ChannelJoinFailureException extends Exception {
	public ChannelJoinFailureException() { super(); }
	public ChannelJoinFailureException(String message) { super(message); }
}
