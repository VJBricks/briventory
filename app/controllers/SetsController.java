package controllers;

import controllers.auth.SignedInAuthenticator;
import play.mvc.Controller;
import play.mvc.Security;

/** This controller handle all actions related to the sets. */
@Security.Authenticated(SignedInAuthenticator.class)
public final class SetsController extends Controller {
}
