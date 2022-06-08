package junit5;

import play.mvc.Http;
import play.mvc.Result;

public class J5WithApplication {

  /**
   * Provides an instance from the application.
   *
   * @param clazz the type's class.
   * @param <T> the type to return, using `app.injector.instanceOf`
   *
   * @return an instance of type T.
   */
  protected <T> T instanceOf(final Class<T> clazz) {
    return ApplicationHandler.getInstance().getByInjection(clazz);
  }

  protected Result resultFrom(final Http.RequestBuilder requestBuilder) {
    return ApplicationHandler.getInstance().getResult(requestBuilder);
  }

}
