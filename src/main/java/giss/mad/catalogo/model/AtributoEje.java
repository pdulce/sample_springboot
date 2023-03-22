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
import giss.mad.catalogo.model.auxejes.TipoElementoReduced;
import java.util.List;

@Entity
@Table(name = "ATRIBUTOEJE", schema = "MACA_CATALOGO")
public final class AtributoEje {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATRIBUTOEJE_SEQ")
  @SequenceGenerator(sequenceName = "ATRIBUTOEJE_SEQ", allocationSize = 1, name = "ATRIBUTOEJE_SEQ")
  @Column(name = "id", nullable = false)
  private Integer id;
  @Column(name = "name", unique = true, length = 50, nullable = false)
  private String name;

  @Column(name = "code", length = 20, nullable = false)
  private String code;

  @Column(name = "is_axis", nullable = false)
  private Integer axis = Constantes.NUMBER_0;

  @Column(name = "is_mandatory", nullable = false)
  private Integer mandatory = Constantes.NUMBER_0;

  @Column(name = "defaultvalue", length = 255)
  private String defaultValue;

  @Column(name = "is_from_capp", nullable = false)
  private Integer fromCapp;

  @Column(name = "multiple", nullable = false)
  private Integer multiple;

  @Column(name = "long_description")
  private Integer longDescription;
  @Column(name = "help")
  private String help;

  @Column(name = "axis_attribute_collateral_id")
  private Integer axisAttributeCollateralId;

  @Column(name = "values_in_domain", nullable = false)
  private Integer valuesInDomain;
  @Column(name = "regex")
  private String regex;

  @Column(name = "observations")
  private String observations;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  private Timestamp updateDate;

  @OneToMany(cascade = CascadeType.REMOVE, targetEntity = ValorDominio.class)
  @JoinColumn(name = "axis_attribute_id")
  private List<ValorDominio> domainValues;

  @Transient
  private List<TipoElementoReduced> elementypes;

  public List<TipoElementoReduced> getElementypes() {
    return elementypes;
  }

  public void setElementypes(final List<TipoElementoReduced> elementypes) {
    this.elementypes = elementypes;
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

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public Integer getAxis() {
    return axis;
  }

  public void setAxis(final Integer axis) {
    this.axis = axis;
  }

  public Integer getMandatory() {
    return mandatory;
  }

  public void setMandatory(final Integer mandatory) {
    this.mandatory = mandatory;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Integer getFromCapp() {
    return fromCapp;
  }

  public void setFromCapp(final Integer fromCapp) {
    this.fromCapp = fromCapp;
  }

  public Integer getLongDescription() {
    return longDescription;
  }

  public void setLongDescription(final Integer longDescription) {
    this.longDescription = longDescription;
  }

  public String getHelp() {
    return help;
  }

  public void setHelp(final String help) {
    this.help = help;
  }

  public Integer getAxisAttributeCollateralId() {
    return axisAttributeCollateralId;
  }

  public void setAxisAttributeCollateralId(final Integer axisAttributeCollateralId) {
    this.axisAttributeCollateralId = axisAttributeCollateralId;
  }

  public Integer getValuesInDomain() {
    if ((domainValues != null && !domainValues.isEmpty())) {
      this.valuesInDomain = Constantes.NUMBER_1;
    }
    return valuesInDomain;
  }

  public void setValuesInDomain(final Integer valuesindomain) {
    if ((domainValues != null && !domainValues.isEmpty())
        || valuesindomain.intValue() == Constantes.NUMBER_1) {
      this.valuesInDomain = Constantes.NUMBER_1;
    } else {
      this.valuesInDomain = valuesindomain;
    }
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(final String regex) {
    this.regex = regex;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(final String observations) {
    this.observations = observations;
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

  public List<ValorDominio> getDomainValues() {
    return domainValues;
  }

  public void setDomainValues(final List<ValorDominio> domainValues) {
    this.domainValues = domainValues;
  }

  public Integer getMultiple() {
    return multiple;
  }

  public void setMultiple(final Integer multiple) {
    this.multiple = multiple;
  }
}
