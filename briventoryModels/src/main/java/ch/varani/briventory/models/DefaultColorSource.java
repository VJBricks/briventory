package ch.varani.briventory.models;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "defaultcolorsource", schema = "public")
@PrimaryKeyJoinColumn(name = "idcolorsource")
public final class DefaultColorSource extends ColorSource {}
