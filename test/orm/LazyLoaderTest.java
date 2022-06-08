package orm;

/** This test suite is focused on {@link orm.LazyLoader}. */
final class LazyLoaderTest {

  /** Tests the {@code null} value on the key. */
  //@Test
  void fetchNullKey() {
    /*
    final var lazyLoader = new LazyLoader<Integer, Object>(integer -> {
      assertNull(integer);
      return null;
    });
    assertFalse(lazyLoader.isFetched());

    final var nullValue = lazyLoader.getValue();
    assertNull(nullValue);
    assertFalse(lazyLoader.isFetched());
     */
  }

  /** Tests the {@code null} value on the value. */
  //@Test
  void fetchNullValue() {
    /*
    final var value = 3;
    final var lazyLoader = new LazyLoader<>(value, integer -> {
      assertNotNull(integer);
      return null;
    });
    assertFalse(lazyLoader.isFetched());

    final var nullValue = lazyLoader.getValue();
    assertNull(nullValue);
    assertTrue(lazyLoader.isFetched());
     */
  }

  /** Tests a real value on the key and the value. */
  //@Test
  void fetchRealValue() {
    /*
    final var value = 3;
    final var lazyLoader = new LazyLoader<>(value, integer -> {
      assertEquals(value, integer);
      return integer + 1;
    });
    assertFalse(lazyLoader.isFetched());

    final var intValue = lazyLoader.getValue();
    Assertions.assertNotNull(intValue);
    assertTrue(lazyLoader.isFetched());
    assertEquals(value + 1, intValue);
     */
  }

}
