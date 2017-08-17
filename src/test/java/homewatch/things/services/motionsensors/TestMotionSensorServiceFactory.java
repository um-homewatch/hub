package homewatch.things.services.motionsensors;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestMotionSensorServiceFactory {
  private static final MotionSensorServiceFactory serviceFactory = new MotionSensorServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    ThingService<MotionSensor> motionsensorService = serviceFactory.create("rest");

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
