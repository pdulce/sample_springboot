package giss.mad.catalogo.model;

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
@Table(name = "VALORDOMINIOCONDICIONADOPOR", schema = "MACA_CATALOGO")
public final class ValorDominioCondicionadoPor {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VALORDOMINIOCONDIT_SEQ")
  @SequenceGenerator(sequenceName = "VALORDOMINIOCONDITIONEDBY_SEQ", allocationSize = 1,
      name = "VALORDOMINIOCONDIT_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "domain_value_id")
  private Integer domainValueId;
  @Transient
  private String name;

  @Transient
  private String attributeName;

  @Column(name = "domain_value_collateral_id")
  private Integer domainValueCollateralId;

  @Column(name = "is_deleted")
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;

  public ValorDominioCondicionadoPor() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getDomainValueId() {
    return domainValueId;
  }

  public void setDomainValueId(final Integer domainValueId) {
    this.domainValueId = domainValueId;
  }

  public Integer getDomainValueCollateralId() {
    return domainValueCollateralId;
  }

  public void setDomainValueCollateralId(final Integer domainValueCollateralId) {
    this.domainValueCollateralId = domainValueCollateralId;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(final String attributeName) {
    this.attributeName = attributeName;
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
