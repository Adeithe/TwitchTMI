package tv.twitch.tmi.exception;

public class ChannelLeaveFailureException extends Exception {
	public ChannelLeaveFailureException() { super(); }
	public ChannelLeaveFailureException(String message) { super(message); }
}
