package things;

import exceptions.InvalidSubTypeException;
import org.apache.commons.net.util.SubnetUtils;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryService<T> {
  private final CompletionService<HttpThingService<T>> completionService;
  private final List<HttpThingService<T>> things = new ArrayList<>();
  private final HttpThingServiceFactory<T> serviceFactory;
  private final String subtype;
  private Integer port = null;

  public DiscoveryService(HttpThingServiceFactory<T> serviceFactory, String subtype) throws InvalidSubTypeException {
    if (!serviceFactory.isSubType(subtype)) throw new InvalidSubTypeException();
    ExecutorService executorService = Executors.newCachedThreadPool();
    this.completionService = new ExecutorCompletionService<>(executorService);
    this.serviceFactory = serviceFactory;
    this.subtype = subtype;
  }

  public DiscoveryService(HttpThingServiceFactory<T> serviceFactory, String subtype, int port) throws InvalidSubTypeException {
    this(serviceFactory, subtype);
    this.port = port;
  }

  public List<HttpThingService<T>> discovery() {
    try {
      InetAddress localhost = InetAddress.getLocalHost();
      NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
      if (networkInterface == null)
        networkInterface = NetworkInterface.getByIndex(1);
      for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
        if (interfaceAddress.getAddress() instanceof Inet4Address) {
          String hostAddress = interfaceAddress.getAddress().getHostAddress();
          short subnetMask = interfaceAddress.getNetworkPrefixLength();
          SubnetUtils subnetUtils = new SubnetUtils(hostAddress + "/" + subnetMask);

          for (String address : subnetUtils.getInfo().getAllAddresses()) {
            pingAddress(address);
          }

          int numberOfTasks = subnetUtils.getInfo().getAllAddresses().length;

          for (int i = 0; i < numberOfTasks; i++) {
            Future<HttpThingService<T>> future = completionService.take();
            if (future.get() != null) {
              this.things.add(future.get());
            }
          }

        }
      }
    } catch (UnknownHostException | SocketException | IllegalAccessException | InvocationTargetException | InstantiationException | InterruptedException |
            ExecutionException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "An exception occurred", e);
    } catch (InvalidSubTypeException e) {
      e.printStackTrace();
    }
    return this.things;
  }

  private void pingAddress(String address) throws UnknownHostException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidSubTypeException {
    HttpThingService<T> thingService = this.serviceFactory.create(InetAddress.getByName(address), null, subtype);
    this.completionService.submit(() -> {
      if (thingService.ping()) {
        return thingService;
      } else
        return null;
    });
  }
}
