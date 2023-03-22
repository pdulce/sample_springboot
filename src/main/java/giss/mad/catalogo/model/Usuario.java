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
import javax.persistence.Transient;

@Entity
@Table(name = "USUARIO", schema = "MACA_CATALOGO")
public final class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
  @SequenceGenerator(sequenceName = "USUARIO_SEQ", allocationSize = 1, name = "USUARIO_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", unique = true, length = 50, nullable = false)
  private String name;

  @Column(name = "email", unique = true, length = 80, nullable = false)
  private String email;

  @Column(name = "password", length = 10, nullable = false)
  private String password;

  @Column(name = "silcon_code", unique = true, length = 15, nullable = false)
  private String silconCode;

  @Column(name = "role_id", nullable = true)
  private Integer roleId;

  @Transient
  private String roleName;
  @Transient
  private List<Permiso> permisos;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  @JsonIgnore
  private Timestamp updateDate;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = UsuarioGrupo.class, orphanRemoval = false)
  @JoinColumn(name = "user_id")
  private List<UsuarioGrupo> groups;

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

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(final Integer deleted) {
    this.deleted = deleted;
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

  public String getSilconCode() {
    return silconCode;
  }

  public void setSilconCode(final String silconCode) {
    this.silconCode = silconCode;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(final Integer roleId) {
    this.roleId = roleId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public List<UsuarioGrupo> getGroups() {
    return groups;
  }

  public void setGroups(final List<UsuarioGrupo> groups) {
    this.groups = groups;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(final String roleName) {
    this.roleName = roleName;
  }

  public List<Permiso> getPermisos() {
    return permisos;
  }

  public void setPermisos(final List<Permiso> permisos) {
    this.permisos = permisos;
  }


}
