package models;

/**
 * A design can be updated over the time. The design itself remain the same but small variations can occurs. {@code
 * Mold} represents the variations.
 */
public class Mold extends Design {

  /** The mold id. */
  private String moldId;
  /** Does this mold is an alternate. */
  private boolean alternate;

}
