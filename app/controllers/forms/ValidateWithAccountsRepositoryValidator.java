package controllers.forms;

import play.data.validation.Constraints.PlayConstraintValidator;
import repositories.AccountsRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidatorContext;

/** Form validation base class that provides a database context through {@link AccountsRepository}. */
public class ValidateWithAccountsRepositoryValidator
    implements PlayConstraintValidator<ValidateWithAccountsRepository, ValidatableWithAccountsRepository<?>> {

  /** The {@link AccountsRepository} instance. */
  private final AccountsRepository accountsRepository;

  /**
   * Creates a new {@link ValidateWithAccountsRepositoryValidator} by injecting the necessary parameters.
   *
   * @param accountsRepository the {@link AccountsRepository} instance.
   */
  @Inject
  public ValidateWithAccountsRepositoryValidator(final AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  /**
   * Initializes the validator.
   *
   * @param constraintAnnotation the {@link ValidateWithAccountsRepository} annotation.
   */
  @Override
  public void initialize(final ValidateWithAccountsRepository constraintAnnotation) { /* No-op. */}

  /**
   * Does this validator is valid.
   *
   * @param value the instance to validate.
   * @param constraintValidatorContext the context in which the constraint is evaluated.
   *
   * @return {@code true} if it is valid, otherwise {@code false}.
   */
  @Override
  public boolean isValid(final ValidatableWithAccountsRepository<?> value,
                         final ConstraintValidatorContext constraintValidatorContext) {
    return reportValidationStatus(value.validate(accountsRepository), constraintValidatorContext);
  }

}
