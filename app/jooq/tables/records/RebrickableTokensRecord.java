/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jooq.tables.RebrickableTokens;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;
import java.beans.ConstructorProperties;
import java.time.LocalDate;


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
@Entity
@Table(
    name = "rebrickable_tokens",
    schema = "public"
)
public class RebrickableTokensRecord extends UpdatableRecordImpl<RebrickableTokensRecord> implements Record3<Long, String, LocalDate> {

    private static final long serialVersionUID = -1536174836;

    /**
     * Setter for <code>public.rebrickable_tokens.id_account</code>.
     */
    public RebrickableTokensRecord setIdAccount(@Nonnull Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.rebrickable_tokens.id_account</code>.
     */
    @Id
    @Column(name = "id_account", nullable = false, precision = 64)
    @NotNull
    @Nonnull
    public Long getIdAccount() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.rebrickable_tokens.key</code>.
     */
    public RebrickableTokensRecord setKey(@Nonnull String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.rebrickable_tokens.key</code>.
     */
    @Column(name = "key", nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    @Nonnull
    public String getKey() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.rebrickable_tokens.valid_until</code>.
     */
    public RebrickableTokensRecord setValidUntil(@Nonnull LocalDate value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.rebrickable_tokens.valid_until</code>.
     */
    @Column(name = "valid_until", nullable = false)
    @Nonnull
    public LocalDate getValidUntil() {
        return (LocalDate) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row3<Long, String, LocalDate> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @Nonnull
    public Row3<Long, String, LocalDate> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @Nonnull
    public Field<Long> field1() {
        return RebrickableTokens.REBRICKABLE_TOKENS.ID_ACCOUNT;
    }

    @Override
    @Nonnull
    public Field<String> field2() {
        return RebrickableTokens.REBRICKABLE_TOKENS.KEY;
    }

    @Override
    @Nonnull
    public Field<LocalDate> field3() {
        return RebrickableTokens.REBRICKABLE_TOKENS.VALID_UNTIL;
    }

    @Override
    @Nonnull
    public Long component1() {
        return getIdAccount();
    }

    @Override
    @Nonnull
    public String component2() {
        return getKey();
    }

    @Override
    @Nonnull
    public LocalDate component3() {
        return getValidUntil();
    }

    @Override
    @Nonnull
    public Long value1() {
        return getIdAccount();
    }

    @Override
    @Nonnull
    public String value2() {
        return getKey();
    }

    @Override
    @Nonnull
    public LocalDate value3() {
        return getValidUntil();
    }

    @Override
    @Nonnull
    public RebrickableTokensRecord value1(@Nonnull Long value) {
        setIdAccount(value);
        return this;
    }

    @Override
    @Nonnull
    public RebrickableTokensRecord value2(@Nonnull String value) {
        setKey(value);
        return this;
    }

    @Override
    @Nonnull
    public RebrickableTokensRecord value3(@Nonnull LocalDate value) {
        setValidUntil(value);
        return this;
    }

    @Override
    @Nonnull
    public RebrickableTokensRecord values(@Nonnull Long value1, @Nonnull String value2, @Nonnull LocalDate value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RebrickableTokensRecord
     */
    public RebrickableTokensRecord() {
        super(RebrickableTokens.REBRICKABLE_TOKENS);
    }

    /**
     * Create a detached, initialised RebrickableTokensRecord
     */
    @ConstructorProperties({ "idAccount", "key", "validUntil" })
    public RebrickableTokensRecord(@Nonnull Long idAccount, @Nonnull String key, @Nonnull LocalDate validUntil) {
        super(RebrickableTokens.REBRICKABLE_TOKENS);

        setIdAccount(idAccount);
        setKey(key);
        setValidUntil(validUntil);
    }
}
