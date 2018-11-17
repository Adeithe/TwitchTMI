package tv.twitch.tmi.handle.impl.obj.pubsub.packet;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PubSubPacket {
	Object data;
	String nonce;
	Type type;
	Error error;
	
	public PubSubPacket(Type type) { this(UUID.randomUUID().toString(), type); }
	public PubSubPacket(String nonce, Type type) {
		this.type = type;
		this.nonce = nonce.replaceAll("[^a-zA-Z0-9]", "");
	}
	
	public enum Type {
		// Client to Server
		LISTEN,
		UNLISTEN,
		PING,
		
		// Server to Client
		RESPONSE,
		MESSAGE,
		PONG,
		RECONNECT
	}
	
	public enum Error {
		ERR_BADMESSAGE,
		ERR_BADAUTH,
		ERR_SERVER,
		ERR_BADTOPIC
	}
}
