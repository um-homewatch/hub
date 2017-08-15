package homewatch.net;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import homewatch.exceptions.NetworkException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HttpCachingUtils {
  private static final LoadingCache<String, ThingResponse> cachedResponses = CacheBuilder.newBuilder()
      .expireAfterAccess(10, TimeUnit.MINUTES)
      .build(new CacheLoader<String, ThingResponse>() {
        @Override
        public ThingResponse load(String url) throws NetworkException {
          return HttpUtils.get(url);
        }
      });

  private HttpCachingUtils() {
  }

  public static ThingResponse get(String url) throws ExecutionException {
    return cachedResponses.get(url);
  }
}
