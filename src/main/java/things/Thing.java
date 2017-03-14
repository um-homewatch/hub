package things;

@SuppressWarnings("SameReturnValue")
public interface Thing {
  boolean ping();

  String getType();

  String getSubType();
}
