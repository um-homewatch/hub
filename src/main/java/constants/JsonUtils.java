package constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static final ObjectMapper OM = new ObjectMapper();

  public static ObjectMapper getOM() {
    return OM;
  }
}
