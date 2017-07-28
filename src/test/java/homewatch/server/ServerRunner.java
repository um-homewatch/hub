package homewatch.server;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Ignore
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
public class ServerRunner {
  @BeforeClass
  public static void setUpBaseClass() {
    Main.main(new String[1]);
  }

  @AfterClass
  public static void tearDown() {
    Spark.stop();
  }
}
