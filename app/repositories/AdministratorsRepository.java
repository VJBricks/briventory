package repositories;

import database.BriventoryDB;
import models.Account;
import models.Administrator;
import orm.Repository;

import javax.inject.Inject;

import static jooq.tables.Administrator.ADMINISTRATOR;

public final class AdministratorsRepository extends Repository<Administrator> {

  /**
   * Creates a new instance of {@link AdministratorsRepository}.
   *
   * @param briventoryDB the {@link BriventoryDB}.
   */
  @Inject
  protected AdministratorsRepository(final BriventoryDB briventoryDB) {
    super(briventoryDB);
  }

  public Administrator findByAccount(final Account account) {
    return fetchOne(Administrator.ADMINISTRATOR_MAPPER,
                    dslContext -> dslContext.selectFrom(ADMINISTRATOR)
                                            .where(ADMINISTRATOR.ID_ACCOUNT.eq(account.getId())));
  }

}
