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
    Spark.exception(IllegalArgumentException.class, (exception, req, res) -> resolveException(exception, res, 400));
    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> resolveException(exception, res, 400));
    Spark.exception(ReadOnlyDeviceException.class, (exception, req, res) -> resolveException(exception, res, 404));

    Spark.exception(NetworkException.class, (exception, req, res) -> resolveException(exception, res, exception.getStatusCode()));

    Spark.exception(Exception.class, (exception, req, res) -> resolveException(exception, res, 500));
  }

  private static void resolveException(Exception exception, Response res, int statusCode) {
    res.header("Content-Type", "application/json");
    res.status(statusCode);
    res.body(exceptionToString(exception));
    LoggerUtils.logException(exception);
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
