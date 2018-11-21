package tv.twitch.tmi.pubsub;

import lombok.Getter;
import tv.twitch.tmi.handle.impl.obj.pubsub.packet.incoming.obj.Leaderboard;

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
	public static PubSub.Topic getBitsTopic(int channelId) { return getPubSubTopic(TopicInfo.BITS, channelId); }
	
	/**
	 * Anyone subscribes (first month), resubscribes (subsequent months), or gifts a subscription to a channel.
	 *
	 * Required Scope: channel_subscriptions
	 *
	 * @param channelId
	 * @return
	 */
	public static PubSub.Topic getChannelSubscriptionsTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_SUBSCRIPTIONS, channelId); }
	
	/**
	 * Anyone makes a purchase on a channel.
	 *
	 * @param channelId
	 * @return
	 */
	public static PubSub.Topic getCommerceTopic(int channelId) { return getPubSubTopic(TopicInfo.COMMERCE, channelId); }
	
	/**
	 * Anyone whispers the specified user.
	 *
	 * Required Scope: whispers:read
	 *
	 * @param userId
	 * @return
	 */
	public static PubSub.Topic getWhispersTopic(int userId) { return getPubSubTopic(TopicInfo.WHISPERS, userId); }
	
	public static PubSub.Topic getVideoPlaybackTopic(String username) { return getPubSubTopic(TopicInfo.VIDEO_PLAYBACK, username); }
	public static PubSub.Topic getVideoPlaybackTopic(int channelId) { return getPubSubTopic(TopicInfo.VIDEO_PLAYBACK_BY_ID, channelId); }
	public static PubSub.Topic getStreamChatroomTopic(int channelId) { return getPubSubTopic(TopicInfo.STREAM_CHATROOM, channelId); }
	public static PubSub.Topic getChannelSubscriptionGiftsTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_SUB_GIFTS, channelId); }
	public static PubSub.Topic getChatroomsUserTopic(int userId) { return getPubSubTopic(TopicInfo.CHATROOM_USER, userId); }
	public static PubSub.Topic getPublicChannelBitEventsTopic(int channelId) { return getPubSubTopic(TopicInfo.PUBLIC_CHANNEL_BIT_EVENTS, channelId); }
	public static PubSub.Topic getUserBitUpdatesTopic(int userId) { return getPubSubTopic(TopicInfo.USER_BIT_UPDATES, userId); }
	public static PubSub.Topic getUserSubscribeEventsTopic(int userId) { return getPubSubTopic(TopicInfo.USER_SUBSCRIBE_EVENTS, userId); }
	public static PubSub.Topic getUserPropertiesUpdateTopic(int userId) { return getPubSubTopic(TopicInfo.USER_PROPERTIES_UPDATE, userId); }
	public static PubSub.Topic getFollowsTopic(int userId) { return getPubSubTopic(TopicInfo.FOLLOWS, userId); }
	public static PubSub.Topic getChannelModerationActionsTopic(int userId, int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_MODERATOR_ACTIONS, userId, channelId); }
	public static PubSub.Topic getLeaderboardEventsTopic(int channelId, Leaderboard.TimeRange timeRange) { return getPubSubTopic(TopicInfo.LEADERBOARD_EVENTS, "bits-usage-by-channel-v1-"+ channelId +"-"+ timeRange.toString()); }
	public static PubSub.Topic getOnsiteNotifications(int userId) { return getPubSubTopic(TopicInfo.ONSITE_NOTIFICATIONS, userId); }
	public static PubSub.Topic getPresence(int userId) { return getPubSubTopic(TopicInfo.PRESENCE, userId); }
	
	private static PubSub.Topic getPubSubTopic(TopicInfo info, Object... args) {
		StringBuilder str = new StringBuilder().append(info.toString());
		for(Object obj : args)
			str.append("."+ obj.toString());
		return new PubSub.Topic(str.toString());
	}
	
	public enum TopicInfo {
		BITS("channel-bits-events-v1"),
		CHANNEL_SUBSCRIPTIONS("channel-subscribe-events-v1"),
		COMMERCE("channel-commerce-events-v1"),
		WHISPERS("whispers"),
		
		// Undocumented
		VIDEO_PLAYBACK("video-playback"),
		VIDEO_PLAYBACK_BY_ID("video-playback-by-id"),
		STREAM_CHATROOM("stream-chat-room-v1"),
		CHANNEL_SUB_GIFTS("channel-sub-gifts-v1"),
		CHATROOM_USER("chatrooms-user-v1"),
		PUBLIC_CHANNEL_BIT_EVENTS("channel-bit-events-public"),
		USER_BIT_UPDATES("user-bits-updates-v1"),
		USER_SUBSCRIBE_EVENTS("user-subscribe-events-v1"),
		USER_PROPERTIES_UPDATE("user-properties-update"),
		FOLLOWS("follows"),
		CHANNEL_MODERATOR_ACTIONS("chat_moderator_actions"),
		LEADERBOARD_EVENTS("leaderboard-events-v1"),
		ONSITE_NOTIFICATIONS("onsite-notifications"),
		PRESENCE("presence"),
		
		UNKNOWN(null);
		
		private String prefix;
		
		TopicInfo(String prefix) { this.prefix = prefix; }
		
		@Override
		public String toString() { return this.prefix; }
		
		public static TopicInfo find(String s) {
			for(TopicInfo topic : values())
				if(topic.toString().equalsIgnoreCase(s))
					return topic;
			return UNKNOWN;
		}
	}
}
