package models;

public final class User {
  /** The id of this {@link User}. */
  private Long id;
  /** The e-mail address. */
  private String email;
  /** The name. */
  private String name;
  /** This user is locked. */
  private Boolean locked;
  /** This user is an administrator. */
  private Boolean administrator;
  /** The default {@link ColorSource}. */
  private ColorSource defaultColorSource;

}
