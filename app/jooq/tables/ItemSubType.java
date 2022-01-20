/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.ItemSubTypeRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class ItemSubType extends TableImpl<ItemSubTypeRecord> {

    private static final long serialVersionUID = -274006803;

    /**
     * The reference instance of <code>public.item_sub_type</code>
     */
    public static final ItemSubType ITEM_SUB_TYPE = new ItemSubType();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<ItemSubTypeRecord> getRecordType() {
        return ItemSubTypeRecord.class;
    }

    /**
     * The column <code>public.item_sub_type.id_base_item_type</code>.
     */
    public final TableField<ItemSubTypeRecord, Long> ID_BASE_ITEM_TYPE = createField(DSL.name("id_base_item_type"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.item_sub_type.id_sub_item_type</code>.
     */
    public final TableField<ItemSubTypeRecord, Long> ID_SUB_ITEM_TYPE = createField(DSL.name("id_sub_item_type"), SQLDataType.BIGINT.nullable(false), this, "");

    private ItemSubType(Name alias, Table<ItemSubTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private ItemSubType(Name alias, Table<ItemSubTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.item_sub_type</code> table reference
     */
    public ItemSubType(String alias) {
        this(DSL.name(alias), ITEM_SUB_TYPE);
    }

    /**
     * Create an aliased <code>public.item_sub_type</code> table reference
     */
    public ItemSubType(Name alias) {
        this(alias, ITEM_SUB_TYPE);
    }

    /**
     * Create a <code>public.item_sub_type</code> table reference
     */
    public ItemSubType() {
        this(DSL.name("item_sub_type"), null);
    }

    public <O extends Record> ItemSubType(Table<O> child, ForeignKey<O, ItemSubTypeRecord> key) {
        super(child, key, ITEM_SUB_TYPE);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<ItemSubTypeRecord> getPrimaryKey() {
        return Keys.PK_ITEM_SUB_TYPE;
    }

    @Override
    @Nonnull
    public ItemSubType as(String alias) {
        return new ItemSubType(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public ItemSubType as(Name alias) {
        return new ItemSubType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ItemSubType rename(String name) {
        return new ItemSubType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ItemSubType rename(Name name) {
        return new ItemSubType(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
