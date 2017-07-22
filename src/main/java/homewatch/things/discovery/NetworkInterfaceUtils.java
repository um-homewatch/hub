package homewatch.things.discovery;

import org.apache.commons.net.util.SubnetUtils;

import java.net.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class NetworkInterfaceUtils {
  static List<String> getAddressesInNetwork() throws SocketException {
    List<String> addresses = new LinkedList<>();
    NetworkInterface networkInterface = getNetworkInterface();

    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
      addAddresses(interfaceAddress, addresses);
    }

    return addresses;
  }

  private static NetworkInterface getNetworkInterface() throws SocketException {
    return NetworkInterface.getByName("wlan0");
  }

  private static void addAddresses(InterfaceAddress interfaceAddress, List<String> addresses) {
    InetAddress address = interfaceAddress.getAddress();
    if (address instanceof Inet4Address) {
      String hostAddress = address.getHostAddress();
      short subnetMask = interfaceAddress.getNetworkPrefixLength();
      SubnetUtils subnetUtils = new SubnetUtils(hostAddress + "/" + subnetMask);

      Collections.addAll(addresses, subnetUtils.getInfo().getAllAddresses());
    }
  }
}
