package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public final class Auth extends Controller {

  public Result signIn() {
    return ok("signIn");
  }

  public Result doSignIn(final Http.Request request) {
    return ok("doSignIn");
  }

  public Result adminSignUp() {
    return ok("adminSignUp");
  }

  public Result doAdminSignUp(final Http.Request request) {
    return ok("doAdminSignUp");
  }
}
