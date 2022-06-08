package orm.caches;

import org.jooq.Record;
import org.jooq.Table;

import java.util.HashMap;
import java.util.Map;

public final class EntitiesCache {

  private final Map<Table<Record>, Record> entitiesPerType;

  EntitiesCache() {
    entitiesPerType = new HashMap<>();
  }

}
