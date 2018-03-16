package tv.twitch.tmi.exception;

public class MissingUsernameException extends Exception {
	public MissingUsernameException() { super(); }
	public MissingUsernameException(String message) { super(message); }
}
