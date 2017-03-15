package lights;

import base.ControllerTest;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import exceptions.NetworkException;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.xml.sax.SAXException;
import server.Main;

import java.io.IOException;
import java.net.UnknownHostException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestLightController extends ControllerTest {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  // JsonObject to change the thing status
  private static JSONObject JSON;

  @BeforeClass
  public static void setup() throws IOException, SAXException {
    JSON = new JSONObject();
    JSON.put("status", false);

    //startup the server
    Main.main(new String[1]);
  }

  private static HttpUrl.Builder URL_BUILDER() {
    return new HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(4567)
            .addPathSegment("lights")
            .addQueryParameter("address", "localhost")
            .addQueryParameter("port", Integer.toString(8080))
            .addQueryParameter("subType", "rest");
  }

  @Test
  public void getStatus() throws UnknownHostException, NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    boolean status = NetUtils.get(URL_BUILDER().build())
            .getJson()
            .get("status")
            .asBoolean();

    assertThat(status, is(true));
  }

  @Test
  public void setStatus() throws UnknownHostException, NetworkException {
    LightStubs.stubPutStatus(wireMockRule, false);
    LightStubs.stubGetStatus(wireMockRule, false);

    boolean status = NetUtils.put(URL_BUILDER().build(), JSON)
            .getJson()
            .get("status")
            .asBoolean();

    assertThat(status, is(false));
  }

  @Test
  public void errorInvalidArgument() throws NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    int statusCode = NetUtils.put(URL_BUILDER().removeAllQueryParameters("address").build(), JSON)
            .getResponse()
            .code();

    assertThat(statusCode, is(400));
  }

  @Test
  public void errorInvalidSubType() throws NetworkException {
    LightStubs.stubGetStatus(wireMockRule, true);

    int statusCode = NetUtils.put(URL_BUILDER().removeAllQueryParameters("subType").addQueryParameter("subType", "cenas").build(), JSON)
            .getResponse()
            .code();

    assertThat(statusCode, is(400));
  }
}
