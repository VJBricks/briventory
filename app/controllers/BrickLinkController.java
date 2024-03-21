package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public final class BrickLinkController extends Controller {

  public Result callback(final Http.Request request) {
    return ok();
  }

  public Result syncCatalog(final Http.Request request) {
    return ok();
  }
  public Result syncInventory(final Http.Request request) {
    return ok();
  }
  public Result syncOrders(final Http.Request request) {
    return ok();
  }
}
