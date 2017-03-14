package things;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.net.InetAddress;

/**
 * Created by joses on 22/02/2017.
 */
public interface Thing {
  boolean ping();

  String getType();

  String getSubType();
}
