package database;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class BriventoryDBContext extends CustomExecutionContext {

  @Inject
  public BriventoryDBContext(final ActorSystem actorSystem) {
    super(actorSystem, "contexts.briventory");
  }

}
