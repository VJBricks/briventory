package ch.varani.briventory.models;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "lockeduser", schema = "public")
@PrimaryKeyJoinColumn(name = "iduser")
public final class LockedUser extends User<LockedUser> {}
