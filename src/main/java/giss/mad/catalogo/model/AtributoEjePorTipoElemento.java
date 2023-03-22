package giss.mad.catalogo.model;


import javax.persistence.EntityListeners;
import javax.persistence.SequenceGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ATRIBUTOEJEPORTIPOELEMENTO", schema = "MACA_CATALOGO")
@EntityListeners(AuditingEntityListener.class)
public final class AtributoEjePorTipoElemento {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATRIBUTOEJEPOTIPOELEM_SEQ")
  @SequenceGenerator(sequenceName = "ATRIBUTOEJEPORTIPOELEM_SEQ", allocationSize = 1,
      name = "ATRIBUTOEJEPOTIPOELEM_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "axis_attribute_id")
  private Integer axisAttributeId;

  @Column(name = "catalog_element_type_id")
  private Integer catalogElementTypeId;

  @Column(name = "for_catalogue")
  private Integer forCatalogue;

  @Column(name = "for_delivery")
  private Integer forDelivery;

  @Column(name = "is_deleted")
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;


  public Integer getAxisAttributeId() {
    return axisAttributeId;
  }

  public void setAxisAttributeId(final Integer axisAttributeId) {
    this.axisAttributeId = axisAttributeId;
  }

  public Integer getCatalogElementTypeId() {
    return catalogElementTypeId;
  }

  public void setCatalogElementTypeId(final Integer catalogElementTypeId) {
    this.catalogElementTypeId = catalogElementTypeId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
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

  public void setDeleted(final Integer isDeleted) {
    this.deleted = isDeleted;
  }
}
