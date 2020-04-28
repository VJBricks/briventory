package controllers;

import controllers.auth.Secured;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the parts. */
@Security.Authenticated(Secured.class)
public class PartsController extends Controller {
}
