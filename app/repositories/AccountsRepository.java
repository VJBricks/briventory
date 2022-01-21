package repositories;

import database.BriventoryDB;
import jooq.tables.records.AccountRecord;
import jooq.tables.records.AdministratorRecord;
import jooq.tables.records.BricklinkTokensRecord;
import jooq.tables.records.BricksetTokensRecord;
import jooq.tables.records.LockedAccountRecord;
import jooq.tables.records.RebrickableTokensRecord;
import models.Account;
import models.BricklinkTokens;
import models.BricksetTokens;
import models.RebrickableTokens;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import orm.repositories.DeletableEntityHandler;
import orm.repositories.DeleteAction;
import orm.repositories.EntityAction;
import orm.repositories.EntityReloader;
import orm.repositories.Repository;
import orm.repositories.StorableEntityHandler;
import orm.repositories.StoreAction;
import play.data.validation.ValidationError;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static jooq.Tables.*;

/**
 * The {@code ActorsRepository} handle all database actions on the following tables: {@link jooq.tables.Account}, {@link
 * jooq.tables.Administrator} and {@link jooq.tables.LockedAccount}. It also handles the various tokens for external
 * systems: {@link BricklinkTokens}, {@link BricksetTokens} and {@link RebrickableTokens}.
 */
@Singleton
public final class AccountsRepository extends Repository {

  // *******************************************************************************************************************
  // Record Mapping for Accounts
  // *******************************************************************************************************************
  /** Specific {@link RecordMapper} to convert an {@link AccountRecord} to an {@link Account} instance. */
  private final RecordMapper<AccountRecord, Account> accountsMapper =
      r -> new Account(AccountsRepository.this).setId(r.getId())
                                               .setIdColorSource(r.getIdColorSource())
                                               .setFirstname(r.getFirstname())
                                               .setLastname(r.getLastname())
                                               .setEmail(r.getEmail())
                                               .setPassword(r.getPassword());

  /** Specific {@link RecordUnmapper} to convert an {@link Account} to an {@link AccountRecord} instance. */
  private final RecordUnmapper<Account, AccountRecord> accountsUnmapper =
      source -> new AccountRecord().setId(source.getId())
                                   .setIdColorSource(source.getIdColorSource())
                                   .setFirstname(source.getFirstname())
                                   .setLastname(source.getLastname())
                                   .setEmail(source.getEmail())
                                   .setPassword(source.getPassword());

  /** Specific {@link EntityReloader} to refresh an {@link Account}, using an {@link AccountRecord} instance. */
  private final EntityReloader<AccountRecord, Account> accountsReloader =
      (source, entity) -> entity.setId(source.getId())
                                .setIdColorSource(source.getIdColorSource())
                                .setFirstname(source.getFirstname())
                                .setLastname(source.getLastname())
                                .setEmail(source.getEmail())
                                .setPassword(source.getPassword());

  // *******************************************************************************************************************
  // Record Mapping for Administrators
  // *******************************************************************************************************************
  /**
   * The {@link RecordUnmapper} that will convert an instance of {@link Account} into an instance of {@link
   * AdministratorRecord}.
   */
  private final RecordUnmapper<Account, AdministratorRecord> administratorUnmapper =
      source -> new AdministratorRecord().setIdAccount(source.getId());

  /**
   * The {@link EntityReloader} that will reload internal attributes of an instance of {@link Account}, by getting them
   * from a {@link AdministratorRecord}.
   */
  private final EntityReloader<AdministratorRecord, Account> administratorReloader =
      (source, entity) -> entity.setAdministrator(source.getIdAccount().equals(entity.getId()));

  // *******************************************************************************************************************
  // Record Mapping for Locked Accounts
  // *******************************************************************************************************************
  /**
   * The {@link RecordUnmapper} that will convert an instance of {@link Account} into an instance of {@link
   * LockedAccountRecord}.
   */
  private final RecordUnmapper<Account, LockedAccountRecord> lockedAccountUnmapper =
      source -> new LockedAccountRecord().setIdAccount(source.getId());

  /**
   * The {@link EntityReloader} that will reload internal attributes of an instance of {@link Account}, by getting them
   * from a {@link LockedAccountRecord}.
   */
  private final EntityReloader<LockedAccountRecord, Account> lockedAccountReloader =
      (source, entity) -> entity.setLocked(source.getIdAccount().equals(entity.getId()));

  // *******************************************************************************************************************
  // Record Mapping for BricklinkTokens
  // *******************************************************************************************************************
  /**
   * The {@link RecordMapper} that will convert an instance of {@link BricklinkTokensRecord} into an instance of {@link
   * BricksetTokens}.
   */
  private final RecordMapper<BricklinkTokensRecord, BricklinkTokens> bricklinkTokensMapper =
      r -> new BricklinkTokens(AccountsRepository.this).setIdAccount(r.getIdAccount())
                                                       .setConsumerKey(r.getConsumerKey())
                                                       .setConsumerSecret(r.getConsumerSecret())
                                                       .setTokenValue(r.getTokenValue())
                                                       .setTokenSecret(r.getTokenSecret())
                                                       .setValidUntil(r.getValidUntil());

  // *******************************************************************************************************************
  // Record Mapping for BricksetTokens
  // *******************************************************************************************************************
  /** Specific {@link RecordMapper} to convert a {@link BricksetTokensRecord} to a {@link BricksetTokens} instance. */
  private final RecordMapper<BricksetTokensRecord, BricksetTokens> bricksetTokensMapper =
      r -> new BricksetTokens(AccountsRepository.this).setIdAccount(r.getIdAccount())
                                                      .setApiKey(r.getApiKey())
                                                      .setUsername(r.getUsername())
                                                      .setPassword(r.getPassword());

  // *******************************************************************************************************************
  // Record Mapping for BricksetTokens
  // *******************************************************************************************************************
  /**
   * Specific {@link RecordMapper} to convert a {@link RebrickableTokensRecord} to a {@link RebrickableTokens}
   * instance.
   */
  private final RecordMapper<RebrickableTokensRecord, RebrickableTokens> rebrickableTokensMapper =
      r -> new RebrickableTokens(AccountsRepository.this).setIdAccount(r.getIdAccount())
                                                         .setKey(r.getKey())
                                                         .setValidUntil(r.getValidUntil());

  // *******************************************************************************************************************
  // Injected Attributes
  // *******************************************************************************************************************
  /** The {@link ColorSourcesRepository} to manage {@link models.ColorsSource} instances. */
  private final ColorSourcesRepository colorSourcesRepository;

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new {@link AccountsRepository} using injection.
   *
   * @param briventoryDB the {@link BriventoryDB} instance.
   * @param colorSourcesRepository the {@link ColorSourcesRepository} instance.
   */
  @Inject
  private AccountsRepository(final BriventoryDB briventoryDB, final ColorSourcesRepository colorSourcesRepository) {
    super(briventoryDB);
    this.colorSourcesRepository = colorSourcesRepository;
  }

  // *******************************************************************************************************************
  // Storage & Deletion
  // *******************************************************************************************************************
  /** {@link StorableEntityHandler} for the administrator flag. */
  private final StorableEntityHandler<ValidationError, Account, AdministratorRecord> storeAdministratorHandler =
      new StorableEntityHandler<>(administratorUnmapper, administratorReloader, ADMINISTRATOR) { };

  /** {@link DeletableEntityHandler} for the administrator flag. */
  private final DeletableEntityHandler<Account, AdministratorRecord> deleteAdministratorHandler =
      new DeletableEntityHandler<>(administratorUnmapper, ADMINISTRATOR) { };

  /** {@link StorableEntityHandler} for the administrator flag. */
  private final StorableEntityHandler<ValidationError, Account, LockedAccountRecord> storeLockedAccountHandler =
      new StorableEntityHandler<>(lockedAccountUnmapper, lockedAccountReloader, LOCKED_ACCOUNT) { };

  /** {@link DeletableEntityHandler} for the administrator flag. */
  private final DeletableEntityHandler<Account, LockedAccountRecord> deleteLockedAccountHandler =
      new DeletableEntityHandler<>(lockedAccountUnmapper, LOCKED_ACCOUNT) { };

  /** {@link StorableEntityHandler} for the {@link Account} entity. */
  private final StorableEntityHandler<ValidationError, Account, AccountRecord> storeAccountHandler =
      new StorableEntityHandler<>(accountsUnmapper, accountsReloader, ACCOUNT) {

        /** {@inheritDoc} */
        @Override
        protected List<EntityAction> getPostStoreActions(final Account account) {
          final List<EntityAction> actions = super.getPostStoreActions(account);

          final Optional<Boolean> isAdministratorOptional = account.isAdministratorOptional();
          if (isAdministratorOptional.isPresent()) {
            if (Boolean.TRUE.equals(isAdministratorOptional.get())) {
              actions.add(new StoreAction<>(storeAdministratorHandler, account));
            } else {
              actions.add(new DeleteAction<>(deleteAdministratorHandler, account));
            }
          }

          final Optional<Boolean> isLockedAccountOptional = account.isLockedOptional();
          isLockedAccountOptional.ifPresent(locked -> {
            if (Boolean.TRUE.equals(isLockedAccountOptional.get())) {
              actions.add(new StoreAction<>(storeLockedAccountHandler, account));
            } else {
              actions.add(new DeleteAction<>(deleteLockedAccountHandler, account));
            }
          });

          return actions;
        }
      };

  /** {@link DeletableEntityHandler} for the {@link Account} entity. */
  private final DeletableEntityHandler<Account, AccountRecord> deleteAccountHandler =
      new DeletableEntityHandler<>(accountsUnmapper, ACCOUNT) {
        /** {@inheritDoc} */
        @Override
        protected boolean shallDelete(final DSLContext dslContext, final Account account) {
          return super.shallDelete(dslContext, account) &&
                 !isLastActiveAdministrator(dslContext, account);

        }
      };

  /**
   * Stores the provided {@link Account} into the database.
   *
   * @param account the {@link Account to store}.
   */
  public void store(final Account account) { store(storeAccountHandler, account); }

  /**
   * Deletes the provided {@link Account} from the database.
   *
   * @param account the {@link Account} to delete.
   */
  public void delete(final Account account) { delete(deleteAccountHandler, account); }

  /**
   * Deletes the {@link List} of {@link Account} instances from the databases.
   *
   * @param accounts the {@link List} of {@link Account} instances to delete.
   */
  public void delete(final List<Account> accounts) { delete(deleteAccountHandler, accounts); }

  // *******************************************************************************************************************
  // Instance Builder
  // *******************************************************************************************************************

  /** @return a new instance of {@link Account}, that is liked to this {@link Repository}. */
  public Account buildInstance() { return new Account(this); }

  // *******************************************************************************************************************
  // Locked Users Matters
  // *******************************************************************************************************************

  /**
   * Checks into the database, if the provided {@link Account} is locked.
   *
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} is locked, otherwise {@code false}.
   */
  public boolean isLocked(final Account account) {
    final Integer count = query(dslContext -> dslContext.selectCount()
                                                        .from(LOCKED_ACCOUNT)
                                                        .where(LOCKED_ACCOUNT.ID_ACCOUNT.eq(account.getId()))
                                                        .fetchOneInto(Integer.class));
    return count > 0;
  }

  /** @return a {@link List} containing all locked accounts. */
  public List<Account> getLockedAccounts() {
    return query(dslContext ->
                     dslContext.select(ACCOUNT.asterisk())
                               .from(ACCOUNT)
                               .innerJoin(LOCKED_ACCOUNT).on(ACCOUNT.ID.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                               .coerce(ACCOUNT)
                               .fetch(accountsMapper)
    );
  }

  // *******************************************************************************************************************
  // Administrator Matters
  // *******************************************************************************************************************

  /**
   * Checks into the database if the provided {@link Account} has an administrator rights.
   *
   * @param account the {@link Account} concerned.
   *
   * @return {@code true} if the {@link Account} has administrator rights, otherwise {@code false}.
   */
  public boolean isAdministrator(final Account account) {
    final Integer count = query(dslContext -> dslContext.selectCount()
                                                        .from(ADMINISTRATOR)
                                                        .where(ADMINISTRATOR.ID_ACCOUNT.eq(account.getId()))
                                                        .fetchOneInto(Integer.class));
    return count > 0;
  }

  /** @return a {@link List} containing all administrators. */
  public List<Account> getAdministrators() {
    return query(dslContext ->
                     dslContext.select(ACCOUNT.asterisk())
                               .from(ACCOUNT)
                               .innerJoin(ADMINISTRATOR).on(ACCOUNT.ID.eq(ADMINISTRATOR.ID_ACCOUNT))
                               .coerce(ACCOUNT)
                               .fetch(accountsMapper)
    );
  }

  /**
   * Queries the database to find out if the account is an active administrator, and is the last one.
   *
   * @param account the {@link Account} to check with.
   *
   * @return {@code true} the {@link Account} is the last active administrator, otherwise {@code false}.
   */
  public boolean isLastActiveAdministrator(final Account account) {
    return query(dslContext -> isLastActiveAdministrator(dslContext, account));
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
    return query(dslContext -> {
      final Integer count = dslContext.selectCount()
                                      .from(ADMINISTRATOR)
                                      .leftJoin(LOCKED_ACCOUNT)
                                      .on(ADMINISTRATOR.ID_ACCOUNT.eq(LOCKED_ACCOUNT.ID_ACCOUNT))
                                      .where(LOCKED_ACCOUNT.ID_ACCOUNT.isNull())
                                      .fetchOneInto(Integer.class);
      if (count == null) return false;
      return count > 0;
    });
  }

  // *******************************************************************************************************************
  // Data Retrieval
  // *******************************************************************************************************************

  /** @return all {@link Account}s in the database. */
  public List<Account> getAll() { return query(dslContext -> dslContext.selectFrom(ACCOUNT).fetch(accountsMapper)); }

  /**
   * Retrieves the {@link Account} corresponding to the id provided.
   *
   * @param id the id of the {@link Account} to search for.
   *
   * @return an {@link Optional} containing the user.
   */
  public Optional<Account> findById(final long id) {
    return query(dslContext -> dslContext.selectFrom(ACCOUNT)
                                         .where(ACCOUNT.ID.eq(id))
                                         .fetchOptional(accountsMapper));
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
    return query(
        dslContext -> dslContext.selectFrom(ACCOUNT)
                                .where(ACCOUNT.EMAIL.eq(email))
                                .fetchOptional(accountsMapper)
    );
  }

  // *******************************************************************************************************************
  // Data Validation
  // *******************************************************************************************************************
  public boolean emailAlreadyExists(final String email) {
    return query(dslContext -> dslContext.fetchCount(ACCOUNT, ACCOUNT.EMAIL.equalIgnoreCase(email.trim()))) > 0;
  }

  // *******************************************************************************************************************
  // ColorSource Matters
  // *******************************************************************************************************************

  public ColorSourcesRepository getColorSourcesRepository() { return colorSourcesRepository; }

}
