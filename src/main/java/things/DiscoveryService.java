package things;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.net.util.SubnetUtils;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryService<T extends HttpThing> {
  private final CompletionService<T> completionService;
  private final List<T> things = new ArrayList<>();
  private final Class<T> thingType;
  private Integer port = null;

  public DiscoveryService(Class<T> thingType) {
    ExecutorService executorService = Executors.newCachedThreadPool();
    this.completionService = new ExecutorCompletionService<>(executorService);
    this.thingType = thingType;
  }

  public DiscoveryService(Class<T> thingType, int port) {
    this(thingType);
    this.port = port;
  }

  public List<T> discovery() {
    Unirest.setTimeouts(1000, 1000);
    try {
      InetAddress localHost = Inet4Address.getLocalHost();
      NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);


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
            Future<T> future = completionService.take();
            if (future.get() != null) {
              this.things.add(future.get());
            }
          }

        }
      }
    } catch (UnknownHostException | SocketException | IllegalAccessException | InvocationTargetException | InstantiationException | InterruptedException | ExecutionException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "An exception occurred", e);
    }
    return this.things;
  }

  private void pingAddress(String address) throws UnknownHostException, IllegalAccessException, InvocationTargetException, InstantiationException {
    T thing;
    if (port == null)
      thing = (T) thingType.getDeclaredConstructors()[0].newInstance(InetAddress.getByName(address));
    else
      thing = (T) thingType.getDeclaredConstructors()[1].newInstance(InetAddress.getByName(address), port);
    this.completionService.submit(() -> {
      if (thing.ping()) {
        return thing;
      } else
        return null;
    });
  }
}
