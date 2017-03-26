package constants;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import things.thermostat.Thermostat;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ThermostatStubs {
  public static void stubGetStatus(WireMockRule wireMockRule, Thermostat thermostat) {
    wireMockRule.stubFor(get(urlPathEqualTo("/status"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(String.format("{\"target_temperature\":%s}", thermostat.getTargetTemperature()))));
  }

  public static void stubPutStatus(WireMockRule wireMockRule, Thermostat thermostat) {
    wireMockRule.stubFor(put(urlPathEqualTo("/status"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(String.format("{\"target_temperature\":%s}", thermostat.getTargetTemperature()))));
  }
}
