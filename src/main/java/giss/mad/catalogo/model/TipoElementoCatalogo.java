package giss.mad.catalogo.model;

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
@Table(name = "TIPOELEMENTOCATALOGO", schema = "MACA_CATALOGO")
public final class TipoElementoCatalogo {

  public static final int ELEMENTO_PROMOCIONABLE = 1;
  public static final int APLICACION = 4;
  public static final int AGRUPACION_FUNCIONAL = 2;
  public static final int PROYECTO = 3;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOELEMCATALOGO_SEQ")
  @SequenceGenerator(sequenceName = "TIPOELEMENTOCATALOGO_SEQ", allocationSize = 1, name = "TIPOELEMCATALOGO_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", unique = true, nullable = false, length = 50)
  private String name;

  @Column(name = "hierarchy_level")
  private Integer hierarchyLevel;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;

  @Column(name = "is_deleted")
  private Integer deleted;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = AtributoEjePorTipoElemento.class)
  @JoinColumn(name = "catalog_element_type_id")
  private List<AtributoEjePorTipoElemento> atributosAsociados;

  public Integer getId() {
    return id;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(final Integer deleted) {
    this.deleted = deleted;
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

  public List<AtributoEjePorTipoElemento> getAtributosAsociados() {
    return atributosAsociados;
  }

  public void setAtributosAsociados(final List<AtributoEjePorTipoElemento> atributosAsociados) {
    this.atributosAsociados = atributosAsociados;
  }

  public Integer getHierarchyLevel() {
    return hierarchyLevel;
  }

  public void setHierarchyLevel(final Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
  }

}
