package things.locks;

public class Lock {
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
}
