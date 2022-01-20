/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.ColorSourceRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
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
public class ColorSource extends TableImpl<ColorSourceRecord> {

    private static final long serialVersionUID = -408879736;

    /**
     * The reference instance of <code>public.color_source</code>
     */
    public static final ColorSource COLOR_SOURCE = new ColorSource();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<ColorSourceRecord> getRecordType() {
        return ColorSourceRecord.class;
    }

    /**
     * The column <code>public.color_source.id</code>.
     */
    public final TableField<ColorSourceRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.color_source.name</code>.
     */
    public final TableField<ColorSourceRecord, String> NAME = createField(DSL.name("name"), jooq.Domains.DOMAIN_NAME.getDataType(), this, "");

    /**
     * The column <code>public.color_source.url</code>.
     */
    public final TableField<ColorSourceRecord, String> URL = createField(DSL.name("url"), jooq.Domains.DOMAIN_URL.getDataType(), this, "");

    private ColorSource(Name alias, Table<ColorSourceRecord> aliased) {
        this(alias, aliased, null);
    }

    private ColorSource(Name alias, Table<ColorSourceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.color_source</code> table reference
     */
    public ColorSource(String alias) {
        this(DSL.name(alias), COLOR_SOURCE);
    }

    /**
     * Create an aliased <code>public.color_source</code> table reference
     */
    public ColorSource(Name alias) {
        this(alias, COLOR_SOURCE);
    }

    /**
     * Create a <code>public.color_source</code> table reference
     */
    public ColorSource() {
        this(DSL.name("color_source"), null);
    }

    public <O extends Record> ColorSource(Table<O> child, ForeignKey<O, ColorSourceRecord> key) {
        super(child, key, COLOR_SOURCE);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public Identity<ColorSourceRecord, Long> getIdentity() {
        return (Identity<ColorSourceRecord, Long>) super.getIdentity();
    }

    @Override
    @Nonnull
    public UniqueKey<ColorSourceRecord> getPrimaryKey() {
        return Keys.PKEY_COLOR_SOURCE;
    }

    @Override
    @Nonnull
    public ColorSource as(String alias) {
        return new ColorSource(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public ColorSource as(Name alias) {
        return new ColorSource(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ColorSource rename(String name) {
        return new ColorSource(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ColorSource rename(Name name) {
        return new ColorSource(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
