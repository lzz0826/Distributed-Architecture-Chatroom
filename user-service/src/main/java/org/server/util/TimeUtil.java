package org.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {


  public static String unixTimestampFormat(long unixTimestamp) {

    // 创建一个SimpleDateFormat对象，指定日期时间格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 将Unix时间戳转换为Date对象
    Date date = new Date(unixTimestamp);

    // 使用SimpleDateFormat格式化Date对象为字符串
    String formattedDate = sdf.format(date);

    return formattedDate;
  }

}
