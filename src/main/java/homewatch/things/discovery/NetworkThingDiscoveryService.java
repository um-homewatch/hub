package homewatch.things.discovery;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.Thing;
import homewatch.things.ThingService;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


public class NetworkThingDiscoveryService<T extends Thing> implements DiscoveryService<T> {
  private final CompletionService<NetworkThingService<T>> completionService;
  private final List<ThingService<T>> things;
  private final NetworkThingServiceFactory<T> serviceFactory;
  private final String subtype;
  private Integer port = null;

  public NetworkThingDiscoveryService(NetworkThingServiceFactory<T> serviceFactory, String subtype) throws InvalidSubTypeException {
    if (!serviceFactory.isSubType(subtype))
      throw new InvalidSubTypeException();
    ExecutorService executorService = Executors.newCachedThreadPool();
    this.completionService = new ExecutorCompletionService<>(executorService);
    this.serviceFactory = serviceFactory;
    this.subtype = subtype;
    this.things = new LinkedList<>();
  }

  public NetworkThingDiscoveryService(NetworkThingServiceFactory<T> serviceFactory, String subtype, int port) throws InvalidSubTypeException {
    this(serviceFactory, subtype);
    this.port = port;
  }

  public List<ThingService<T>> perform() {
    try {
      List<String> addresses = NetworkInterfaceUtils.getAddressesInNetwork();

      for (String address : addresses) {
        pingAddress(address);
      }

      addTasks(addresses.size());
    } catch (SocketException | InterruptedException | ExecutionException e) {
      LoggerUtils.logException(e);
    }

    return this.things;
  }

  private void addTasks(int numberOfTasks) throws InterruptedException, ExecutionException {
    for (int i = 0; i < numberOfTasks; i++) {
      Future<NetworkThingService<T>> future = completionService.take();

      if (future.get() != null) this.things.add(future.get());
    }
  }

  private void pingAddress(String address) {
    this.completionService.submit(() -> {
      String hostname = InetAddress.getByName(address).getHostName();
      NetworkThingService<T> thingService = this.serviceFactory.create(hostname, this.port, subtype);

      boolean on = thingService.ping();

      return on ? thingService : null;
    });
  }
}
