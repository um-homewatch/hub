package things;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;
import server.Main;
import spark.Spark;

import java.io.IOException;

@Ignore
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
public class ServerRunner {
  @BeforeClass
  public static void setUpBaseClass() {
    try {
      Main.main(new String[1]);
    } catch (IOException | SAXException e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void tearDown() {
    Spark.stop();
  }
}
