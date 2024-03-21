package repositories;

import database.BriventoryDB;
import jooq.tables.records.AdministratorRecord;
import jooq.tables.records.LockedAccountRecord;
import models.Account;
import org.jooq.DSLContext;
import org.jooq.Select;
import orm.*;
import play.data.validation.ValidationError;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static jooq.Tables.*;

/**
 * The {@code ActorsRepository} handle all database actions on the following tables: {@link jooq.tables.Account},
 * {@link jooq.tables.Administrator} and {@link jooq.tables.LockedAccount}.
 */
@Singleton
public final class AccountsRepository extends Repository<Account> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link AccountsRepository} using injection.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   */
  @Inject
  public AccountsRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Storage & Deletion
  // *******************************************************************************************************************

  /**
   * Persists the provided {@link Account} into the database.
   *
   * @param account the {@link Account} to persist.
   */
  public void persist(final Account account) { super.persist(account); }

  /**
   * Deletes the provided {@link Account} from the database.
   *
   * @param account the {@link Account} to delete.
   */
  public void delete(final Account account) { deleteInTransaction(account); }

  public void delete(final List<Account> accounts) { deleteAllInTransaction(accounts); }

  // *******************************************************************************************************************
  // Locked Users Matters
  // *******************************************************************************************************************

  /**
   * Creates the {@link RecordLoader} that will handle the locked state of an account.
   *
   * @param account the {@link Account} concerned.
   *
   * @return a {@link RecordLoader}.
   */
  public RecordLoader<Account, Boolean> createLockedAccountLoader(final Account account) {
    return createRecordLoader(account,
                              (dslContext, a) -> exists(dslContext, isLocked(dslContext, account)),
                              (dslContext, a, isLocked) -> {
                                final LockedAccountRecord lockedAccountRecord = dslContext.newRecord(LOCKED_ACCOUNT)
                                                                                          .setIdAccount(a.getId());
                                Action action;
                                if (Boolean.TRUE.equals(isLocked))
                                  action = new PersistRecordAction<>(lockedAccountRecord);
                                else
                                  action = new DeleteRecordAction<>(lockedAccountRecord);
                                return Collections.singletonList(action);
                              });
  }

  /**
   * Checks into the database, if the provided {@link Account} is locked.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} is locked, otherwise {@code false}.
   */
  private Select<?> isLocked(final DSLContext dslContext, final Account account) {
    return dslContext.selectFrom(LOCKED_ACCOUNT)
                     .where(LOCKED_ACCOUNT.ID_ACCOUNT.eq(account.getId()));
  }

  /**
   * Checks into the database, if the provided {@link Account} is locked.
   *
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} is locked, otherwise {@code false}.
   */
  public boolean isLocked(final Account account) {
    return exists(dslContext -> isLocked(dslContext, account));
  }

  /** @return a {@link List} containing all locked accounts. */
  public List<Account> getLockedAccounts() {
    return fetch(Account.ACCOUNT_MAPPER,
                 dslContext ->
                     dslContext.select(ACCOUNT.asterisk())
                               .from(ACCOUNT)
                               .innerJoin(LOCKED_ACCOUNT).on(ACCOUNT.ID.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                               .coerce(ACCOUNT));
  }

  // *******************************************************************************************************************
  // Administrator Matters
  // *******************************************************************************************************************

  /**
   * Creates the {@link RecordLoader} that will handle the locked state of an account.
   *
   * @param account the {@link Account} concerned.
   *
   * @return a {@link RecordLoader}.
   */
  public RecordLoader<Account, Boolean> createAdministratorLoader(final Account account) {
    return createRecordLoader(account,
                              (dslContext, a) -> exists(dslContext, isAdministrator(dslContext, a)),
                              (dslContext, a, isAdministrator) -> {
                                final AdministratorRecord administratorRecord = dslContext.newRecord(ADMINISTRATOR)
                                                                                          .setIdAccount(a.getId());
                                Action action;
                                if (Boolean.TRUE.equals(isAdministrator))
                                  action = new PersistRecordAction<>(administratorRecord);
                                else
                                  action = new DeleteRecordAction<>(administratorRecord);
                                return Collections.singletonList(action);
                              });
  }

  /**
   * Checks into the database, if the provided {@link Account} has administrator rights.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} is locked, otherwise {@code false}.
   */
  private Select<?> isAdministrator(final DSLContext dslContext, final Account account) {
    return dslContext.selectFrom(ADMINISTRATOR)
                     .where(ADMINISTRATOR.ID_ACCOUNT.eq(account.getId()));
  }

  /**
   * Checks into the database if the provided {@link Account} has administrator rights.
   *
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} has administrator rights, otherwise {@code false}.
   */
  public boolean isAdministrator(final Account account) {
    return exists(dslContext -> isAdministrator(dslContext, account));
  }

  /** @return a {@link List} containing all administrators. */
  public List<Account> getAdministrators() {
    return fetch(Account.ACCOUNT_MAPPER,
                 dslContext ->
                     dslContext.select(ACCOUNT.asterisk())
                               .from(ACCOUNT)
                               .innerJoin(ADMINISTRATOR).on(ACCOUNT.ID.eq(ADMINISTRATOR.ID_ACCOUNT))
                               .coerce(ACCOUNT));
  }

  /**
   * Queries the database to find out if the account is an active administrator, and is the last one.
   *
   * @param account the {@link Account} to check with.
   *
   * @return {@code true} the {@link Account} is the last active administrator, otherwise {@code false}.
   */
  public boolean isLastActiveAdministrator(final Account account) {
    return exists(dslContext ->
                      dslContext.select(ADMINISTRATOR.asterisk())
                                .from(ADMINISTRATOR)
                                .leftJoin(LOCKED_ACCOUNT).on(ADMINISTRATOR.ID_ACCOUNT.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                                .where(ADMINISTRATOR.ID_ACCOUNT.ne(account.getId())
                                                               .and(LOCKED_ACCOUNT.ID_ACCOUNT.isNull())));
  }

  /**
   * Queries the database to find out if the account is an active administrator, and is the last one.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account} to check with.
   *
   * @return {@code true} the {@link Account} is the last active administrator, otherwise {@code false}.
   */
  public boolean isLastActiveAdministrator(final DSLContext dslContext, final Account account) {
    final Integer count = dslContext.selectCount()
                                    .from(ADMINISTRATOR)
                                    .leftJoin(LOCKED_ACCOUNT).on(ADMINISTRATOR.ID_ACCOUNT.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                                    .where(ADMINISTRATOR.ID_ACCOUNT.ne(account.getId())
                                                                   .and(LOCKED_ACCOUNT.ID_ACCOUNT.isNull()))
                                    .fetchOneInto(Integer.class);
    if (count == null) return false;
    return count == 0;
  }

  /** @return {@code true} if the database contains at least one active administrator, otherwise {@code false}. */
  public boolean hasActiveAdministrator() {
    return exists(dslContext ->
                      dslContext.select(ADMINISTRATOR.asterisk())
                                .from(ADMINISTRATOR)
                                .leftJoin(LOCKED_ACCOUNT).on(ADMINISTRATOR.ID_ACCOUNT.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                                .where(LOCKED_ACCOUNT.ID_ACCOUNT.isNull())
    );
  }

  // *******************************************************************************************************************
  // Data Retrieval
  // *******************************************************************************************************************

  /** @return all {@link Account}s in the database. */
  public List<Account> getAll() { return fetch(Account.ACCOUNT_MAPPER, dslContext -> dslContext.selectFrom(ACCOUNT)); }

  /**
   * Retrieves the {@link Account} corresponding to the id provided.
   *
   * @param id the id of the {@link Account} to search for.
   *
   * @return an {@link Optional} containing the user.
   */
  public Optional<Account> findById(final long id) {
    return fetchOptional(Account.ACCOUNT_MAPPER,
                         dslContext -> dslContext.selectFrom(ACCOUNT)
                                                 .where(ACCOUNT.ID.eq(id)));
  }

  /**
   * Retrieves all {@link Account}s using the e-mail address given. Normally, e-mail addresses are unique, so a
   * singleton list or an empty one should be returned by this method.
   *
   * @param email the e-mail address.
   *
   * @return a list of {@link Account} instance.
   */
  public Optional<Account> findByEmail(final String email) {
    return fetchOptional(Account.ACCOUNT_MAPPER,
                         dslContext -> dslContext.selectFrom(ACCOUNT)
                                                 .where(ACCOUNT.EMAIL.eq(email)));
  }

  // *******************************************************************************************************************
  // Data Validation
  // *******************************************************************************************************************
  public List<ValidationError> validate(final Account account) { return super.validate(account); }

  public boolean emailAlreadyExists(final Account account, final String email) {
    return exists(dslContext -> dslContext.selectFrom(ACCOUNT)
                                          .where(ACCOUNT.EMAIL.equalIgnoreCase(email.trim()))
                                          .and(ACCOUNT.ID.ne(account.getId())));
  }

  public boolean emailAlreadyExists(final String email) {
    return exists(dslContext -> dslContext.selectFrom(ACCOUNT)
                                          .where(ACCOUNT.EMAIL.equalIgnoreCase(email.trim())));
  }

}
