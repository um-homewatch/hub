package homewatch.constants;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class NgrokStubs {
  public static void stubGetStatus(WireMockRule wireMockRule) throws IOException {
    JsonNode json = JsonUtils.getOM().readTree(new File("src/test/fixtures/ngrok.json"));

    wireMockRule.stubFor(get(urlPathEqualTo("/api/tunnels/homewatch-hub"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(json.toString())));
  }
}
