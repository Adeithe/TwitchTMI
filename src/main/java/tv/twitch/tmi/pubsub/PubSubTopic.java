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
	
	public static PubSub.Topic getBitsCampaignTopic() { return getPubSubTopic(TopicInfo.BITS_CAMPAIGNS, "updates"); }
	public static PubSub.Topic getBitsExtensionTransactionTopic(int channelId, String extensionId) { return getPubSubTopic(TopicInfo.BITS_EXT_TRANSACTION, channelId +"-"+ extensionId); }
	public static PubSub.Topic getBitsExtensionUserTransactionTopic(int userId, int channelId, String extensionId) { return getPubSubTopic(TopicInfo.BITS_EXT_USER_TRANSACTION, userId, channelId +"-"+ extensionId); }
	public static PubSub.Topic getBroadcastSettingsUpdateTopic(int channelId) { return getPubSubTopic(TopicInfo.BROADCAST_SETTING_UPDATE, channelId); }
	public static PubSub.Topic getChannelBitEventsTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_BIT_EVENTS, channelId); }
	public static PubSub.Topic getChannelEventUpdatesTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_EVENT_UPDATES, channelId); }
	public static PubSub.Topic getChannelModerationActionsTopic(int userId, int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_MODERATOR_ACTIONS, userId, channelId); }
	public static PubSub.Topic getChannelSubscriptionGiftsTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_SUB_GIFTS, channelId); }
	public static PubSub.Topic getChannelSquadUpdatesTopic(int channelId) { return getPubSubTopic(TopicInfo.CHANNEL_SQUAD_UPDATES, channelId); }
	public static PubSub.Topic getChatroomsChannelTopic(int channelId) { return getPubSubTopic(TopicInfo.CHATROOM_CHANNEL, channelId); }
	public static PubSub.Topic getChatroomsUserTopic(int userId) { return getPubSubTopic(TopicInfo.CHATROOM_USER, userId); }
	public static PubSub.Topic getExtensionControlTopic(int channelId) { return getPubSubTopic(TopicInfo.EXTENSION_CONTROL, channelId); }
	public static PubSub.Topic getExtensionControlTopic(String extensionId) { return getPubSubTopic(TopicInfo.EXTENSION_CONTROL, extensionId); }
	public static PubSub.Topic getFollowsTopic(int userId) { return getPubSubTopic(TopicInfo.FOLLOWS, userId); }
	public static PubSub.Topic getFriendshipTopic(int userId) { return getPubSubTopic(TopicInfo.FRIENDSHIP, userId); }
	public static PubSub.Topic getLeaderboardEventsTopic(int channelId, Leaderboard.TimeRange timeRange) { return getPubSubTopic(TopicInfo.LEADERBOARD_EVENTS, "bits-usage-by-channel-v1-"+ channelId +"-"+ timeRange.toString()); }
	public static PubSub.Topic getOnsiteNotificationsTopic(int userId) { return getPubSubTopic(TopicInfo.ONSITE_NOTIFICATIONS, userId); }
	public static PubSub.Topic getPresenceTopic(int userId) { return getPubSubTopic(TopicInfo.PRESENCE, userId); }
	public static PubSub.Topic getRaidsTopic(int channelId) { return getPubSubTopic(TopicInfo.RAIDS, channelId); }
	public static PubSub.Topic getStreamChangeTopic(int userId) { return getPubSubTopic(TopicInfo.STREAM_CHANGE, userId); }
	public static PubSub.Topic getStreamChatroomTopic(int channelId) { return getPubSubTopic(TopicInfo.STREAM_CHATROOM, channelId); }
	public static PubSub.Topic getUserBitUpdatesTopic(int userId) { return getPubSubTopic(TopicInfo.USER_BITS_UPDATES, userId); }
	public static PubSub.Topic getUserCampaignEventsTopic(int userId) { return getPubSubTopic(TopicInfo.USER_CAMPAIGN_EVENTS, userId); }
	public static PubSub.Topic getUserCommerceEventsTopic(int userId) { return getPubSubTopic(TopicInfo.USER_COMMERCE_EVENTS, userId); }
	public static PubSub.Topic getUserExtensionPurchaseEventsTopic(int userId) { return getPubSubTopic(TopicInfo.USER_EXTENSION_PURCHASE_EVENTS, userId); }
	public static PubSub.Topic getUserPropertiesUpdateTopic(int userId) { return getPubSubTopic(TopicInfo.USER_PROPERTIES_UPDATE, userId); }
	public static PubSub.Topic getUserSubscribeEventsTopic(int userId) { return getPubSubTopic(TopicInfo.USER_SUBSCRIBE_EVENTS, userId); }
	public static PubSub.Topic getVideoPlaybackTopic(String username) { return getPubSubTopic(TopicInfo.VIDEO_PLAYBACK, username); }
	public static PubSub.Topic getVideoPlaybackTopic(int channelId) { return getPubSubTopic(TopicInfo.VIDEO_PLAYBACK_BY_ID, channelId); }
	
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
		BITS_CAMPAIGNS("bits-campaigns-v1"),
		BITS_EXT_TRANSACTION("bits-ext-v1-transaction"),
		BITS_EXT_USER_TRANSACTION("bits-ext-v1-user-transaction"),
		BROADCAST_SETTING_UPDATE("broadcast-settings-update"),
		CHANNEL_BIT_EVENTS("channel-bit-events-public"),
		CHANNEL_EVENT_UPDATES("channel-event-updates"),
		CHANNEL_MODERATOR_ACTIONS("chat_moderator_actions"),
		CHANNEL_SUB_GIFTS("channel-sub-gifts-v1"),
		CHANNEL_SQUAD_UPDATES("channel-squad-updates"),
		CHATROOM_CHANNEL("chatrooms-channel-v1"),
		CHATROOM_USER("chatrooms-user-v1"),
		EXTENSION_CONTROL("extension-control"),
		FOLLOWS("follows"),
		FRIENDSHIP("friendship"),
		LEADERBOARD_EVENTS("leaderboard-events-v1"),
		ONSITE_NOTIFICATIONS("onsite-notifications"),
		PRESENCE("presence"),
		RAIDS("raid"),
		STREAM_CHANGE("stream-change-v1"),
		STREAM_CHATROOM("stream-chat-room-v1"),
		USER_BITS_UPDATES("user-bits-updates-v1"),
		USER_CAMPAIGN_EVENTS("user-campaign-events"),
		USER_COMMERCE_EVENTS("user-commerce-events"),
		USER_EXTENSION_PURCHASE_EVENTS("user-extensionpurchase-events"),
		USER_PROPERTIES_UPDATE("user-properties-update"),
		USER_SUBSCRIBE_EVENTS("user-subscribe-events-v1"),
		VIDEO_PLAYBACK("video-playback"),
		VIDEO_PLAYBACK_BY_ID("video-playback-by-id"),
		
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
