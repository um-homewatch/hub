package homewatch.constants;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.net.HttpCachingUtils;
import okhttp3.HttpUrl;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestCacheUtils {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  //make request twice, but only one actually consumes the http resource
  public void testCachingGet() throws ExecutionException {
    wireMockRule.stubFor(get(urlPathEqualTo("/")).willReturn(
        aResponse().withBody("foo")));

    byte[] foo = HttpCachingUtils.get(HttpUrl.parse("http://localhost:8080")).getPayload();

    assertThat(foo, is("foo".getBytes()));

    HttpCachingUtils.get(HttpUrl.parse("http://localhost:8080"));

    wireMockRule.verify(1, getRequestedFor(urlPathEqualTo("/")));
  }
}
