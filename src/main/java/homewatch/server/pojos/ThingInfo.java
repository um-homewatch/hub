package homewatch.server.pojos;

import spark.QueryParamsMap;

public class ThingInfo {
  private final String subType;

  ThingInfo(String subType) {
    this.subType = subType;
  }

  public static ThingInfo fromQueryString(QueryParamsMap query) {
    QueryParamsMap subType = query.get("subType");

    if (!subType.hasValue()) {
      throw new IllegalArgumentException("missing argument subtype");
    } else {
      return new ThingInfo(subType.value());
    }
  }

  public String getSubType() {
    return subType;
  }
}