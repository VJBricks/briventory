package controllers;

import junit5.J5WithApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

class GlobalControllerTest extends J5WithApplication {

  @Test
  void testIndex() {
    Http.RequestBuilder request = new Http.RequestBuilder()
        .method(Helpers.GET)
        .uri("/");

    Result result = Helpers.route(getApp(), request);
    Assertions.assertEquals(Http.Status.SEE_OTHER, result.status());
  }

}
