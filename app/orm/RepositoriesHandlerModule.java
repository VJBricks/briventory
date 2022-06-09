package orm;

import com.google.inject.AbstractModule;

/**
 * The {@code RepositoriesHandlerModule} must be enabled to allow the {@link RepositoriesHandler} to inject
 * {@link Repository} instances.
 */
public final class RepositoriesHandlerModule extends AbstractModule {

  /** {@inheritDoc} */
  @Override
  public void configure() {
    requestStaticInjection(RepositoriesHandler.class);
  }

}
