package org.server.withdraw.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public abstract class DtoSigner {

    /**
     * 對應實現類的屬性名sign
     */
    public static final String KEY_SIGN = "sign";


    /**
     * 生成包含鍵值對的地圖的字符串表示形式，並使用指定的密鑰計算其MD5哈希。
     * 通過首字母的ASCII碼進行排序
     *
     * @param map 其中包含要簽名的鍵值對。
     * @param key 用於簽名的密鑰。
     * @return map的字符串表示形式的MD5哈希。
     */
    public static String signToString(Map<String, Object> map, String key) {
    	List<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            if (name.equals(KEY_SIGN)) {
                continue;
            }

            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof String && StringUtils.isBlank((String) value)) {
                continue;
            }

            String valueString;
            if (value instanceof Date) {
                valueString = String.valueOf(((Date) value).getTime());
            } else {
                valueString = value.toString();
            }
            list.add(name + "=" + valueString + "&");
        }

        String[] sorted = list.toArray(new String[0]);
        Arrays.sort(sorted, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (String str : sorted) {
            sb.append(str);
        }

        String result = sb.toString() + "key=" + key;

        log.info("signToString:{}", result);

        return DigestUtils.md5Hex(result).toUpperCase();
    }

    /**
     * 生成當前對象字段的字符串表示形式，並使用指定的密鑰計算其MD5哈希。
     * 通過首字母的ASCII碼進行排序
     * @param key 用於簽名的密鑰。
     * @return 對象字段字符串表示形式的MD5哈希。
     */
    protected String signToString(String key) {
    	Map<String, Object> map = new HashMap<>();
        forEachField(map::put);
        return signToString(map, key);
    }

    /**
     * 調用此方法可以將tdo生成簽名,通過將生成的簽名設置到 'sign' 字段來簽署當前對象。
     *
     * @param key 用於簽名的密鑰。
     * @throws NoSuchFieldException   如果在對象中找不到 'sign' 字段。
     * @throws IllegalAccessException 如果訪問 'sign' 字段時出現問題。
     */
    public void sign(String key) throws NoSuchFieldException, IllegalAccessException {
    	String sign = signToString(key);

        Field field = findSignField();
        field.setAccessible(true);
        field.set(this, sign);
    }

    /**
     * 將當前對象的字段轉換為查詢字符串。
     *
     * @param url 要附加查詢參數的URL。
     * @return 包含對象字段的查詢字符串。
     */
    public String toQueryString(String url) {
    	UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        forEachField(uriComponentsBuilder::queryParam);
        return uriComponentsBuilder.build().encode().toString();
    }


    /**
     * 定義一個接口，用於在字段迭代期間調用回調函數。
     */
    @FunctionalInterface
    private interface KeyValueCallback {
        void doWith(String key, String value);
    }

    /**
     * 遍歷對象字段，對每個字段調用回調函數。
     *
     * @param callback 用於處理鍵值對的回調函數。
     */
    private void forEachField(KeyValueCallback callback) {
    	Object object = this;
        ReflectionUtils.doWithFields(this.getClass(), field -> {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                return;
            }
            String name = field.getName();
            Object value = field.get(object);
            if (value != null) {
                if (value instanceof String && ((String) value).length() == 0) {
                    return;
                }
                String valueString;
                if (value instanceof Date) {
                    valueString = String.valueOf(((Date) value).getTime());
                } else {
                    valueString = value.toString();
                }
                callback.doWith(name, valueString);
            }
        });
    }

    /**
     * 驗證當前對象的簽名是否有效 依照首字母的ASCII碼進行排序。
     * 實現類物件必須要有值,且sign屬性必須先執行簽名過
     *
     * @param key 用於簽名的密鑰。(我方提供給對方的公鑰對方回傳,使用私鑰解密)
     * @return 如果簽名有效，則返回true；否則返回false。
     * @throws NoSuchFieldException   如果在對象中找不到 'sign' 字段。
     * @throws IllegalAccessException 如果訪問 'sign' 字段時出現問題。
     */
    public boolean verify(String key) throws IllegalAccessException, NoSuchFieldException {
    	String sign = signToString(key);
        Field field = findSignField();
        field.setAccessible(true);
        log.info("request sign: "+(String) field.get(this));
        log.info("our sign: "+sign);
        return sign.equalsIgnoreCase((String) field.get(this));
    }

    /**
     * 查找 'sign' 字段(屬性)並返回。
     * 使用 ReflectionUtils.findField 反射
     * @return 'sign' 字段。
     * @throws NoSuchFieldException 如果在對象中找不到 'sign' 字段。
     */
    private Field findSignField() throws NoSuchFieldException {
    	Field field = ReflectionUtils.findField(this.getClass(), KEY_SIGN);
        if (field == null) {
            throw new NoSuchFieldException(KEY_SIGN);
        }
        return field;
    }
}
