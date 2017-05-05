package homewatch.server.pojos;

import spark.QueryParamsMap;

public class ThingInfo {
  private final String subtype;

  ThingInfo(String subtype) {
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