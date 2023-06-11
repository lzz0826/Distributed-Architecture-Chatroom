package org.server.websocket.mpa;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import java.util.concurrent.ConcurrentHashMap;

public class WsChnIdCtxMap {
    /**
     * key:       value:
     * channelId, channel
     */
    private static final ConcurrentHashMap<ChannelId, Channel> map = new ConcurrentHashMap<>();

    public static void put(ChannelId chnId, Channel chn) {
        map.put(chnId, chn);
    }

    public static Channel get(ChannelId chnId) {
        if (!map.containsKey(chnId)) {
            return null;
        }

        return map.get(chnId);
    }

    public static void del(ChannelId chnId) {
        map.remove(chnId);
    }
}
