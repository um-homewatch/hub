package server.controllers.pojos;

import spark.QueryParamsMap;

import java.net.UnknownHostException;

public class ThingInfo {
  private final String subType;

  ThingInfo(String subType) {
    this.subType = subType;
  }

  public static ThingInfo fromQueryString(QueryParamsMap query) throws UnknownHostException {
    QueryParamsMap subType = query.get("subType");

    if (!subType.hasValue()) {
      throw new IllegalArgumentException();
    } else {
      return new ThingInfo(subType.value());
    }
  }

  public String getSubType() {
    return subType;
  }
}
