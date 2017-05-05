package homewatch.things.lights;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.HttpThingService;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestLightServiceFactory {
  private static final LightServiceFactory serviceFactory = new LightServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    InetAddress addr = InetAddress.getByName("192.168.1.50");
    HttpThingService<Light> lightService = serviceFactory.create(addr, 80, "rest");

    assertThat(lightService.getAddress(), is(addr));
    assertThat(lightService.getPort(), is(80));
    assertTrue(lightService instanceof RestLightService);
    assertThat(lightService.getType(), is("Things::Light"));
    assertThat(lightService.getSubType(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Light> lightService = serviceFactory.create("rest");

    assertTrue(lightService instanceof RestLightService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
