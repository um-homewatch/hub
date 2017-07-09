package homewatch.server.controllers;

import spark.QueryParamsMap;

class ThingInfo {
  private final String subtype;

  private ThingInfo(String subtype) {
    this.subtype = subtype;
  }

  public static ThingInfo fromQueryString(QueryParamsMap query) {
    QueryParamsMap subtype = query.get("subtype");

    if (!subtype.hasValue()) {
      throw new IllegalArgumentException("missing argument subtype");
    } else {
      return new ThingInfo(subtype.value());
    }
  }

  public String getSubType() {
    return subtype;
  }
}