package org.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {


  static public String strListToString(List<String> strList){
    return String.join(",",strList);
  }


  static public List<String> stringToStrList(String str){
    List<String> list = Arrays.asList(str.split(","));
    return list;
  }

}
