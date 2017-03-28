package homewatch.constants;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import homewatch.exceptions.NetworkException;
import homewatch.net.JsonResponse;
import homewatch.net.NetUtils;
import okhttp3.HttpUrl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CacheUtils {
  private static final LoadingCache<HttpUrl, JsonResponse> cachedResponses = CacheBuilder.newBuilder()
      .expireAfterAccess(10, TimeUnit.MINUTES)
      .build(new CacheLoader<HttpUrl, JsonResponse>() {
        @Override
        public JsonResponse load(HttpUrl url) throws NetworkException {
          Logger.getGlobal().info(url.host());
          return NetUtils.get(url);
        }
      });

  private CacheUtils() {
  }

  public static JsonResponse get(HttpUrl url) throws ExecutionException {
    return cachedResponses.get(url);
  }
}
