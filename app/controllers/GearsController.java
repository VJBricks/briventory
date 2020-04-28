package controllers;

import controllers.auth.Secured;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the gears. */
@Security.Authenticated(Secured.class)
public class GearsController extends Controller {
}
