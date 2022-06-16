/*
 * This file is generated by jOOQ.
 */
package jooq.tables;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.AccountRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.16.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Account extends TableImpl<AccountRecord> {

    private static final long serialVersionUID = 1313989777;

    /**
     * The reference instance of <code>public.account</code>
     */
    public static final Account ACCOUNT = new Account();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<AccountRecord> getRecordType() {
        return AccountRecord.class;
    }

    /**
     * The column <code>public.account.id</code>.
     */
    public final TableField<AccountRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.account.id_color_source</code>.
     */
    public final TableField<AccountRecord, Long> ID_COLOR_SOURCE = createField(DSL.name("id_color_source"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.account.firstname</code>.
     */
    public final TableField<AccountRecord, String> FIRSTNAME = createField(DSL.name("firstname"), jooq.Domains.DOMAIN_NAME.getDataType(), this, "");

    /**
     * The column <code>public.account.lastname</code>.
     */
    public final TableField<AccountRecord, String> LASTNAME = createField(DSL.name("lastname"), jooq.Domains.DOMAIN_NAME.getDataType(), this, "");

    /**
     * The column <code>public.account.email</code>.
     */
    public final TableField<AccountRecord, String> EMAIL = createField(DSL.name("email"), jooq.Domains.DOMAIN_EMAIL.getDataType(), this, "");

    /**
     * The column <code>public.account.password</code>.
     */
    public final TableField<AccountRecord, String> PASSWORD = createField(DSL.name("password"), jooq.Domains.DOMAIN_PASSWORD.getDataType(), this, "");

    private Account(Name alias, Table<AccountRecord> aliased) {
        this(alias, aliased, null);
    }

    private Account(Name alias, Table<AccountRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(String alias) {
        this(DSL.name(alias), ACCOUNT);
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(Name alias) {
        this(alias, ACCOUNT);
    }

    /**
     * Create a <code>public.account</code> table reference
     */
    public Account() {
        this(DSL.name("account"), null);
    }

    public <O extends Record> Account(Table<O> child, ForeignKey<O, AccountRecord> key) {
        super(child, key, ACCOUNT);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public Identity<AccountRecord, Long> getIdentity() {
        return (Identity<AccountRecord, Long>) super.getIdentity();
    }

    @Override
    @Nonnull
    public UniqueKey<AccountRecord> getPrimaryKey() {
        return Keys.PK_ACCOUNT;
    }

    @Override
    @Nonnull
    public List<UniqueKey<AccountRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_ACCOUNT_EMAIL);
    }

    @Override
    @Nonnull
    public List<ForeignKey<AccountRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ACCOUNT__FK_ACCOUNT_TO_COLOR_SOURCE);
    }

    private transient ColorSource _colorSource;

    /**
     * Get the implicit join path to the <code>public.color_source</code> table.
     */
    public ColorSource colorSource() {
        if (_colorSource == null)
            _colorSource = new ColorSource(this, Keys.ACCOUNT__FK_ACCOUNT_TO_COLOR_SOURCE);

        return _colorSource;
    }

    @Override
    @Nonnull
    public Account as(String alias) {
        return new Account(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public Account as(Name alias) {
        return new Account(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Account rename(String name) {
        return new Account(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Account rename(Name name) {
        return new Account(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row6<Long, Long, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
