package giss.mad.catalogo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PERMISOROL", schema = "MACA_CATALOGO")
public final class PermisoRol {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISOROL_SEQ")
  @SequenceGenerator(sequenceName = "PERMISOROL_SEQ", allocationSize = 1, name = "PERMISOROL_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "role_id")
  private Integer roleId;

  @Column(name = "privilege_id")
  private Integer privilegeId;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  @JsonIgnore
  private Timestamp updateDate;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;


  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getPrivilegeId() {
    return privilegeId;
  }

  public void setPrivilegeId(final Integer privilegeId) {
    this.privilegeId = privilegeId;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final Timestamp updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(final Integer deleted) {
    this.deleted = deleted;
  }
}
