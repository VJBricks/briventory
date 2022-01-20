/*
 * This file is generated by jOOQ.
 */
package jooq.tables.records;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;

import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

import jooq.tables.ColorSource;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
@Entity
@Table(
    name = "color_source",
    schema = "public"
)
public class ColorSourceRecord extends UpdatableRecordImpl<ColorSourceRecord> implements Record3<Long, String, String> {

    private static final long serialVersionUID = 1829067543;

    /**
     * Setter for <code>public.color_source.id</code>.
     */
    public ColorSourceRecord setId(@Nonnull Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.color_source.id</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, precision = 64)
    @Nonnull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.color_source.name</code>.
     */
    public ColorSourceRecord setName(@Nonnull String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.color_source.name</code>.
     */
    @Column(name = "name", nullable = false, length = 1024)
    @NotNull
    @Size(max = 1024)
    @Nonnull
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.color_source.url</code>.
     */
    public ColorSourceRecord setUrl(@Nonnull String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.color_source.url</code>.
     */
    @Column(name = "url", nullable = false, length = 4096)
    @NotNull
    @Size(max = 4096)
    @Nonnull
    public String getUrl() {
        return (String) get(2);
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
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @Nonnull
    public Row3<Long, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @Nonnull
    public Field<Long> field1() {
        return ColorSource.COLOR_SOURCE.ID;
    }

    @Override
    @Nonnull
    public Field<String> field2() {
        return ColorSource.COLOR_SOURCE.NAME;
    }

    @Override
    @Nonnull
    public Field<String> field3() {
        return ColorSource.COLOR_SOURCE.URL;
    }

    @Override
    @Nonnull
    public Long component1() {
        return getId();
    }

    @Override
    @Nonnull
    public String component2() {
        return getName();
    }

    @Override
    @Nonnull
    public String component3() {
        return getUrl();
    }

    @Override
    @Nonnull
    public Long value1() {
        return getId();
    }

    @Override
    @Nonnull
    public String value2() {
        return getName();
    }

    @Override
    @Nonnull
    public String value3() {
        return getUrl();
    }

    @Override
    @Nonnull
    public ColorSourceRecord value1(@Nonnull Long value) {
        setId(value);
        return this;
    }

    @Override
    @Nonnull
    public ColorSourceRecord value2(@Nonnull String value) {
        setName(value);
        return this;
    }

    @Override
    @Nonnull
    public ColorSourceRecord value3(@Nonnull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @Nonnull
    public ColorSourceRecord values(@Nonnull Long value1, @Nonnull String value2, @Nonnull String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ColorSourceRecord
     */
    public ColorSourceRecord() {
        super(ColorSource.COLOR_SOURCE);
    }

    /**
     * Create a detached, initialised ColorSourceRecord
     */
    @ConstructorProperties({ "id", "name", "url" })
    public ColorSourceRecord(@Nonnull Long id, @Nonnull String name, @Nonnull String url) {
        super(ColorSource.COLOR_SOURCE);

        setId(id);
        setName(name);
        setUrl(url);
    }
}
