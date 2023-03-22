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
@Table(name = "ENTREGAELEMENTOCATALOGO", schema = "MACA_CATALOGO")
public final class EntregaElementoCatalogo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTREGAELEMCATALOGUE_SEQ")
  @SequenceGenerator(sequenceName = "ENTREGAELEMENTOCATALOGO_SEQ", allocationSize = 1,
      name = "ENTREGAELEMCATALOGUE_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;
  @Column(name = "name", unique = true, length = 50, nullable = false)
  private String name;

  @Column(name = "catalog_element_id", nullable = false)
  private Integer catalogElementId;

  @Column(name = "group_id", nullable = false)
  private Integer groupId;

  @Column(name = "is_deleted")
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValoresEjesDeEntregaUsuario.class)
  @JoinColumn(name = "delivery_catalog_element_id")
  private List<ValoresEjesDeEntregaUsuario> attributeValuesCollection;


  public EntregaElementoCatalogo() {
  }

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

  public Integer getCatalogElementId() {
    return catalogElementId;
  }

  public void setCatalogElementId(final Integer catalogElementId) {
    this.catalogElementId = catalogElementId;
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

  public List<ValoresEjesDeEntregaUsuario> getAttributeValuesCollection() {
    return attributeValuesCollection;
  }

  public void setAttributeValuesCollection(final List<ValoresEjesDeEntregaUsuario>
      valoresAtributosEjes) {
    this.attributeValuesCollection = valoresAtributosEjes;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
  }
}
