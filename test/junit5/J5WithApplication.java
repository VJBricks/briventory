package junit5;

import akka.stream.Materializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.Helpers;

public class J5WithApplication {

  private Application app;

  /**
   * The application's Akka streams Materializer.
   */
  protected Materializer mat;

  /**
   * Override this method to setup the application to use.
   *
   * @return The application to use
   */
  private Application provideApplication() {
    /*return Helpers.fakeApplication();*/
    return Assertions.assertDoesNotThrow(() -> new GuiceApplicationBuilder()
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
        .build());
  }

  /**
   * Provides an instance from the application.
   *
   * @param clazz the type's class.
   * @param <T> the type to return, using `app.injector.instanceOf`
   *
   * @return an instance of type T.
   */
  protected <T> T instanceOf(Class<T> clazz) {
    return app.injector().instanceOf(clazz);
  }

  public Application getApp() { return app; }

  @BeforeEach // JUnit 5
  public void startPlay() {
    app = provideApplication();
    Helpers.start(app);
    mat = app.asScala().materializer();
  }

  @AfterEach // JUnit 5
  public void stopPlay() {
    if (app != null) {
      Helpers.stop(app);
      app = null;
    }
  }

}
