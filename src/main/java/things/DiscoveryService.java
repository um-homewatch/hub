package things;

import constants.LoggerUtils;
import exceptions.InvalidSubTypeException;
import org.apache.commons.net.util.SubnetUtils;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DiscoveryService<T> {
  private final CompletionService<HttpThingService<T>> completionService;
  private final List<HttpThingService<T>> things = new ArrayList<>();
  private final HttpThingServiceFactory<T> serviceFactory;
  private final String subtype;
  private Integer port = null;

  public DiscoveryService(HttpThingServiceFactory<T> serviceFactory, String subtype) throws InvalidSubTypeException {
    if (!serviceFactory.isSubType(subtype))
      throw new InvalidSubTypeException();
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
        networkInterface = NetworkInterface.getByName("wlp2s0");

      for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
        pingInterfaceAddresses(interfaceAddress);
      }
    } catch (UnknownHostException | SocketException | InterruptedException | ExecutionException | InvalidSubTypeException e) {
      LoggerUtils.logException(e);
    }
    return this.things;
  }

  private void pingInterfaceAddresses(InterfaceAddress interfaceAddress) throws UnknownHostException, InterruptedException, ExecutionException, InvalidSubTypeException {
    InetAddress address = interfaceAddress.getAddress();
    if (address instanceof Inet4Address) {
      String hostAddress = address.getHostAddress();
      short subnetMask = interfaceAddress.getNetworkPrefixLength();
      SubnetUtils subnetUtils = new SubnetUtils(hostAddress + "/" + subnetMask);

      for (String subnetAddress : subnetUtils.getInfo().getAllAddresses()) {
        pingAddress(subnetAddress);
      }

      addTasks(subnetUtils.getInfo().getAllAddresses().length);
    }
  }

  private void addTasks(int numberOfTasks) throws InterruptedException, ExecutionException {
    for (int i = 0; i < numberOfTasks; i++) {
      Future<HttpThingService<T>> future = completionService.take();
      if (future.get() != null) {
        this.things.add(future.get());
      }
    }
  }

  private void pingAddress(String address) throws UnknownHostException, InvalidSubTypeException {
    HttpThingService<T> thingService = this.serviceFactory.create(InetAddress.getByName(address), this.port, subtype);

    this.completionService.submit(() -> {
      if (thingService.ping()) {
        return thingService;
      } else
        return null;
    });
  }
}
