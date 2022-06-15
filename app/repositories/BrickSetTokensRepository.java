package repositories;

import database.BriventoryDB;
import jooq.tables.records.BricksetTokensRecord;
import models.Account;
import models.BrickSetTokens;
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

import static jooq.Tables.BRICKSET_TOKENS;
import static models.BrickSetTokens.BRICKSET_TOKENS_MAPPER;

/** The {@code BrickSetTokensRepository} handler the BrickSet tokens, needed to sync an inventory from BrickSet. */
@Singleton
public final class BrickSetTokensRepository extends Repository<BrickSetTokens> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BrickSetTokensRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB} injected instance.
   */
  @Inject
  public BrickSetTokensRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Matters
  // *******************************************************************************************************************

  /**
   * Creates a new {@link OptionalModelLoader} to load {@link BrickSetTokens} lazily.
   *
   * @param account the associated {@link Account}.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  public OptionalModelLoader<Account, BrickSetTokens> createBrickSetTokensLoader(final Account account) {
    return createOptionalModelLoader(account,
                                     this::findByAccount,
                                     (dslContext, a, optionalBrickSetTokens) -> {
                                       ModelAction modelAction;
                                       if (optionalBrickSetTokens.isPresent()) {
                                         modelAction = new PersistAction1<>(optionalBrickSetTokens.get());
                                       } else {
                                         modelAction = new DeleteRecordAction<>(
                                             new BricksetTokensRecord().setIdAccount(a.getId()));
                                       }
                                       return Collections.singletonList(modelAction);
                                     });
  }

  // *******************************************************************************************************************
  // General Data Retrieval
  // *******************************************************************************************************************

  /**
   * Retrieves from the database the {@link BrickSetTokens} linked with the {@link Account} provided.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account}.
   *
   * @return an {@link Optional} instance, containing the existing {@link BrickSetTokens}.
   */
  private Optional<BrickSetTokens> findByAccount(final DSLContext dslContext, final Account account) {
    return fetchOptional(BRICKSET_TOKENS_MAPPER,
                         dslContext,
                         ctx -> ctx.selectFrom(BRICKSET_TOKENS)
                                   .where(BRICKSET_TOKENS.ID_ACCOUNT.eq(account.getId())));
  }

}
