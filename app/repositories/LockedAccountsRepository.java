package repositories;

import database.BriventoryDB;
import models.Account;
import models.LockedAccount;
import orm.Repository;

import javax.inject.Inject;

import static jooq.tables.LockedAccount.LOCKED_ACCOUNT;

public final class LockedAccountsRepository extends Repository<LockedAccount> {

  /**
   * Creates a new instance of {@link LockedAccountsRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB}.
   */
  @Inject
  protected LockedAccountsRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  public LockedAccount findByAccount(final Account account) {
    return fetchOne(LockedAccount.LOCKED_ACCOUNT_MAPPER,
                    dslContext -> dslContext.selectFrom(LOCKED_ACCOUNT)
                                            .where(LOCKED_ACCOUNT.ID_ACCOUNT.eq(account.getId())));
  }

}
