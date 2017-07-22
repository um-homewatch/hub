package homewatch.net;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import homewatch.exceptions.NetworkException;
import okhttp3.HttpUrl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HttpCachingUtils {
  private static final LoadingCache<HttpUrl, ThingResponse> cachedResponses = CacheBuilder.newBuilder()
          .expireAfterAccess(10, TimeUnit.MINUTES)
          .build(new CacheLoader<HttpUrl, ThingResponse>() {
            @Override
            public ThingResponse load(HttpUrl url) throws NetworkException {
              return HttpUtils.get(url);
            }
          });

  private HttpCachingUtils() {
  }

  public static ThingResponse get(HttpUrl url) throws ExecutionException {
    return cachedResponses.get(url);
  }
}
