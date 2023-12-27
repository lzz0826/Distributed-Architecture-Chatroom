package org.server.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 *
 * 支付/代付sign工具類
 *
 */
public class SignUtil {

	/**
	 * 字段字典排序後拼接"&"符號字串 (最後會加上拼接"key"參數,可null)<br>
	 * 例:"a=1&b=2&c=3" + key
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSortSign(Map<String, Object> map, String key) {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String param = list.get(i);
			if (i == list.size() - 1) {
				sb.append(param + "=" + String.valueOf(map.get(param)));
			} else {
				sb.append(param + "=" + String.valueOf(map.get(param)) + "&");
			}
		}
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 字段添加序後拼接"&"符號字串 (最後會加上拼接"key"參數,可null)<br>
	 * 例:"a=1&b=2&c=3" + key
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSignWithoutSort(Map<String, Object> map, String key) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + String.valueOf(map.get(entry.getKey())) + "&");

		}
		sb.deleteCharAt(sb.length()-1);
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}
	
	/**
	 * 字段字典排序後拼接"&"符號字串 (最後會加上拼接"key"參數,可null)<br>
	 * 例:"a=1&b=2&c=3" + key
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSortSignByValueURLEncoder(Map<String, Object> map, String key) {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < list.size(); i++) {
				String param = list.get(i);
				if (i == list.size() - 1) {
					sb.append(param + "=" + java.net.URLEncoder.encode(String.valueOf(map.get(param)), "utf-8"));
				} else {
					sb.append(param + "=" + java.net.URLEncoder.encode(String.valueOf(map.get(param)), "utf-8") + "&");
				}
			}
			if (key != null) {
				// 需加上簽名密鑰
				sb.append(key);
			}
			return sb.toString();
		}catch (UnsupportedEncodingException e) {
			return "error";
		}
	}

	/**
	 * 字段字典排序<br>
	 * 例:"a=1&b=2&c=3" + key
	 *
	 * @param map            参与签名的參數
	 * @param keyValueSymbol key和value拼接的符號(例:"=")
	 * @param joinSymbol     key1和key2拼接的符號(例:"&")
	 * @param key            参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSortSign(Map<String, Object> map, String keyValueSymbol, String joinSymbol, String key) {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String param = list.get(i);
			if (i == list.size() - 1) {
				sb.append(param + keyValueSymbol + String.valueOf(map.get(param)));
			} else {
				sb.append(param + keyValueSymbol + String.valueOf(map.get(param)) + joinSymbol);
			}
		}
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 字段字典排序後拼接字串(最後會加上拼接"key"參數,可null)<br>
	 * 例:"a1b2c3" + key
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSortNoSymbolSign(Map<String, Object> map, String key) {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		list.forEach(e -> {
			sb.append(e + String.valueOf(map.get(e)));
		});
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 字段特定序後拼接字串(最後會加上拼接"key"參數,可null)<br>
	 * 例:"a|b|c|key"
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSortSign(LinkedHashMap<String, Object> map, String key, String joinSymbol) {
		StringBuilder sb = new StringBuilder();
		map.values().forEach(value ->{
			sb.append(value).append(joinSymbol);
		});
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 拼接字串(最後會加上拼接"key"參數,可null)<br>
	 * 例:"a1b2c3" + key
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeNoSymbolSign(Map<String, Object> map, String key) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			sb.append(it.getKey() + it.getValue());
		}
		if (key != null) {
			// 需加上簽名密鑰
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 拼接字串(不含key,不含密鑰)<br>
	 * 例: a=1,b=2,c=3 拼接结果"123"
	 *
	 * @param map 参与签名的參數
	 * @return
	 */
	public static String makeNoSymbolSign(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			sb.append(it.getValue());
		}
		return sb.toString();
	}

	/**
	 * 拼接"&"符號字串 (最後會加上拼接"key"參數,可null)
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSign(Map<String, Object> map, String key) {
		List<String> signParamList = new ArrayList<>();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			signParamList.add(it.getKey() + "=" + it.getValue());
		}
		String signStr = String.join("&", signParamList);
		return key != null ? signStr += key : signStr;
	}
	
	/**
	 * 拼接"&"符號字串 (最後會加上拼接"key"參數,可null)<br>
	 * 值使用URLEncoder
	 *
	 * @param map 参与签名的參數
	 * @param key 参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSignByValueURLEncoder(Map<String, Object> map, String key) {
		List<String> signParamList = new ArrayList<>();
		try {
			for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Object> it = iterator.next();
				signParamList.add(it.getKey() + "=" + java.net.URLEncoder.encode(String.valueOf(it.getValue()), "utf-8"));
			}
			String signStr = String.join("&", signParamList);
			return key != null ? signStr += key : signStr;
		}catch (UnsupportedEncodingException e) {
			return "error";
		}
	}

	/**
	 * 拼接"&"符號字串 (最後會加上拼接"key"參數,可null)
	 *
	 * @param map            参与签名的參數
	 * @param keyValueSymbol key和value拼接的符號(例:"=")
	 * @param joinSymbol     key1和key2拼接的符號(例:"&")
	 * @param key            参与签名的秘钥(例:"&key=m3ucpajqxshtzdm2jwwf")
	 * @return
	 */
	public static String makeSign(Map<String, Object> map, String keyValueSymbol, String joinSymbol, String key) {
		List<String> signParamList = new ArrayList<>();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			signParamList.add(it.getKey() + keyValueSymbol + it.getValue());
		}
		String signStr = String.join(joinSymbol, signParamList);
		return key != null ? signStr += key : signStr;
	}

	public static String makeSignNoKey(Map<String, Object> map, String keyValueSymbol, String joinSymbol, String key) {
		List<String> signParamList = new ArrayList<>();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			signParamList.add(keyValueSymbol + it.getValue());
		}
		String signStr = String.join(joinSymbol, signParamList);
		return key != null ? signStr += key : signStr;
	}

	public static String makeSignNoKey(Map<String, Object> map, String joinSymbol, String key) {
		List<String> signParamList = new ArrayList<>();
		for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			signParamList.add(String.valueOf(it.getValue()));
		}
		String signStr = String.join(joinSymbol, signParamList);
		return key != null ? signStr += key : signStr;
	}

	public static String makeSortSignNoKey(Map<String, Object> map, String joinSymbol, String key) {
		TreeMap<String, Object> sortMap = new TreeMap<String, Object>(map);
		List<String> signParamList = new ArrayList<>();
		for (Iterator<Entry<String, Object>> iterator = sortMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> it = iterator.next();
			signParamList.add(String.valueOf(it.getValue()));
		}
		String signStr = String.join(joinSymbol, signParamList);
		return key != null ? signStr += key : signStr;
	}


	public static String signHmacSHA256(String sign , String merchantKey){
		String key = merchantKey; // 雜湊用的密鑰字串，又稱為鹽(salt)
		String message = sign;
		try {
			byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8.name()); // 把密鑰字串轉為byte[]
			Key hmacKey = new SecretKeySpec(keyBytes, "HmacSHA256"); // 建立HMAC加密用密鑰
			Mac hmacSHA256 = Mac.getInstance("HmacSHA256"); // 取得SHA256 HMAC的Mac實例
			hmacSHA256.init(hmacKey); // 使用密鑰對Mac進行初始化
			byte [] macData = hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8.name())); // 對原始訊息進行雜湊計算
			String hexStringOfTheOriginMessage = Hex.encodeHexString(macData); //  使用Apache Commons Codec的Hex把雜湊計算的結果轉為Hex字串

			return hexStringOfTheOriginMessage;
		} catch (UnsupportedEncodingException |
				NoSuchAlgorithmException |
				InvalidKeyException e) {
			// 例外處理
		}
		return "";
	}

}
