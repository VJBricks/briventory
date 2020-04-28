package controllers;

import controllers.auth.Secured;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the sets. */
@Security.Authenticated(Secured.class)
public final class SetsController extends Controller {
}
