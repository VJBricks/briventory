package repositories;

import database.BriventoryDB;
import jooq.tables.records.BricklinkTokensRecord;
import models.Account;
import models.BrickLinkTokens;
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

/** The {@code BrickLinkTokensRepository} handler the BrickLink tokens, needed to sync an inventory from BrickLink. */
@Singleton
public final class BrickLinkTokensRepository extends Repository<BrickLinkTokens> {

  // *******************************************************************************************************************
  // Construction & Initialization
  // *******************************************************************************************************************

  /**
   * Creates a new instance of {@link BrickLinkTokensRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB} injected instance.
   */
  @Inject
  public BrickLinkTokensRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  // *******************************************************************************************************************
  // Lazy Loaders Matters
  // *******************************************************************************************************************

  /**
   * Creates a new {@link OptionalModelLoader} to load {@link BrickLinkTokens} lazily.
   *
   * @param account the associated {@link Account}.
   *
   * @return an instance of {@link OptionalModelLoader}.
   */
  public OptionalModelLoader<Account, BrickLinkTokens> createBrickLinkTokensLoader(final Account account) {
    return createOptionalModelLoader(account,
                                     this::findByAccount,
                                     (dslContext, a, optionalBrickLinkTokens) -> {
                                       ModelAction modelAction;
                                       if (optionalBrickLinkTokens.isPresent()) {
                                         modelAction = new PersistAction1<>(optionalBrickLinkTokens.get());
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
   * Retrieves from the database the {@link BrickLinkTokens} linked with the {@link Account} provided.
   *
   * @param dslContext the {@link DSLContext}.
   * @param account the {@link Account}.
   *
   * @return an {@link Optional} instance, containing the existing {@link BrickLinkTokens}.
   */
  private Optional<BrickLinkTokens> findByAccount(final DSLContext dslContext, final Account account) {
    return fetchOptional(BrickLinkTokens.BRICKLINK_TOKENS_MAPPER,
                         dslContext,
                         ctx -> ctx.selectFrom(BRICKLINK_TOKENS)
                                   .where(BRICKLINK_TOKENS.ID_ACCOUNT.eq(account.getId())));
  }

}
