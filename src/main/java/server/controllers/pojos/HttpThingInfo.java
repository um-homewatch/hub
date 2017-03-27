package server.controllers.pojos;

import spark.QueryParamsMap;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpThingInfo extends ThingInfo {
  private final InetAddress address;
  private final Integer port;

  private HttpThingInfo(InetAddress address, Integer port, String subType) {
    super(subType);
    this.address = address;
    this.port = port;
  }

  public static HttpThingInfo fromQueryString(QueryParamsMap query) {
    try {
      QueryParamsMap address = query.get("address");
      QueryParamsMap port = query.get("port");
      QueryParamsMap subType = query.get("subType");

      if (!address.hasValue() || !subType.hasValue()) {
        throw new IllegalArgumentException("missing parameters. required params = address & subtype");
      } else {
        Integer portNumber = port.hasValue() ? port.integerValue() : null;

        return new HttpThingInfo(InetAddress.getByName(address.value()), portNumber, subType.value());
      }
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public InetAddress getAddress() {
    return address;
  }

  public Integer getPort() {
    return port;
  }
}
