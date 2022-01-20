package database;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * {@code BriventoryDBContext} will create a specific {@link scala.concurrent.ExecutionContext}, that will encapsulate
 * all the operations performed on the database.
 */
public class BriventoryDBContext extends CustomExecutionContext {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link database.BriventoryDBContext}.
   *
   * @param actorSystem the {@link ActorSystem}.
   */
  @Inject
  public BriventoryDBContext(final ActorSystem actorSystem) {
    super(actorSystem, "contexts.briventory");
  }

}
