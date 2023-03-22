package giss.mad.catalogo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "PERMISO", schema = "MACA_CATALOGO")
public final class Permiso {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISO_SEQ")
  @SequenceGenerator(sequenceName = "PERMISO_SEQ", allocationSize = 1, name = "PERMISO_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", unique = true, length = 100, nullable = false)
  private String name;

  @Column(name = "description", unique = true, nullable = false)
  private String description;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = PermisoRol.class, orphanRemoval = false)
  @JoinColumn(name = "privilege_id")
  @JsonIgnore
  private List<PermisoRol> roles;

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

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

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
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

  public List<PermisoRol> getRoles() {
    return roles;
  }

  public void setRoles(final List<PermisoRol> roles) {
    this.roles = roles;
  }
}
