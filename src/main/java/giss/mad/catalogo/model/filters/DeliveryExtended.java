package giss.mad.catalogo.model.filters;


import com.fasterxml.jackson.annotation.JsonIgnore;
import giss.mad.catalogo.model.ValoresEjesDeEntregaUsuario;

import java.sql.Timestamp;
import java.util.List;

public final class DeliveryExtended {


  private Integer id;

  private Integer catalogElementTypeId;

  private String name;

  private String cappCode;

  private Integer catalogElementId;

  @JsonIgnore
  private Integer deleted;

  private Timestamp creationDate;

  private Timestamp startDate;

  private Timestamp endDate;

  private Integer userId;

  @JsonIgnore
  private Timestamp updateDate;

  private List<ValoresEjesDeEntregaUsuario> attributeValuesCollection;

  public DeliveryExtended() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getCatalogElementTypeId() {
    return catalogElementTypeId;
  }

  public void setCatalogElementTypeId(final Integer catalogElementTypeId) {
    this.catalogElementTypeId = catalogElementTypeId;
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

  public Integer getCatalogElementId() {
    return catalogElementId;
  }

  public void setCatalogElementId(final Integer catalogElementId) {
    this.catalogElementId = catalogElementId;
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

  public List<ValoresEjesDeEntregaUsuario> getAttributeValuesCollection() {
    return attributeValuesCollection;
  }

  public void setAttributeValuesCollection(
      final List<ValoresEjesDeEntregaUsuario> attributeValuesCollection) {
    this.attributeValuesCollection = attributeValuesCollection;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(final Timestamp startDate) {
    this.startDate = startDate;
  }

  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(final Timestamp endDate) {
    this.endDate = endDate;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(final Integer userId) {
    this.userId = userId;
  }
}
