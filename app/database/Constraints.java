package database;

/** {@code Constraints} contains all constraints for each domain used in the database. */
public final class Constraints {

  /** The character length of the domain {@code dname}. */
  public static final int NAME_DOMAIN_LENGTH = 1024;

  /** The character length of the domain {@code demail}. */
  public static final int EMAIL_DOMAIN_LENGTH = 1024;

  /** The character length of the domain {@code dpassword}. */
  public static final int PASSWORD_DOMAIN_LENGTH = 1024;
  /** The minimal length of a password. */
  public static final int PASSWORD_MIN_LENGTH = 8;
  /** The minimal strength of new passwords. */
  public static final int PASSWORD_MIN_STRENGTH = 4;

  /** The charactere length of the domain {@code dtoken}. */
  public static final int TOKEN_DOMAIN_LENGTH = 64;

  /** The character length of the domain {@code durl}. */
  public static final int URL_DOMAIN_LENGTH = 4096;

  /** The caharcter length of the domain {@code ddescription}. */
  public static final int DESCRIPTION_DOMAIN_LENGTH = 2048;

  /** The minimal value of the domain {@code dcolorcomponent}. */
  public static final int COLOR_COMPONENT_MIN_VALUE = 0;
  /** The maximal value of the domain {@code dcolorcomponent}. */
  public static final int COLOR_COMPONENT_MAX_VALUE = 255;

  /** The minimal value of the domain {@code daplphacomponent}. */
  public static final double ALPHA_COMPONENT_MIN_VALUE = 0;
  /** The maximal value of the domain {@code dalphacomponent}. */
  public static final double ALPHA_COMPONENT_MAX_VALUE = 1;

  /** {@link Constraints} is a utility class, the instance creation is forbidden. */
  private Constraints() { /* Utility class, no instance allowed. */ }

}
