package tv.twitch.tmi.exception;

public class MessageSendFailureException extends Exception {
	public MessageSendFailureException() { super(); }
	public MessageSendFailureException(String message) { super(message); }
}
