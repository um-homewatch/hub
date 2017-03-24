package server;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import constants.NgrokStubs;
import exceptions.NetworkException;
import net.JsonResponse;
import net.NetUtils;
import okhttp3.HttpUrl;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import things.ServerRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestNgrok extends ServerRunner{
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(4040).bindAddress("0.0.0.0"));

  @Test
  public void testGetTunnel() throws NetworkException, IOException {
    NgrokStubs.stubGetStatus(wireMockRule);

    JsonResponse jsonResponse = NetUtils.get(HttpUrl.parse("http://localhost:4567/tunnel"));

    String url = jsonResponse.getJson().get("url").asText();

    assertThat(url, is("https://cc9606d1.ngrok.io"));
  }
}
