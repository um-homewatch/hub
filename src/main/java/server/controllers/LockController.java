package server.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import things.locks.Lock;
import things.locks.LockFactory;

import java.io.IOException;

public class LockController {
  private static final ObjectMapper OM = new ObjectMapper();

  public static String get(Request req, Response res) throws IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Lock lock = LockFactory.create(info.getAddress(), info.getPort(), info.getSubType());

    return OM.writeValueAsString(lock);
  }

  public static String put(Request req, Response res) throws NetworkException, IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    Lock lock = LockFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    JsonNode body = OM.readTree(req.body());

    if (!body.has("status")) {
      res.status(400);
      return "error";
    }

    lock.setLock(body.get("status").asBoolean());

    return OM.writeValueAsString(lock);
  }
}