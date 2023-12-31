package org.server.withdraw.dto;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.server.withdraw.model.TraderResponseCode;

public class TraderResponseDto extends HashMap<String, Object> {
    private static final String KEY_CODE = "retCode";
    private static final String KEY_MESSAGE = "retMessage";

    private TraderResponseCode code;

    public static TraderResponseDto success() {
        return new TraderResponseDto(TraderResponseCode.SUCCESS);
    }

    public static TraderResponseDto success(Object data) {
        return success(data, null);
    }

    public static TraderResponseDto success(Object data, String requestKey) {
        TraderResponseDto dto = new TraderResponseDto(TraderResponseCode.SUCCESS);
        if (data != null) {
            dto.setData(data);
        }
        if (requestKey != null) {
            dto.sign(requestKey);
        }
        return dto;
    }

    public TraderResponseDto() {
    }

    public TraderResponseDto(TraderResponseCode code) {
        this.setCode(code);
    }

    public TraderResponseCode getCode() {
        return code;
    }

    public void setCode(TraderResponseCode code) {
        this.setCode(code, code.getMessage());
    }

    public void setCode(TraderResponseCode code, String message) {
        this.code = code;
        this.put(KEY_CODE, code.getCode());
        this.put(KEY_MESSAGE, message);
    }

    public void setData(Object data) {
        if (data == null) {
            return;
        }

        Class<?> cls = data.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(data);
                if (value == null) {
                    continue;
                }
                if (value instanceof String) {
                    if (((String) value).length() == 0) {
                        continue;
                    }
                }
                this.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void sign(String key) {
        if (key != null) {
            this.put(DtoSigner.KEY_SIGN, DtoSigner.signToString(this, key));
        }
    }
}
