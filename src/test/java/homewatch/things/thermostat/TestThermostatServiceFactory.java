package homewatch.things.thermostat;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestThermostatServiceFactory {
  private static final ThermostatServiceFactory serviceFactory = new ThermostatServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    InetAddress addr = InetAddress.getByName("192.168.1.50");
    HttpThingService<Thermostat> thermostatService = serviceFactory.create(addr, 80, "rest");

    assertThat(thermostatService.getAddress(), is(addr));
    assertThat(thermostatService.getPort(), is(80));
    assertTrue(thermostatService instanceof RestThermostatService);
    assertThat(thermostatService.getType(), is("Things::Thermostat"));
    assertThat(thermostatService.getSubtype(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Thermostat> thermostatService = serviceFactory.create("rest");

    assertTrue(thermostatService instanceof RestThermostatService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
