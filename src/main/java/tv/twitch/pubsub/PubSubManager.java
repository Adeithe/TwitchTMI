package tv.twitch.pubsub;

import lombok.Getter;
import tv.twitch.TwitchClient;
import tv.twitch.pubsub.exceptions.ShardNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PubSubManager {
	@Getter private TwitchClient client;
	
	private List<PubSubConnection> pubSubConnections = new ArrayList<>();
	
	public PubSubManager(TwitchClient client) {
		this.client = client;
	}
	
	/**
	 * Gets the current {@link PubSubConnection} Shard or creates a new one if needed.
	 *
	 * @return The current available {@link PubSubConnection}.
	 */
	public PubSubConnection getNextShard() {
		try {
			if(this.getShardCount() > 0)
				for(int i = 0; i < this.getShardCount(); i++) {
					PubSubConnection shard = this.getShard(i);
					if(!shard.hasMaxConnections())
						return shard;
				}
			if(!this.hasMaxShards())
				this.pubSubConnections.add(new PubSubConnection(this, this.getCurrentShardID()));
			return this.getShard(this.getCurrentShardID());
		} catch(ShardNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param shardID
	 * @return The {@link PubSubConnection} Shard by ID.
	 * @throws ShardNotFoundException
	 */
	public PubSubConnection getShard(int shardID) throws ShardNotFoundException {
		if(this.getShardCount() <= shardID)
			throw new ShardNotFoundException();
		return this.pubSubConnections.get(shardID);
	}
	
	/**
	 * @return The total number of PubSub connections.
	 */
	public int getTotalConnections() {
		int connections = 0;
		for(int i = 0; i < this.getShardCount(); i++)
			connections += this.pubSubConnections.get(i).getConnections();
		return connections;
	}
	
	/**
	 * Kill a currently living {@link PubSubConnection}.
	 *
	 * @param shardID
	 * @throws ShardNotFoundException
	 */
	public void kill(int shardID) throws ShardNotFoundException {
		for(int i = 0; i < this.getShardCount(); i++) {
			PubSubConnection shard = this.getShard(i);
			shard.close();
		}
	}
	
	/**
	 * @return The current ShardID.
	 */
	public int getCurrentShardID() {
		int shardID = this.getShardCount() - 1;
		if(shardID < 0)
			shardID = 0;
		return shardID;
	}
	
	/**
	 * @return The number of {@link PubSubConnection} Shards.
	 */
	public int getShardCount() { return this.pubSubConnections.size(); }
	
	/**
	 * @return If no more Shards should be created.
	 */
	public boolean hasMaxShards() { return !(this.getShardCount() < 10); }
}
