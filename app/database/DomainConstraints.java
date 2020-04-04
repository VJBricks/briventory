package database;

/** {@code DomainConstraints} contains all constraints for each domain used in the database. */
public final class DomainConstraints {

  /** The character length of the domain {@code dname}. */
  public static final int NAME_LENGTH = 1024;
  /** The character length of the domain {@code demail}. */
  public static final int EMAIL_LENGTH = 1024;
  /** The character length of the domain {@code dpassword}. */
  public static final int PASSWORD_LENGTH = 1024;
  /** The character length of the domain {@code durl}. */
  public static final int URL_LENGTH = 4096;
  /** The caharcter length of the domain {@code ddescription}. */
  public static final int DESCRIPTION_LENGTH = 2048;
  /** The minimal value of the domain {@code dcolorcomponent}. */
  public static final int COLOR_COMPONENT_MIN_VALUE = 0;
  /** The maximal value of the domain {@code dcolorcomponent}. */
  public static final int COLOR_COMPONENT_MAX_VALUE = 255;
  /** The minimal value of the domain {@code daplphacomponent}. */
  public static final double ALPHA_COMPONENT_MIN_VALUE = 0;
  /** The maximal value of the domain {@code dalphacomponent}. */
  public static final double ALPHA_COMPONENT_MAX_VALUE = 1;

  /** {@link DomainConstraints} is a utility class, the instance creation is forbidden. */
  private DomainConstraints() { /* Utility class, no instance allowed. */ }

}
