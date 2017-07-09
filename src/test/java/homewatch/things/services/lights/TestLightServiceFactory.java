package homewatch.things.services.lights;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestLightServiceFactory {
  private static final LightServiceFactory serviceFactory = new LightServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    NetworkThingService<Light> lightService = serviceFactory.create(addr, 80, "rest");

    assertThat(lightService.getAddress(), is(addr));
    assertThat(lightService.getPort(), is(80));
    assertTrue(lightService instanceof RestLightService);
    assertThat(lightService.getType(), is("Things::Light"));
    assertThat(lightService.getSubtype(), is("rest"));
  }

  @Test
  public void testCoapCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    NetworkThingService<Light> lightService = serviceFactory.create(addr, 80, "coap");

    assertThat(lightService.getAddress(), is(addr));
    assertThat(lightService.getPort(), is(80));
    assertTrue(lightService instanceof CoapLightService);
    assertThat(lightService.getType(), is("Things::Light"));
    assertThat(lightService.getSubtype(), is("coap"));
  }

  @Test
  public void testHueCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    NetworkThingService<Light> lightService = serviceFactory.create(addr, 80, "hue");

    assertThat(lightService.getAddress(), is(addr));
    assertThat(lightService.getPort(), is(80));
    assertTrue(lightService instanceof HueLightService);
    assertThat(lightService.getType(), is("Things::Light"));
    assertThat(lightService.getSubtype(), is("hue"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Light> lightService = serviceFactory.create("rest");

    assertTrue(lightService instanceof RestLightService);
  }

  @Test
  public void testIsSubTypeRest() {
    assertTrue(serviceFactory.isSubType("rest"));
  }

  @Test
  public void testIsSubTypeCoap() {
    assertTrue(serviceFactory.isSubType("coap"));
  }

  @Test
  public void testIsSubTypeHue() {
    assertTrue(serviceFactory.isSubType("hue"));
  }
}
