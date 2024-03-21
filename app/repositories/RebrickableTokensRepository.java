package repositories;

import database.BriventoryDB;
import jooq.tables.records.RebrickableTokensRecord;
import models.Account;
import models.RebrickableTokens;
import org.jooq.DSLContext;
import orm.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Optional;

import static jooq.Tables.REBRICKABLE_TOKENS;
import static models.RebrickableTokens.REBRICKABLE_TOKENS_MAPPER;

/**
 * The {@code RebrickableTokensRepository} handler the Rebrickable tokens, needed to sync an inventory from
 * Rebrickable.
 */
@Singleton
public final class RebrickableTokensRepository extends Repository<RebrickableTokens> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link RebrickableTokensRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB} injected instance.
   */
  @Inject
  public RebrickableTokensRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Matters
  // *******************************************************************************************************************

  /**
   * Creates a new {@link OptionalModelLoader} to load {@link RebrickableTokens} lazily.
   *
   * @param account the associated {@link Account}.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  public OptionalModelLoader<Account, RebrickableTokens> createRebrickableLoader(final Account account) {
    return createOptionalModelLoader(account,
                                     this::findByAccount,
                                     (dslContext, a, optionalRebrickableTokens) -> {
                                       Action action;
                                       if (optionalRebrickableTokens.isPresent()) {
                                         action = new PersistAction1<>(this, optionalRebrickableTokens.get());
                                       } else {
                                         final RebrickableTokensRecord rebrickableTokensRecord =
                                             dslContext.newRecord(REBRICKABLE_TOKENS).setIdAccount(a.getId());
                                         action = new DeleteRecordAction<>(rebrickableTokensRecord);
                                       }
                                       return Collections.singletonList(action);
                                     });
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /**
   * Retrieves from the database the {@link RebrickableTokens} linked with the {@link Account} provided.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account}.
   *
   * @return an {@link Optional} instance, containing the existing {@link RebrickableTokens}.
   */
  private Optional<RebrickableTokens> findByAccount(final DSLContext dslContext, final Account account) {
    return fetchOptional(REBRICKABLE_TOKENS_MAPPER,
                         dslContext,
                         ctx -> ctx.selectFrom(REBRICKABLE_TOKENS)
                                   .where(REBRICKABLE_TOKENS.ID_ACCOUNT.eq(account.getId())));
  }

}
