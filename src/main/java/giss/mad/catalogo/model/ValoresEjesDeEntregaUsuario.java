package giss.mad.catalogo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "VALORESEJESDEENTREGAUSUARIO", schema = "MACA_CATALOGO")
public final class ValoresEjesDeEntregaUsuario {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VALOREJEDEENTREGA_SEQ")
  @SequenceGenerator(sequenceName = "VALORESEJESENTREGAUSER_SEQ", allocationSize = 1, name = "VALOREJEDEENTREGA_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "axis_attribute_id")
  private Integer axisAttributeId;

  @Column(name = "delivery_catalog_element_id", nullable = false)
  private Integer deliveryCatalogElementId;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ValorDominioDeAttrEntrega.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "valor_eje_entrega_id")
  private List<ValorDominioDeAttrEntrega> domainValues;

  @Column(name = "user_value", length = 500)
  private String userValue;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  @JsonIgnore
  private Timestamp creationDate;

  @Column(name = "update_date")
  @JsonIgnore
  private Timestamp updateDate;

  public ValoresEjesDeEntregaUsuario() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getAxisAttributeId() {
    return axisAttributeId;
  }

  public void setAxisAttributeId(final Integer axisAttributeId) {
    this.axisAttributeId = axisAttributeId;
  }

  public Integer getDeliveryCatalogElementId() {
    return deliveryCatalogElementId;
  }

  public void setDeliveryCatalogElementId(final Integer deliveryCatalogElementId) {
    this.deliveryCatalogElementId = deliveryCatalogElementId;
  }
  public String getUserValue() {
    return userValue;
  }

  public void setUserValue(final String userValue) {
    this.userValue = userValue;
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

  public List<ValorDominioDeAttrEntrega> getDomainValues() {
    return domainValues;
  }

  public void setDomainValues(final List<ValorDominioDeAttrEntrega> domainValues) {
    this.domainValues = domainValues;
  }
}
