package server.controllers;

import spark.QueryParamsMap;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by joses on 13/03/2017.
 */
public class HttpThingInfo {
  private InetAddress address;
  private Integer port;
  private String subType;

  public HttpThingInfo(InetAddress address, Integer port, String subType) {
    this.address = address;
    this.port = port;
    this.subType = subType;
  }

  public static HttpThingInfo fromQueryString(QueryParamsMap query) throws UnknownHostException {
    QueryParamsMap address = query.get("address");
    QueryParamsMap port = query.get("port");
    QueryParamsMap subType = query.get("subType");

    if (!address.hasValue() || !subType.hasValue()){
      return null;
    }else{
      Integer portNumber = port.hasValue() ? port.integerValue() : null;
      return new HttpThingInfo(InetAddress.getByName(address.value()), port.integerValue(), subType.value());
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
