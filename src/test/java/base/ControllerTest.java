package base;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.xml.sax.SAXException;
import server.Main;
import spark.Spark;

import java.io.IOException;

@Ignore
public class ControllerTest {
  @BeforeClass
  public static void setUpBaseClass() {
    try {
      Main.main(new String[1]);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }
}
