package server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import things.exceptions.NetworkException;
import things.lights.Light;
import things.lights.LightFactory;

import java.io.IOException;

/**
 * Created by joses on 06/03/2017.
 */
public class LightController {
  private static ObjectMapper OM = new ObjectMapper();

  public static String get(Request req, Response res) throws NetworkException, IOException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Light light = LightFactory.create(info.getAddress(), info.getPort(), info.getSubType());

    return OM.writeValueAsString(light);
  }

  public static String put(Request req, Response res) throws NetworkException, IOException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Light light = LightFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    JsonNode body = OM.readTree(req.body());

    if (!body.has("status")){
      res.status(400);
      return "error";
    }

    light.setStatus(body.get("status").asBoolean());

    return OM.writeValueAsString(light);
  }
}
