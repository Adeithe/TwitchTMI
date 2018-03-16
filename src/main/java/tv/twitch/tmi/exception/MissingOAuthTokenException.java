package tv.twitch.tmi.exception;

public class MissingOAuthTokenException extends Exception {
	public MissingOAuthTokenException() { super(); }
	public MissingOAuthTokenException(String message) { super(message); }
}
