package homewatch.things;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Thing {
  @JsonIgnore
  ThingServiceFactory getFactory();

  @JsonIgnore
  String getStringRepresentation();
}