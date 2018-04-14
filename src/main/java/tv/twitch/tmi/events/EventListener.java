package tv.twitch.tmi.events;

import tv.twitch.tmi.events.obj.*;

public interface EventListener {
	default void onConnect(ConnectEvent event) throws Exception {}
	default void onChannelJoin(ChannelJoinEvent event) throws Exception {}
	default void onChannelLeave(ChannelLeaveEvent event) throws Exception {}
	
	default void onWhisper(MessageEvent event) throws Exception {}
	default void onMessage(MessageEvent event) throws Exception {}
	default void onAction(MessageEvent event) throws Exception {}
	default void onCheer(CheerEvent event) throws Exception {}
	
	default void onResub(SubEvent event) throws Exception {}
	default void onSub(SubEvent event) throws Exception {}
	default void onPrimeSub(SubEvent event) throws Exception {}
	default void onSubGift(SubEvent event) throws Exception {}
	
	default void onBan(BanEvent event) throws Exception {}
	default void onTimeout(BanEvent event) throws Exception {}
	
	default void onEmoteMode(ChannelModeEvent event) throws Exception {}
	default void onFollowMode(ChannelModeEvent event) throws Exception {}
	default void onR9KMode(ChannelModeEvent event) throws Exception {}
	default void onSlowMode(ChannelModeEvent event) throws Exception {}
	default void onSubMode(ChannelModeEvent event) throws Exception {}
	default void onChannelMode(ChannelModeEvent event) throws Exception {}
	
	default void onHost(HostEvent event) throws Exception {}
	default void onRaid(RaidEvent event) throws Exception {}
	
	default void onPing(PingEvent event) throws Exception {}
	
	default void onError(ErrorEvent event) throws Exception {}
}
