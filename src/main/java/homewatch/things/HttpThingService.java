package homewatch.things;

public abstract class HttpThingService<T extends Thing> extends NetworkThingService<T> {
  protected HttpThingService() {
  }

  protected HttpThingService(String address) {
    super(address);
  }

  protected HttpThingService(String address, Integer port) {
    super(address, port);
  }

  protected String getUrl() {
    if (this.getPort() == null)
      return String.format("http://%s", this.getAddress());
    else
      return String.format("http://%s:%d", this.getAddress(), this.getPort());
  }
}
