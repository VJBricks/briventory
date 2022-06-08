package orm;

import com.google.inject.AbstractModule;

public final class RepositoriesHandlerModule extends AbstractModule {

  public void configure() {
    requestStaticInjection(RepositoriesHandler.class);
  }

}
