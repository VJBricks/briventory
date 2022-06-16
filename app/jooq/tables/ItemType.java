/*
 * This file is generated by jOOQ.
 */
package jooq.tables;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.ItemTypeRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
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
 * item_type is considered as a table of constants, there is not sequence.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.16.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ItemType extends TableImpl<ItemTypeRecord> {

    private static final long serialVersionUID = -391904496;

    /**
     * The reference instance of <code>public.item_type</code>
     */
    public static final ItemType ITEM_TYPE = new ItemType();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<ItemTypeRecord> getRecordType() {
        return ItemTypeRecord.class;
    }

    /**
     * The column <code>public.item_type.id</code>.
     */
    public final TableField<ItemTypeRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.item_type.name</code>.
     */
    public final TableField<ItemTypeRecord, String> NAME = createField(DSL.name("name"), jooq.Domains.DOMAIN_NAME.getDataType(), this, "");

    /**
     * The column <code>public.item_type.code</code>.
     */
    public final TableField<ItemTypeRecord, String> CODE = createField(DSL.name("code"), SQLDataType.CHAR(1).nullable(false), this, "");

    /**
     * The column <code>public.item_type.dimension_in_stud</code>.
     */
    public final TableField<ItemTypeRecord, Boolean> DIMENSION_IN_STUD = createField(DSL.name("dimension_in_stud"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("true", SQLDataType.BOOLEAN)), this, "");

    private ItemType(Name alias, Table<ItemTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private ItemType(Name alias, Table<ItemTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("item_type is considered as a table of constants, there is not sequence."), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.item_type</code> table reference
     */
    public ItemType(String alias) {
        this(DSL.name(alias), ITEM_TYPE);
    }

    /**
     * Create an aliased <code>public.item_type</code> table reference
     */
    public ItemType(Name alias) {
        this(alias, ITEM_TYPE);
    }

    /**
     * Create a <code>public.item_type</code> table reference
     */
    public ItemType() {
        this(DSL.name("item_type"), null);
    }

    public <O extends Record> ItemType(Table<O> child, ForeignKey<O, ItemTypeRecord> key) {
        super(child, key, ITEM_TYPE);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<ItemTypeRecord> getPrimaryKey() {
        return Keys.PK_ITEM_TYPE;
    }

    @Override
    @Nonnull
    public List<UniqueKey<ItemTypeRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_ITEM_TYPE_ON_CODE);
    }

    @Override
    @Nonnull
    public ItemType as(String alias) {
        return new ItemType(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public ItemType as(Name alias) {
        return new ItemType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ItemType rename(String name) {
        return new ItemType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public ItemType rename(Name name) {
        return new ItemType(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row4<Long, String, String, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
