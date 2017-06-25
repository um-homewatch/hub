package homewatch.things.motionsensors;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestMotionSensorServiceFactory {
  private static final MotionSensorServiceFactory serviceFactory = new MotionSensorServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    InetAddress addr = InetAddress.getByName("192.168.1.50");
    NetworkThingService<MotionSensor> motionsensorService = serviceFactory.create(addr, 80, "rest");

    assertThat(motionsensorService.getAddress(), is(addr));
    assertThat(motionsensorService.getPort(), is(80));
    assertTrue(motionsensorService instanceof RestMotionSensorService);
    assertThat(motionsensorService.getType(), is("Things::MotionSensor"));
    assertThat(motionsensorService.getSubtype(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<MotionSensor> motionsensorService = serviceFactory.create("rest");

    assertTrue(motionsensorService instanceof RestMotionSensorService);
  }

  @Test
  public void testIsSubTypeTrue() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
