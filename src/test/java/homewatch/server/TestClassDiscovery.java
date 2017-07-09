package homewatch.server;

import com.google.common.collect.Sets;
import homewatch.things.Thing;
import homewatch.things.services.lights.Light;
import homewatch.things.services.locks.Lock;
import homewatch.things.services.motionsensors.MotionSensor;
import homewatch.things.services.thermostat.Thermostat;
import homewatch.things.services.weather.Weather;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestClassDiscovery {
  @Test
  public void testClasses() {
    Set<Class<? extends Thing>> classes = Sets.newHashSet(Light.class, Weather.class, Thermostat.class, MotionSensor.class, Lock.class);
    Set<Class<? extends Thing>> scannedClasses = ClassDiscoverer.getThings();

    assertThat(scannedClasses, is(classes));
  }
}
