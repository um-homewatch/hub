package homewatch.net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import homewatch.constants.JsonUtils;

public class JsonResponse {
  private static final ObjectMapper OM = JsonUtils.getOM();

  private final JsonNode json;
  private final int statusCode;

  public JsonResponse(JsonNode json, int statusCode) {
    this.statusCode = statusCode;
    this.json = json;
  }

  public JsonNode getJson() {
    return json;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
