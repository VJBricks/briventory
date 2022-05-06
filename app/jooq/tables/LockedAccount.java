/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.LockedAccountRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row1;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.16.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LockedAccount extends TableImpl<LockedAccountRecord> {

    private static final long serialVersionUID = 548246686;

    /**
     * The reference instance of <code>public.locked_account</code>
     */
    public static final LockedAccount LOCKED_ACCOUNT = new LockedAccount();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<LockedAccountRecord> getRecordType() {
        return LockedAccountRecord.class;
    }

    /**
     * The column <code>public.locked_account.id_account</code>.
     */
    public final TableField<LockedAccountRecord, Long> ID_ACCOUNT = createField(DSL.name("id_account"), SQLDataType.BIGINT.nullable(false), this, "");

    private LockedAccount(Name alias, Table<LockedAccountRecord> aliased) {
        this(alias, aliased, null);
    }

    private LockedAccount(Name alias, Table<LockedAccountRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.locked_account</code> table reference
     */
    public LockedAccount(String alias) {
        this(DSL.name(alias), LOCKED_ACCOUNT);
    }

    /**
     * Create an aliased <code>public.locked_account</code> table reference
     */
    public LockedAccount(Name alias) {
        this(alias, LOCKED_ACCOUNT);
    }

    /**
     * Create a <code>public.locked_account</code> table reference
     */
    public LockedAccount() {
        this(DSL.name("locked_account"), null);
    }

    public <O extends Record> LockedAccount(Table<O> child, ForeignKey<O, LockedAccountRecord> key) {
        super(child, key, LOCKED_ACCOUNT);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<LockedAccountRecord> getPrimaryKey() {
        return Keys.PK_LOCKED_ACCOUNT;
    }

    @Override
    @Nonnull
    public List<ForeignKey<LockedAccountRecord, ?>> getReferences() {
        return Arrays.asList(Keys.LOCKED_ACCOUNT__FK_LOCKED_ACCOUNT_TO_ACCOUNT);
    }

    private transient Account _account;

    /**
     * Get the implicit join path to the <code>public.account</code> table.
     */
    public Account account() {
        if (_account == null)
            _account = new Account(this, Keys.LOCKED_ACCOUNT__FK_LOCKED_ACCOUNT_TO_ACCOUNT);

        return _account;
    }

    @Override
    @Nonnull
    public LockedAccount as(String alias) {
        return new LockedAccount(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public LockedAccount as(Name alias) {
        return new LockedAccount(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public LockedAccount rename(String name) {
        return new LockedAccount(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public LockedAccount rename(Name name) {
        return new LockedAccount(name, null);
    }

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row1<Long> fieldsRow() {
        return (Row1) super.fieldsRow();
    }
}