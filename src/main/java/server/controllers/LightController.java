package server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import things.lights.Light;
import things.lights.LightFactory;

import java.io.IOException;

public class LightController {
  private static final ObjectMapper OM = new ObjectMapper();

  public static String get(Request req, Response res) throws IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Light light = LightFactory.create(info.getAddress(), info.getPort(), info.getSubType());

    return OM.writeValueAsString(light);
  }

  public static String put(Request req, Response res) throws NetworkException, IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Light light = LightFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    JsonNode body = OM.readTree(req.body());

    if (!body.has("status")) {
      res.status(400);
      return "error";
    }

    light.setStatus(body.get("status").asBoolean());

    return OM.writeValueAsString(light);
  }
}
