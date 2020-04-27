package models;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admin", schema = "public")
@PrimaryKeyJoinColumn(name = "iduser", referencedColumnName = "id")
public final class Admin extends User {}
