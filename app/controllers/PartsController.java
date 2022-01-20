package controllers;

import controllers.auth.SignedInAuthenticator;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the parts. */
@Security.Authenticated(SignedInAuthenticator.class)
public class PartsController extends Controller {
}
