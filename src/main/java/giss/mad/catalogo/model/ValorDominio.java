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
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "VALORDOMINIO", schema = "MACA_CATALOGO")
public final class ValorDominio {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VALORDOMINIO_SEQ")
  @SequenceGenerator(sequenceName = "VALORDOMINIO_SEQ", allocationSize = 1, name = "VALORDOMINIO_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", length = 500, nullable = false)
  private String name;

  @Column(name = "axis_attribute_id", nullable = false)
  private Integer axisAttributeId;

  @Transient
  private String attributeName;

  @Column(name = "for_catalogue")
  private Integer forCatalogue;

  @Column(name = "for_delivery")
  private Integer forDelivery;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  //@JsonIgnore
  private Timestamp creationDate;

  @Column(name = "update_date")
  @JsonIgnore
  private Timestamp updateDate;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValorDominioCondicionadoPor.class)
  @JoinColumn(name = "domain_value_id")
  private List<ValorDominioCondicionadoPor> masterDomainValues;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValorDominioDeAttrElemCat.class, orphanRemoval = false)
  @JoinColumn(name = "domain_value_id")
  private List<ValorDominioDeAttrElemCat> elementDomainValues;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValorDominioDeAttrEntrega.class, orphanRemoval = false)
  @JoinColumn(name = "domain_value_id")
  private List<ValorDominioDeAttrEntrega> deliveryDomainValues;

  public ValorDominio() {
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(final String attributeName) {
    this.attributeName = attributeName;
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

  public Integer getForCatalogue() {
    return forCatalogue;
  }

  public void setForCatalogue(final  Integer forCatalogue) {
    this.forCatalogue = forCatalogue;
  }

  public Integer getForDelivery() {
    return forDelivery;
  }

  public void setForDelivery(final Integer forDelivery) {
    this.forDelivery = forDelivery;
  }

  public Integer getAxisAttributeId() {
    return axisAttributeId;
  }

  public void setAxisAttributeId(final Integer axisAttributeId) {
    this.axisAttributeId = axisAttributeId;
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

  public List<ValorDominioCondicionadoPor> getMasterDomainValues() {
    return masterDomainValues;
  }

  public void setMasterDomainValues(
      final List<ValorDominioCondicionadoPor> masterDomainValues) {
    this.masterDomainValues = masterDomainValues;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(final Integer isDeleted) {
    this.deleted = isDeleted;
  }

  public List<ValorDominioDeAttrElemCat> getElementDomainValues() {
    return elementDomainValues;
  }

  public void setElementDomainValues(final List<ValorDominioDeAttrElemCat> elementDomainValues) {
    this.elementDomainValues = elementDomainValues;
  }

  public List<ValorDominioDeAttrEntrega> getDeliveryDomainValues() {
    return deliveryDomainValues;
  }

  public void setDeliveryDomainValues(final List<ValorDominioDeAttrEntrega> deliveryDomainValues) {
    this.deliveryDomainValues = deliveryDomainValues;
  }
}
