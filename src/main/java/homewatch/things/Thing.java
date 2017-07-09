package homewatch.things;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Thing {
  @JsonIgnore
  public ThingServiceFactory getFactory();

  @JsonIgnore
  public String getStringRepresentation();
}