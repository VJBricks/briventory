package controllers.forms;

import repositories.AccountsRepository;

/**
 * Form validation while having access to the Briventory database.
 *
 * @param <T> the return type of the {@link #validate(AccountsRepository)} method.
 */
public interface ValidatableWithAccountsRepository<T> {

  /**
   * Validates the form while having access to the database.
   *
   * @param accountsRepository the {@link AccountsRepository} instance containing the necessary methods to access the
   * database.
   *
   * @return the validation errors.
   */
  T validate(AccountsRepository accountsRepository);

}
