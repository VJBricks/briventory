package controllers.forms;

import database.BriventoryDB;

/**
 * Form validation while having access to the Briventory database.
 *
 * @param <T> the return type of the {@link #validate(BriventoryDB)} method.
 */
public interface ValidatableWithDB<T> {

  /**
   * Validates the form while having access to the database.
   *
   * @param briventoryDB the {@link BriventoryDB} instance containing the necessary methods to access the database.
   *
   * @return the validation errors.
   */
  T validate(BriventoryDB briventoryDB);

}
