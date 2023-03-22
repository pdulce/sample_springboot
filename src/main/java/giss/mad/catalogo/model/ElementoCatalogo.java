package giss.mad.catalogo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.FetchType;
import java.util.List;

@Entity
@Table(name = "ELEMENTOCATALOGO", schema = "MACA_CATALOGO")
public final class ElementoCatalogo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELEMCATALOGUE_SEQ")
  @SequenceGenerator(sequenceName = "ELEMENTOCATALOGO_SEQ", allocationSize = 1, name = "ELEMCATALOGUE_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", unique = true, length = 50, nullable = false)
  private String name;

  @Column(name = "capp_code", length = 50)
  private String cappCode;

  @Column(name = "catalog_element_type_id", nullable = false)
  private Integer catalogElementTypeId;

  @Column(name = "catalog_element_collateral_id")
  private Integer catalogElementCollateralId;

  @Column(name = "group_id", nullable = false)
  private Integer groupId;
  @Column(name = "is_deleted")
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;

  @Transient
  private ElementoCatalogo parentElement;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValoresEjesDeElemenCatalogoUsuario.class,
          fetch = FetchType.EAGER)
  @JoinColumn(name = "catalog_element_id")
  @Fetch(FetchMode.JOIN) // necesario en H2 para carga en memoria de esta lista
  private List<ValoresEjesDeElemenCatalogoUsuario> attributeValuesCollection;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = EntregaElementoCatalogo.class)
  @JoinColumn(name = "catalog_element_id")
  private List<EntregaElementoCatalogo> deliveryCollection;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ElementoCatalogo.class)
  @JoinColumn(name = "catalog_element_collateral_id")
  private List<ElementoCatalogo> subElements;

  public ElementoCatalogo() {
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

  public String getCappCode() {
    return cappCode;
  }

  public void setCappCode(final String cappCode) {
    this.cappCode = cappCode;
  }

  public Integer getCatalogElementTypeId() {
    return catalogElementTypeId;
  }

  public void setCatalogElementTypeId(final Integer catalogElementTypeId) {
    this.catalogElementTypeId = catalogElementTypeId;
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

  public List<ValoresEjesDeElemenCatalogoUsuario> getAttributeValuesCollection() {
    return attributeValuesCollection;
  }

  public void setAttributeValuesCollection(
      final List<ValoresEjesDeElemenCatalogoUsuario> attributeValuesCollection) {
    this.attributeValuesCollection = attributeValuesCollection;
  }

  public List<EntregaElementoCatalogo> getDeliveryCollection() {
    return deliveryCollection;
  }

  public void setDeliveryCollection(final List<EntregaElementoCatalogo> deliveryCollection) {
    this.deliveryCollection = deliveryCollection;
  }

  @JsonIgnore
  public Integer getCatalogElementCollateralId() {
    return catalogElementCollateralId;
  }

  public void setCatalogElementCollateralId(final Integer catalogelementcollateral) {
    this.catalogElementCollateralId = catalogelementcollateral;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
  }

  public List<ElementoCatalogo> getSubElements() {
    return subElements;
  }

  public void setSubElements(final List<ElementoCatalogo> subElements) {
    this.subElements = subElements;
  }

  public ElementoCatalogo getParentElement() {
    return parentElement;
  }

  public void setParentElement(final ElementoCatalogo parentElement) {
    this.parentElement = parentElement;
  }
}
