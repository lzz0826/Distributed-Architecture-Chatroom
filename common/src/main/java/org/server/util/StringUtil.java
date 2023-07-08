package org.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtil {

  static public String strListToString(List<String> strList){
    if (strList == null) {
      return "";
    }
    return String.join(",", strList);
  }

  static public List<String> stringToStrList(String str){
    if (str == null || str.isEmpty()) {
      return new ArrayList<>();
    }
    return Arrays.asList(str.split(","));
  }

  static public Set<String> stringToStrSet(String str){
    if (str == null || str.isEmpty()) {
      return new HashSet<>();
    }
    String[] array = str.split(",");
    return new HashSet<>(Arrays.asList(array));
  }

  static public String strSetToString(Set<String> set){
    if (set == null) {
      return "";
    }
    return String.join(",", set);
  }
}
