package tv.twitch.tmi.events;

import tv.twitch.tmi.TwitchTMI;
import tv.twitch.tmi.events.obj.*;

public interface EventListener {
	default void onConnect(ConnectEvent event) throws Exception {}
	default void onReady() throws Exception {}
	
	default void onChannelJoin(ChannelEvent event) throws Exception {}
	default void onChannelLeave(ChannelEvent event) throws Exception {}
	
	default void onHost(HostEvent event) throws Exception {}
	
	default void onBan(BanEvent event) throws Exception {}
	default void onTimeout(BanEvent event) throws Exception {}
	
	default void onMessage(MessageEvent event) throws Exception {}
	default void onAction(MessageEvent event) throws Exception {}
	default void onCheer(MessageEvent event, int amount) throws Exception {}
	default void onWhisper(MessageEvent event) throws Exception {}
	
	default void onSubMode(ChannelModeEvent event) throws Exception {}
	default void onSubOnlyMode(ChannelModeEvent event) throws Exception {}
	default void onEmoteOnly(ChannelModeEvent event) throws Exception {}
	default void onEmoteOnlyMode(ChannelModeEvent event) throws Exception {}
	default void onSlowMode(ChannelModeEvent event) throws Exception {}
	default void onFollowersOnly(ChannelModeEvent event) throws Exception {}
	default void onFollowersOnlyMode(ChannelModeEvent event) throws Exception {}
	default void onR9KMode(ChannelModeEvent event) throws Exception {}
	default void onChannelMode(ChannelModeEvent event) throws Exception {}
	
	default void onPing(PingEvent event) throws Exception {}
	default void onError(ErrorEvent event) throws Exception {}
	
	/**
	 * This event is used internally.
	 * It can be overwritten, however, doing so isn't recommended.
	 *
	 * @param TMI
	 * @throws Exception
	 */
	default void onReconnectRequest(TwitchTMI TMI) throws Exception {
		if(!TMI.isConnected())
			TMI.connect();
	}
}
