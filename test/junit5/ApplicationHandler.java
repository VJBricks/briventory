package junit5;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.concurrent.atomic.AtomicInteger;

public final class ApplicationHandler {
  private static ApplicationHandler singletonInstance;

  static synchronized ApplicationHandler getInstance() {
    if (singletonInstance == null) singletonInstance = new ApplicationHandler();
    return singletonInstance;
  }

  private Application application;

  private final AtomicInteger applicationReferenceCounter;

  private ApplicationHandler() {
    applicationReferenceCounter = new AtomicInteger(0);
    application = new GuiceApplicationBuilder()
        /*.load(
            new play.api.inject.BuiltinModule(),
            new play.inject.BuiltInModule(),
            new play.api.i18n.I18nModule(),
            new play.api.db.DBModule(),
            new play.db.DBModule(),
            new play.api.db.HikariCPModule(),
            new play.api.mvc.CookiesModule(),
            new play.data.FormFactoryModule(),
            new play.data.FormFactoryModule(),
            new play.data.validation.ValidatorsModule(),
            new AssetsModule(),
            new CSRFModule(),
            new AllowedHostsModule())*/
        /*.withConfigLoader(
            environment -> ConfigFactory.load(environment.classLoader()))*/
        /*.in(Mode.DEV)*/
        /*.load(
            new play.api.inject.BuiltinModule(),
            new play.inject.BuiltInModule(),
            new play.api.i18n.I18nModule(),
            new play.api.db.DBModule(),
            new play.db.DBModule(),
            new play.db.jpa.JPAModule(),
            new play.api.db.HikariCPModule(),
            new play.api.mvc.CookiesModule(),
            new play.data.FormFactoryModule(),
            new play.data.FormFactoryModule(),
            new play.data.validation.ValidatorsModule(),
            new AssetsModule())
        .in(Mode.DEV)
        .withConfigLoader(env -> ConfigFactory.load(env.classLoader()))*/
        .build();
  }

  <T> T getByInjection(final Class<T> injectedType) {
    synchronized (application) {
      return application.injector().instanceOf(injectedType);
    }
  }

  Result getResult(final Http.RequestBuilder requestBuilder) {
    synchronized (application) {
      return Helpers.route(application, requestBuilder);
    }
  }

  void startApplication() {
    synchronized (application) {
      final int count = applicationReferenceCounter.getAndIncrement();
      if (count == 0) Helpers.start(application);
    }
  }

  void stopApplication() {
    synchronized (application) {
      final int count = applicationReferenceCounter.decrementAndGet();
      if (count == 0) Helpers.stop(application);
    }
  }

}
