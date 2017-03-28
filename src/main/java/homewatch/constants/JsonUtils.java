package homewatch.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static final ObjectMapper OM = new ObjectMapper();

  private JsonUtils() {
  }

  public static ObjectMapper getOM() {
    return OM;
  }
}
