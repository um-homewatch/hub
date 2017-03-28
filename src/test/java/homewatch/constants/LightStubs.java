package homewatch.constants;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class LightStubs {
  public static void stubGetStatus(WireMockRule wireMockRule, boolean value) {
    wireMockRule.stubFor(get(urlPathEqualTo("/status"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(String.format("{\"power\":%s}", value))));
  }

  public static void stubPutStatus(WireMockRule wireMockRule, boolean value) {
    wireMockRule.stubFor(put(urlPathEqualTo("/status"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(String.format("{\"power\":%s}", value))));
  }
}
