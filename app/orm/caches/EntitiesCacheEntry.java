package orm.caches;

import org.jooq.Record;
import org.jooq.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EntitiesCacheEntry<R extends Record> implements Map.Entry<Table<R>, List<R>> {

  private Table<R> table;
  private List<R> records;

  EntitiesCacheEntry(final Table<R> table) {
    this.table = table;
  }

  /** {@inheritDoc} */
  @Override
  public Table<R> getKey() { return table; }

  /** {@inheritDoc} */
  @Override
  public List<R> getValue() { return List.copyOf(records); }

  /** {@inheritDoc} */
  @Override
  public List<R> setValue(final List<R> value) {
    final List<R> oldList = records;
    records = new LinkedList<>(value);
    return oldList;
  }

  /**
   * Compares the specified object with this entry for equality. Returns {@code true} if the given object is also a map
   * entry and the two entries represent the same mapping.  More formally, two entries {@code e1} and {@code e2}
   * represent the same mapping
   * if<pre>
   *     (e1.getKey()==null ?
   *      e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &amp;&amp;
   *     (e1.getValue()==null ?
   *      e2.getValue()==null : e1.getValue().equals(e2.getValue()))
   * </pre>
   * This ensures that the {@code equals} method works properly across different implementations of the {@code
   * Map.Entry} interface.
   *
   * @param o object to be compared for equality with this map entry
   *
   * @return {@code true} if the specified object is equal to this map entry
   */
  @Override
  public boolean equals(final Object o) {
    return false;
  }

  /**
   * Returns the hash code value for this map entry.  The hash code
   * of a map entry {@code e} is defined to be: <pre>
   *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
   *     (e.getValue()==null ? 0 : e.getValue().hashCode())
   * </pre>
   * This ensures that {@code e1.equals(e2)} implies that {@code e1.hashCode()==e2.hashCode()} for any two Entries
   * {@code e1} and {@code e2}, as required by the general contract of {@code Object.hashCode}.
   *
   * @return the hash code value for this map entry
   *
   * @see Object#hashCode()
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  @Override
  public int hashCode() {
    return 0;
  }

}
