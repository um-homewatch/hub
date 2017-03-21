package things;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.xml.sax.SAXException;
import server.Main;

import java.io.IOException;

@Ignore
public class ServerRunner {
  @BeforeClass
  public static void setUpBaseClass() {
    try {
      Main.main(new String[1]);
    } catch (IOException | SAXException e) {
      e.printStackTrace();
    }
  }
}
