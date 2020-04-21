package controllers.forms;

import database.BriventoryDB;
import play.data.validation.Constraints.PlayConstraintValidator;

import javax.inject.Inject;
import javax.validation.ConstraintValidatorContext;

/** Form validation base class that provides a database context through {@link BriventoryDB}. */
public class ValidateWithDBValidator implements PlayConstraintValidator<ValidateWithDB, ValidatableWithDB<?>> {

  /** The {@link BriventoryDB} instance. */
  private final BriventoryDB briventoryDB;

  /**
   * Creates a new {@link ValidateWithDBValidator} by injecting the necessary parameters.
   *
   * @param briventoryDB the persistence context.
   */
  @Inject
  public ValidateWithDBValidator(final BriventoryDB briventoryDB) {
    this.briventoryDB = briventoryDB;
  }

  /**
   * Initializes the validator.
   *
   * @param constraintAnnotation the {@link ValidateWithDB} annotation.
   */
  @Override
  public void initialize(final ValidateWithDB constraintAnnotation) {}

  /**
   * Does this validator is valid.
   *
   * @param value the instance to validate.
   * @param constraintValidatorContext the context in which the constraint is evaluated.
   *
   * @return {@code true} if it is valid, otherwise {@code false}.
   */
  @Override
  public boolean isValid(final ValidatableWithDB<?> value,
                         final ConstraintValidatorContext constraintValidatorContext) {
    return reportValidationStatus(value.validate(briventoryDB), constraintValidatorContext);
  }

}
