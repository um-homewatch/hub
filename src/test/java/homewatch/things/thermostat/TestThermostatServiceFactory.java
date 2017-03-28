package homewatch.things.thermostat;

import homewatch.exceptions.InvalidSubTypeException;
import org.junit.Test;
import homewatch.things.ThingService;

import java.net.UnknownHostException;

import static org.junit.Assert.assertTrue;

public class TestThermostatServiceFactory {
  private static final ThermostatServiceFactory serviceFactory = new ThermostatServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Thermostat> thermostatService = serviceFactory.create("rest");

    assertTrue(thermostatService instanceof RestThermostatService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
