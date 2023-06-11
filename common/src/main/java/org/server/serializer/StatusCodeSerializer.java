package org.server.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.server.common.StatusCode;

public class StatusCodeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = jsonSerializer.out;

        StatusCode statusCode = (StatusCode) object;
        out.write(String.valueOf(statusCode.code));
    }
}
