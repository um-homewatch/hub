package homewatch.things;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import org.apache.commons.net.util.SubnetUtils;

import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class DiscoveryService<T> {
  private final CompletionService<HttpThingService<T>> completionService;
  private final List<HttpThingService<T>> things;
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
    this.things = new LinkedList<>();
  }

  public DiscoveryService(HttpThingServiceFactory<T> serviceFactory, String subtype, int port) throws InvalidSubTypeException {
    this(serviceFactory, subtype);
    this.port = port;
  }

  public List<HttpThingService<T>> discovery() {
    try {
      List<String> addresses = getAddressList();

      for (String address : addresses) {
        pingAddress(address);
      }

      addTasks(addresses.size());
    } catch (UnknownHostException | SocketException | InterruptedException | ExecutionException | InvalidSubTypeException e) {
      LoggerUtils.logException(e);
    }

    return this.things;
  }

  private List<String> getAddressList() throws InterruptedException, ExecutionException, InvalidSubTypeException, UnknownHostException, SocketException {
    List<String> addresses = new LinkedList<>();
    NetworkInterface networkInterface = getNetworkInterface();

    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
      addAddresses(interfaceAddress, addresses);
    }

    return addresses;
  }

  private NetworkInterface getNetworkInterface() throws SocketException, UnknownHostException {
    InetAddress localhost = InetAddress.getLocalHost();

    return NetworkInterface.getByName("wlan0");
  }

  private void addAddresses(InterfaceAddress interfaceAddress, List<String> addresses) throws UnknownHostException, InterruptedException, ExecutionException, InvalidSubTypeException {
    InetAddress address = interfaceAddress.getAddress();
    if (address instanceof Inet4Address) {
      String hostAddress = address.getHostAddress();
      short subnetMask = interfaceAddress.getNetworkPrefixLength();
      SubnetUtils subnetUtils = new SubnetUtils(hostAddress + "/" + subnetMask);

      for (String subnetAddress : subnetUtils.getInfo().getAllAddresses()) {
        addresses.add(subnetAddress);
      }
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
    this.completionService.submit(() -> {
      HttpThingService<T> thingService = this.serviceFactory.create(InetAddress.getByName(address), this.port, subtype);

      boolean on = thingService.ping();
      System.out.println(on);
      if (on) {
        return thingService;
      } else
        return null;
    });
  }
}
