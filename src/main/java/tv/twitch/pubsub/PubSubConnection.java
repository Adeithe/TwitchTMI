package tv.twitch.pubsub;

import lombok.Getter;

import java.net.URI;

public class PubSubConnection {
	public static final String IP = "pubsub-edge.twitch.tv";
	
	@Getter private PubSubManager pubSubManager;
	@Getter private int shardID;
	private ShardService shardService;
	
	@Getter private int connections = 0;
	
	/**
	 * @return True if this Shard can not take any more connections.
	 */
	public boolean hasMaxConnections() {
		return (this.getConnections() < 50);
	}
	
	/**
	 * @return If the {@link ShardService} is connected.
	 */
	public boolean isConnected() { return this.shardService.isOpen(); }
	
	/**
	 * Connects to the {@link ShardService}.
	 */
	public void connect() {
		if(!this.isConnected())
			this.shardService.connect();
	}
	
	/**
	 * Closes the {@link ShardService} connection.
	 */
	public void close() {
		if(this.isConnected())
			this.shardService.close();
	}
	
	PubSubConnection(PubSubManager pubSubManager, int id) {
		this.pubSubManager = pubSubManager;
		this.shardID = id;
		this.shardService = new ShardService(this, URI.create("wss://"+ IP));
		
		this.connect();
	}
}
