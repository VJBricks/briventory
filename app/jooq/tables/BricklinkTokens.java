/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.BricklinkTokensRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class BricklinkTokens extends TableImpl<BricklinkTokensRecord> {

    private static final long serialVersionUID = -1653945127;

    /**
     * The reference instance of <code>public.bricklink_tokens</code>
     */
    public static final BricklinkTokens BRICKLINK_TOKENS = new BricklinkTokens();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<BricklinkTokensRecord> getRecordType() {
        return BricklinkTokensRecord.class;
    }

    /**
     * The column <code>public.bricklink_tokens.id_account</code>.
     */
    public final TableField<BricklinkTokensRecord, Long> ID_ACCOUNT = createField(DSL.name("id_account"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.bricklink_tokens.consumer_key</code>.
     */
    public final TableField<BricklinkTokensRecord, String> CONSUMER_KEY = createField(DSL.name("consumer_key"), jooq.Domains.DOMAIN_TOKEN.getDataType(), this, "");

    /**
     * The column <code>public.bricklink_tokens.consumer_secret</code>.
     */
    public final TableField<BricklinkTokensRecord, String> CONSUMER_SECRET = createField(DSL.name("consumer_secret"), jooq.Domains.DOMAIN_TOKEN.getDataType(), this, "");

    /**
     * The column <code>public.bricklink_tokens.token_value</code>.
     */
    public final TableField<BricklinkTokensRecord, String> TOKEN_VALUE = createField(DSL.name("token_value"), jooq.Domains.DOMAIN_TOKEN.getDataType(), this, "");

    /**
     * The column <code>public.bricklink_tokens.token_secret</code>.
     */
    public final TableField<BricklinkTokensRecord, String> TOKEN_SECRET = createField(DSL.name("token_secret"), jooq.Domains.DOMAIN_TOKEN.getDataType(), this, "");

    /**
     * The column <code>public.bricklink_tokens.valid_until</code>.
     */
    public final TableField<BricklinkTokensRecord, LocalDate> VALID_UNTIL = createField(DSL.name("valid_until"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field("(now() + '1 year'::interval)", SQLDataType.LOCALDATE)), this, "");

    private BricklinkTokens(Name alias, Table<BricklinkTokensRecord> aliased) {
        this(alias, aliased, null);
    }

    private BricklinkTokens(Name alias, Table<BricklinkTokensRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.bricklink_tokens</code> table reference
     */
    public BricklinkTokens(String alias) {
        this(DSL.name(alias), BRICKLINK_TOKENS);
    }

    /**
     * Create an aliased <code>public.bricklink_tokens</code> table reference
     */
    public BricklinkTokens(Name alias) {
        this(alias, BRICKLINK_TOKENS);
    }

    /**
     * Create a <code>public.bricklink_tokens</code> table reference
     */
    public BricklinkTokens() {
        this(DSL.name("bricklink_tokens"), null);
    }

    public <O extends Record> BricklinkTokens(Table<O> child, ForeignKey<O, BricklinkTokensRecord> key) {
        super(child, key, BRICKLINK_TOKENS);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<BricklinkTokensRecord> getPrimaryKey() {
        return Keys.PK_BRICKLINK_TOKENS;
    }

    @Override
    @Nonnull
    public List<ForeignKey<BricklinkTokensRecord, ?>> getReferences() {
        return Arrays.asList(Keys.BRICKLINK_TOKENS__FK_BRICKLINK_TOKENS_TO_ACCOUNT);
    }

    private transient Account _account;

    /**
     * Get the implicit join path to the <code>public.account</code> table.
     */
    public Account account() {
        if (_account == null)
            _account = new Account(this, Keys.BRICKLINK_TOKENS__FK_BRICKLINK_TOKENS_TO_ACCOUNT);

        return _account;
    }

    @Override
    @Nonnull
    public BricklinkTokens as(String alias) {
        return new BricklinkTokens(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public BricklinkTokens as(Name alias) {
        return new BricklinkTokens(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public BricklinkTokens rename(String name) {
        return new BricklinkTokens(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public BricklinkTokens rename(Name name) {
        return new BricklinkTokens(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row6<Long, String, String, String, String, LocalDate> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}