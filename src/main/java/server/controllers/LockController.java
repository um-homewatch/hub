package server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidSubTypeException;
import exceptions.NetworkException;
import spark.Request;
import spark.Response;
import things.ThingService;
import things.locks.Lock;
import things.locks.LockServiceFactory;

import java.io.IOException;

public class LockController {
  private static final ObjectMapper OM = new ObjectMapper();

  public static String get(Request req, Response res) throws IOException, InvalidSubTypeException, NetworkException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    ThingService<Lock> lockService = LockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());

    return OM.writeValueAsString(lockService.get());
  }

  public static String put(Request req, Response res) throws NetworkException, IOException, InvalidSubTypeException {
    HttpThingInfo info = HttpThingInfo.fromQueryString(req.queryMap());
    ThingService<Lock> lockService = LockServiceFactory.create(info.getAddress(), info.getPort(), info.getSubType());
    Lock lock = OM.readValue(req.body(), Lock.class);

    lockService.put(lock);

    return OM.writeValueAsString(lock);
  }
}