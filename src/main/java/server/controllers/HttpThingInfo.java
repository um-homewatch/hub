package server.controllers;

import spark.QueryParamsMap;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpThingInfo {
  private InetAddress address;
  private Integer port;
  private String subType;

  private HttpThingInfo(InetAddress address, Integer port, String subType) {
    this.address = address;
    this.port = port;
    this.subType = subType;
  }

  public static HttpThingInfo fromQueryString(QueryParamsMap query) throws UnknownHostException {
    QueryParamsMap address = query.get("address");
    QueryParamsMap port = query.get("port");
    QueryParamsMap subType = query.get("subType");

    if (!address.hasValue() || !subType.hasValue()) {
      throw new IllegalArgumentException();
    } else {
      Integer portNumber = port.hasValue() ? port.integerValue() : null;
      return new HttpThingInfo(InetAddress.getByName(address.value()), portNumber, subType.value());
    }
  }

  public InetAddress getAddress() {
    return address;
  }

  public void setAddress(InetAddress address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }
}
