package globalhandlers;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import play.Environment;
import play.db.DBApi;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@code ApplicationStart} is a singleton that is launched every time the App is started. It performs the following
 * operations:
 * <ul>
 *   <li>migrate the database if a new version is available.</li>
 * </ul>
 */
@Singleton
public final class ApplicationStart {

  /** The {@link MetricRegistry} instance. */
  private final MetricRegistry metricRegistry = new MetricRegistry();

  /**
   * Creates a new instance of {@link ApplicationStart} using the injected parameters.
   *
   * @param lifecycle the {@link ApplicationLifecycle} instance.
   * @param environment the {@link Environment} instance.
   * @param dbApi the {@link DBApi} instance.
   */
  @Inject
  public ApplicationStart(final ApplicationLifecycle lifecycle, final Environment environment, final DBApi dbApi) {
    final var dataSource = dbApi.getDatabase("briventory").getDataSource();
    if (dataSource instanceof HikariDataSource)
      ((HikariDataSource) dataSource).setMetricRegistry(metricRegistry);

    lifecycle.addStopHook(() -> {
      dbApi.shutdown();
      return null;
    });
  }

}
