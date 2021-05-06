package globalhandlers;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import play.Logger;

public final class CacheEventLogger implements CacheEventListener<Object, Object> {

  /** The {@link play.Logger.ALogger} to display what is happening. */
  private final Logger.ALogger logger = Logger.of(CacheEventLogger.class);

  @Override
  public void onEvent(final CacheEvent<?, ?> cacheEvent) {
    logger.debug("EHCache {} - {}: {} -> {}",
                 cacheEvent.getKey(),
                 cacheEvent.getType(),
                 cacheEvent.getOldValue(),
                 cacheEvent.getNewValue());
  }

}
