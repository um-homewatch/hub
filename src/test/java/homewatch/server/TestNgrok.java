package homewatch.server;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.constants.NgrokStubs;
import homewatch.exceptions.NetworkException;
import homewatch.net.HttpUtils;
import homewatch.net.JsonResponse;
import homewatch.things.ServerRunner;
import okhttp3.HttpUrl;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@PowerMockIgnore("javax.net.ssl.*")
public class TestNgrok extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(4040).bindAddress("0.0.0.0"));

  @Test
  public void testGetTunnel() throws NetworkException, IOException {
    NgrokStubs.stubGetStatus(wireMockRule);

    JsonResponse jsonResponse = HttpUtils.get(HttpUrl.parse("http://localhost:4567/tunnel"));

    String url = jsonResponse.getJson().get("url").asText();

    assertThat(url, is("https://cc9606d1.ngrok.io"));
  }
}
