package org.server.util;

import com.alibaba.fastjson.serializer.SerializeConfig;
import org.server.common.StatusCode;
import org.server.serializer.*;

public class FastJsonUtil {

    public static SerializeConfig getCommonSerializeConfig() {
        SerializeConfig config = new SerializeConfig();
        config.put(StatusCode.class, new StatusCodeSerializer());

        return config;
    }

}
