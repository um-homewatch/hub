package homewatch.things.services.locks;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestLockServiceFactory {
  private static final LockServiceFactory serviceFactory = new LockServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    NetworkThingService<Lock> lockService = serviceFactory.create(addr, 80, "rest");

    assertThat(lockService.getAddress(), is(addr));
    assertThat(lockService.getPort(), is(80));
    assertTrue(lockService instanceof RestLockService);
    assertThat(lockService.getType(), is("Things::Lock"));
    assertThat(lockService.getSubtype(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Lock> lockService = serviceFactory.create("rest");

    assertTrue(lockService instanceof RestLockService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
