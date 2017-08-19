package homewatch.server.controllers;

import spark.QueryParamsMap;

import java.util.HashMap;
import java.util.Map;

class QueryStringUtils {
  public static Map<String, String> convertQueryMap(QueryParamsMap queryMap) {
    Map<String, String[]> mapStringArray = queryMap.toMap();
    Map<String, String> map = new HashMap<>();

    mapStringArray.forEach((k, v) -> map.put(k, v[0]));

    return map;
  }
}
