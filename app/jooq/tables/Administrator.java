/*
 * This file is generated by jOOQ.
 */
package jooq.tables;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.AdministratorRecord;
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
public class Administrator extends TableImpl<AdministratorRecord> {

    private static final long serialVersionUID = 1744902605;

    /**
     * The reference instance of <code>public.administrator</code>
     */
    public static final Administrator ADMINISTRATOR = new Administrator();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<AdministratorRecord> getRecordType() {
        return AdministratorRecord.class;
    }

    /**
     * The column <code>public.administrator.id_account</code>.
     */
    public final TableField<AdministratorRecord, Long> ID_ACCOUNT = createField(DSL.name("id_account"), SQLDataType.BIGINT.nullable(false), this, "");

    private Administrator(Name alias, Table<AdministratorRecord> aliased) {
        this(alias, aliased, null);
    }

    private Administrator(Name alias, Table<AdministratorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.administrator</code> table reference
     */
    public Administrator(String alias) {
        this(DSL.name(alias), ADMINISTRATOR);
    }

    /**
     * Create an aliased <code>public.administrator</code> table reference
     */
    public Administrator(Name alias) {
        this(alias, ADMINISTRATOR);
    }

    /**
     * Create a <code>public.administrator</code> table reference
     */
    public Administrator() {
        this(DSL.name("administrator"), null);
    }

    public <O extends Record> Administrator(Table<O> child, ForeignKey<O, AdministratorRecord> key) {
        super(child, key, ADMINISTRATOR);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<AdministratorRecord> getPrimaryKey() {
        return Keys.PK_ADMINISTRATOR;
    }

    @Override
    @Nonnull
    public List<ForeignKey<AdministratorRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ADMINISTRATOR__FK_ADMINISTRATOR_TO_ACCOUNT);
    }

    private transient Account _account;

    /**
     * Get the implicit join path to the <code>public.account</code> table.
     */
    public Account account() {
        if (_account == null)
            _account = new Account(this, Keys.ADMINISTRATOR__FK_ADMINISTRATOR_TO_ACCOUNT);

        return _account;
    }

    @Override
    @Nonnull
    public Administrator as(String alias) {
        return new Administrator(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public Administrator as(Name alias) {
        return new Administrator(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Administrator rename(String name) {
        return new Administrator(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Administrator rename(Name name) {
        return new Administrator(name, null);
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
