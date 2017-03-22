package things.locks;

import exceptions.InvalidSubTypeException;
import org.junit.Test;
import things.HttpThingService;
import things.ThingService;
import things.locks.Lock;
import things.locks.LockServiceFactory;
import things.locks.RestLockService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestLockServiceFactory {
  private static final LockServiceFactory serviceFactory = new LockServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    InetAddress addr = InetAddress.getByName("192.168.1.50");
    HttpThingService<Lock> lockService = serviceFactory.create(addr, 80, "rest");

    assertThat(lockService.getIpAddress(), is(addr));
    assertThat(lockService.getPort(), is(80));
    assertTrue(lockService instanceof RestLockService);
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Lock> lockService = serviceFactory.create("rest");

    assertTrue(lockService instanceof RestLockService);
  }

  @Test
  public void testIsSubType(){
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
