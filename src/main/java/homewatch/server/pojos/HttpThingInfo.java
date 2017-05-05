package homewatch.server.pojos;

import spark.QueryParamsMap;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpThingInfo extends ThingInfo {
  private final InetAddress address;
  private final Integer port;

  private HttpThingInfo(InetAddress address, Integer port, String subtype) {
    super(subtype);
    this.address = address;
    this.port = port;
  }

  public static HttpThingInfo fromQueryString(QueryParamsMap query) {
    try {
      QueryParamsMap address = query.get("address");
      QueryParamsMap port = query.get("port");
      QueryParamsMap subtype = query.get("subtype");

      if (!address.hasValue() || !subtype.hasValue()) {
        throw new IllegalArgumentException("missing parameters. required params = address & subtype");
      } else {
        Integer portNumber = port.hasValue() ? port.integerValue() : null;

        return new HttpThingInfo(InetAddress.getByName(address.value()), portNumber, subtype.value());
      }
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public InetAddress getAddress() {
    return address;
  }

  public Integer getPort() {
    return port;
  }
}
