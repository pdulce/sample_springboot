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
import javax.persistence.Transient;

@Entity
@Table(name = "USUARIOGRUPO", schema = "MACA_CATALOGO")
public final class UsuarioGrupo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIOGRUPO_SEQ")
  @SequenceGenerator(sequenceName = "USUARIOGRUPO_SEQ", allocationSize = 1, name = "USUARIOGRUPO_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "group_id")
  private Integer groupId;

  @Transient
  private String groupName;

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

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(final Integer userId) {
    this.userId = userId;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
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

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(final String groupName) {
    this.groupName = groupName;
  }
}
