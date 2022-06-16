/*
 * This file is generated by jOOQ.
 */
package jooq.tables;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.RebrickableTokensRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
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
import java.time.LocalDate;
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
public class RebrickableTokens extends TableImpl<RebrickableTokensRecord> {

    private static final long serialVersionUID = 39928695;

    /**
     * The reference instance of <code>public.rebrickable_tokens</code>
     */
    public static final RebrickableTokens REBRICKABLE_TOKENS = new RebrickableTokens();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<RebrickableTokensRecord> getRecordType() {
        return RebrickableTokensRecord.class;
    }

    /**
     * The column <code>public.rebrickable_tokens.id_account</code>.
     */
    public final TableField<RebrickableTokensRecord, Long> ID_ACCOUNT = createField(DSL.name("id_account"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.rebrickable_tokens.key</code>.
     */
    public final TableField<RebrickableTokensRecord, String> KEY = createField(DSL.name("key"), jooq.Domains.DOMAIN_TOKEN.getDataType(), this, "");

    /**
     * The column <code>public.rebrickable_tokens.valid_until</code>.
     */
    public final TableField<RebrickableTokensRecord, LocalDate> VALID_UNTIL = createField(DSL.name("valid_until"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field("(now() + '1 year'::interval)", SQLDataType.LOCALDATE)), this, "");

    private RebrickableTokens(Name alias, Table<RebrickableTokensRecord> aliased) {
        this(alias, aliased, null);
    }

    private RebrickableTokens(Name alias, Table<RebrickableTokensRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.rebrickable_tokens</code> table reference
     */
    public RebrickableTokens(String alias) {
        this(DSL.name(alias), REBRICKABLE_TOKENS);
    }

    /**
     * Create an aliased <code>public.rebrickable_tokens</code> table reference
     */
    public RebrickableTokens(Name alias) {
        this(alias, REBRICKABLE_TOKENS);
    }

    /**
     * Create a <code>public.rebrickable_tokens</code> table reference
     */
    public RebrickableTokens() {
        this(DSL.name("rebrickable_tokens"), null);
    }

    public <O extends Record> RebrickableTokens(Table<O> child, ForeignKey<O, RebrickableTokensRecord> key) {
        super(child, key, REBRICKABLE_TOKENS);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<RebrickableTokensRecord> getPrimaryKey() {
        return Keys.PK_REBRICKABLE_TOKENS;
    }

    @Override
    @Nonnull
    public List<ForeignKey<RebrickableTokensRecord, ?>> getReferences() {
        return Arrays.asList(Keys.REBRICKABLE_TOKENS__FK_REBRICKABLE_TOKENS_TO_ACCOUNT);
    }

    private transient Account _account;

    /**
     * Get the implicit join path to the <code>public.account</code> table.
     */
    public Account account() {
        if (_account == null)
            _account = new Account(this, Keys.REBRICKABLE_TOKENS__FK_REBRICKABLE_TOKENS_TO_ACCOUNT);

        return _account;
    }

    @Override
    @Nonnull
    public RebrickableTokens as(String alias) {
        return new RebrickableTokens(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public RebrickableTokens as(Name alias) {
        return new RebrickableTokens(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public RebrickableTokens rename(String name) {
        return new RebrickableTokens(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public RebrickableTokens rename(Name name) {
        return new RebrickableTokens(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row3<Long, String, LocalDate> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
