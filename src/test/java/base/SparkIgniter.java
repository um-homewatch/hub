package base;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.xml.sax.SAXException;
import server.Main;

import java.io.IOException;

public class SparkIgniter {
  private static boolean STARTED = false;

  public static void start() {
    try {
      if (!STARTED) {
        Main.main(new String[1]);
        STARTED = true;
      }
    } catch (IOException | SAXException e) {
      e.printStackTrace();
    }
  }
}
