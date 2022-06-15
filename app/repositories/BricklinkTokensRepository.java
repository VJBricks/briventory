package repositories;

import database.BriventoryDB;
import jooq.tables.records.BricklinkTokensRecord;
import models.Account;
import models.BricklinkTokens;
import org.jooq.DSLContext;
import orm.DeleteRecordAction;
import orm.ModelAction;
import orm.OptionalModelLoader;
import orm.PersistAction1;
import orm.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Optional;

import static jooq.Tables.BRICKLINK_TOKENS;

/** The {@code BricklinkTokensRepository} handler the BrickLink tokens, needed to sync an inventory from BrickLink. */
@Singleton
public final class BricklinkTokensRepository extends Repository<BricklinkTokens> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BricklinkTokensRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB} injected instance.
   */
  @Inject
  public BricklinkTokensRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Matters
  // *******************************************************************************************************************

  /**
   * Creates a new {@link OptionalModelLoader} to load {@link BricklinkTokens} lazily.
   *
   * @param account the associated {@link Account}.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  public OptionalModelLoader<Account, BricklinkTokens> createBricklinkTokensLoader(final Account account) {
    return createOptionalModelLoader(account,
                                     this::findByAccount,
                                     (dslContext, a, optionalBricklinkTokens) -> {
                                       ModelAction modelAction;
                                       if (optionalBricklinkTokens.isPresent()) {
                                         modelAction = new PersistAction1<>(optionalBricklinkTokens.get());
                                       } else {
                                         modelAction = new DeleteRecordAction<>(
                                             new BricklinkTokensRecord().setIdAccount(a.getId()));
                                       }
                                       return Collections.singletonList(modelAction);
                                     });
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /**
   * Retrieves from the database the {@link BricklinkTokens} linked with the {@link Account} provided.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account}.
   *
   * @return an {@link Optional} instance, containing the existing {@link BricklinkTokens}.
   */
  private Optional<BricklinkTokens> findByAccount(final DSLContext dslContext, final Account account) {
    return fetchOptional(BricklinkTokens.BRICKLINK_TOKENS_MAPPER,
                         dslContext,
                         ctx -> ctx.selectFrom(BRICKLINK_TOKENS)
                                   .where(BRICKLINK_TOKENS.ID_ACCOUNT.eq(account.getId())));
  }

}
