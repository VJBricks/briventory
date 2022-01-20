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

import java.beans.ConstructorProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

import jooq.tables.Container;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
    name = "container",
    schema = "public"
)
public class ContainerRecord extends UpdatableRecordImpl<ContainerRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = -272012393;

    /**
     * Setter for <code>public.container.id</code>.
     */
    public ContainerRecord setId(@Nonnull Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.container.id</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, precision = 64)
    @Nonnull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.container.id_container_type</code>.
     */
    public ContainerRecord setIdContainerType(@Nullable Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.container.id_container_type</code>.
     */
    @Column(name = "id_container_type", precision = 64)
    @Nullable
    public Long getIdContainerType() {
        return (Long) get(1);
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
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @Nonnull
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @Nonnull
    public Field<Long> field1() {
        return Container.CONTAINER.ID;
    }

    @Override
    @Nonnull
    public Field<Long> field2() {
        return Container.CONTAINER.ID_CONTAINER_TYPE;
    }

    @Override
    @Nonnull
    public Long component1() {
        return getId();
    }

    @Override
    @Nullable
    public Long component2() {
        return getIdContainerType();
    }

    @Override
    @Nonnull
    public Long value1() {
        return getId();
    }

    @Override
    @Nullable
    public Long value2() {
        return getIdContainerType();
    }

    @Override
    @Nonnull
    public ContainerRecord value1(@Nonnull Long value) {
        setId(value);
        return this;
    }

    @Override
    @Nonnull
    public ContainerRecord value2(@Nullable Long value) {
        setIdContainerType(value);
        return this;
    }

    @Override
    @Nonnull
    public ContainerRecord values(@Nonnull Long value1, @Nullable Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ContainerRecord
     */
    public ContainerRecord() {
        super(Container.CONTAINER);
    }

    /**
     * Create a detached, initialised ContainerRecord
     */
    @ConstructorProperties({ "id", "idContainerType" })
    public ContainerRecord(@Nonnull Long id, @Nullable Long idContainerType) {
        super(Container.CONTAINER);

        setId(id);
        setIdContainerType(idContainerType);
    }
}
