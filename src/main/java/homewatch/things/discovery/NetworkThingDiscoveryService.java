package homewatch.things.discovery;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.Thing;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class NetworkThingDiscoveryService<T extends Thing> extends DiscoveryService<T> {
  private final CompletionService<NetworkThingService<T>> completionService;
  private final List<ThingService<T>> things;
  private Integer port = null;

  public NetworkThingDiscoveryService(ThingServiceFactory<T> thingServiceFactory, String subtype) throws InvalidSubTypeException {
    super(thingServiceFactory, subtype);
    ExecutorService executorService = Executors.newCachedThreadPool();
    this.completionService = new ExecutorCompletionService<>(executorService);
    this.things = new LinkedList<>();
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setAttributes(Map<String, ?> attributes) {
    String portString = this.getAttribute(attributes, "port", String.class);

    try {
      this.port = (portString == null) ? null : Integer.parseInt(portString);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("invalid port");
    }
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
      try {
        String hostname = InetAddress.getByName(address).getHostName();

        NetworkThingService<T> thingService = (NetworkThingService<T>) this.thingServiceFactory.createThingService(subtype);
        thingService.setAddress(hostname);
        thingService.setPort(this.port);

        boolean on = thingService.ping();

        return on ? thingService : null;
      } catch (Exception e) {
        return null;
      }
    });
  }
}
