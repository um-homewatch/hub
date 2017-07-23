package homewatch.things.services.locks;

import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;

public class Lock implements Thing {
  private final boolean locked;

  public Lock() {
    this.locked = false;
  }

  public Lock(boolean locked) {
    this.locked = locked;
  }

  public boolean isLocked() {
    return locked;
  }

  @Override
  public ThingServiceFactory<Lock> getFactory() {
    return new LockServiceFactory();
  }

  @Override
  public String getStringRepresentation() {
    return "locks";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Lock lock = (Lock) o;

    return locked == lock.locked;
  }

  @Override
  public int hashCode() {
    return (locked ? 1 : 0);
  }

  @Override
  public String toString() {
    return "Lock{" +
        "locked=" + locked +
        '}';
  }
}
