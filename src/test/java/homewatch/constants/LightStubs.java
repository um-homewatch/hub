package homewatch.constants;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class LightStubs {
  public static void stubGetRest(WireMockRule wireMockRule, boolean value) {
    wireMockRule.stubFor(get(urlPathEqualTo("/status"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(String.format("{\"power\":%s}", value))));
  }

  public static void stubPutRest(WireMockRule wireMockRule, boolean value) {
    wireMockRule.stubFor(put(urlPathEqualTo("/status"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(String.format("{\"power\":%s}", value))));
  }

  public static void stubGetHue(WireMockRule wireMockRule, boolean value) {
    String response = String.format("{\"state\":{\"on\":%s}}", value);

    wireMockRule.stubFor(get(urlPathEqualTo("/api/newdeveloper/lights/1"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(response)));
  }

  public static void stubPutHue(WireMockRule wireMockRule, boolean value) {
    String response = String.format("{\"state\":{\"on\":%s}}", value);

    wireMockRule.stubFor(put(urlPathEqualTo("/api/newdeveloper/lights/1/state"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(response)));
  }
}
