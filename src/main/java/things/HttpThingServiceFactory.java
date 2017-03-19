package things;

import com.sun.istack.internal.Nullable;
import exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public interface HttpThingServiceFactory<T> {
  HttpThingService<T> create(InetAddress address, @Nullable Integer port, String subtype) throws InvalidSubTypeException;

  boolean isSubType(String subtype);
}
