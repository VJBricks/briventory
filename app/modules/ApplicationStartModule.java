package modules;

import com.google.inject.AbstractModule;
import globalhandlers.ApplicationStart;

/** This module binds the {@link ApplicationStart} handler. */
public final class ApplicationStartModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ApplicationStart.class).asEagerSingleton();
  }

}
