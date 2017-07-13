package homewatch.server;

import homewatch.things.Thing;
import org.reflections.Reflections;

import java.util.Set;

class ClassDiscoverer {
  private ClassDiscoverer() {
  }

  public static Set<Class<? extends Thing>> getThings() {
    Reflections reflections = new Reflections("homewatch.things");
    return reflections.getSubTypesOf(Thing.class);
  }
}
