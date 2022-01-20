package controllers;

import controllers.auth.SignedInAuthenticator;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the gears. */
@Security.Authenticated(SignedInAuthenticator.class)
public class GearsController extends Controller {
}
