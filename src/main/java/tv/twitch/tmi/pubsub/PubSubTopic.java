package tv.twitch.tmi.pubsub;

import lombok.Getter;

/**
 * Please initialize the {@link PubSubTopic} using {@link PubSub.Topic}
 */
@Getter
public class PubSubTopic {
	private String topic;
	
	PubSubTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() { return this.topic; }
	
	/**
	 * Anyone cheers on a specified channel.
	 *
	 * @param channelId
	 * @return
	 */
	public static PubSubTopic getBitsTopic(int channelId) { return getPubSubTopic(TopicInfo.BITS, channelId); }
	
	/**
	 * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
	 *
	 * Required Scope: channel_subscriptions
	 *
	 * @param channelId
	 * @return
	 */
	public static PubSubTopic getChannelSubscriptionsTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_SUBSCRIPTIONS, channelId); }
	
	/**
	 * Anyone makes a purchase on a channel.
	 *
	 * @param channelId
	 * @return
	 */
	public static PubSubTopic getCommerceTopic(int channelId) { return getPubSubTopic(TopicInfo.COMMERCE, channelId); }
	
	/**
	 * Anyone whispers the specified user.
	 *
	 * Required Scope: whispers:read
	 *
	 * @param userId
	 * @return
	 */
	public static PubSubTopic getWhispersTopic(int userId) { return getPubSubTopic(TopicInfo.WHISPERS, userId); }
	
	public static PubSubTopic getChannelModerationActionsTopic(int userId, int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_MODERATOR_ACTIONS, userId, channelId); }
	
	private static PubSubTopic getPubSubTopic(TopicInfo info, Object... args) {
		StringBuilder str = new StringBuilder().append(info.toString());
		for(Object obj : args)
			str.append("."+ obj.toString());
		return new PubSubTopic(str.toString());
	}
	
	public enum TopicInfo {
		BITS("channel-bits-events-v1"),
		CHANNEL_SUBSCRIPTIONS("channel-subscribe-events-v1"),
		COMMERCE("channel-commerce-events-v1"),
		WHISPERS("whispers"),
		CHANNEL_MODERATOR_ACTIONS("chat_moderator_actions");
		
		private String prefix;
		private boolean authNeeded;
		
		TopicInfo(String prefix) { this.prefix = prefix; }
		
		@Override
		public String toString() { return this.prefix; }
	}
}
