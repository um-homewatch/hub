package homewatch.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import homewatch.exceptions.ReadOnlyDeviceException;
import spark.Response;
import spark.Spark;

class ExceptionHandling {
  private ExceptionHandling() {
  }

  public static void perform() {
    Spark.notFound((req,res) -> resolveException("not found", res, 404));
    Spark.internalServerError((req,res) -> resolveException("not found", res, 500));

    Spark.exception(IllegalArgumentException.class, (exception, req, res) -> resolveException(exception, res, 400));
    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> resolveException(exception, res, 400));
    Spark.exception(ReadOnlyDeviceException.class, (exception, req, res) -> resolveException(exception, res, 404));
    Spark.exception(NetworkException.class, (exception, req, res) -> resolveException(exception, res, exception.getStatusCode()));
    Spark.exception(Exception.class, (exception, req, res) -> resolveException(exception, res, 500));
  }

  private static String resolveException(Exception exception, Response res, int statusCode) {
    String body = exceptionToString(exception);

    res.header("Content-Type", "application/json");
    res.status(statusCode);
    res.body(body);
    LoggerUtils.logException(exception);

    return body;
  }

  private static String resolveException(String exception, Response res, int statusCode) {
    res.header("Content-Type", "application/json");
    res.status(statusCode);
    res.body(exception);

    return exception;
  }

  private static String exceptionToString(Exception e) {
    try {
      return JsonUtils.getOM().writeValueAsString(new ErrorMessage(e));
    } catch (JsonProcessingException e1) {
      LoggerUtils.logException(e1);
      return null;
    }
  }
}
