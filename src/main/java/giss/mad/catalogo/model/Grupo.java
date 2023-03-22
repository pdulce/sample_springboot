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
import java.util.List;

@Entity
@Table(name = "GRUPO", schema = "MACA_CATALOGO")
public final class Grupo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_SEQ")
  @SequenceGenerator(sequenceName = "GRUPO_SEQ", allocationSize = 1, name = "GRUPO_SEQ")
  @Column(name = "id", nullable = false)

  private Integer id;
  @Column(name = "name", unique = true, length = 100, nullable = false)
  private String name;

  @Column(name = "is_deleted")
  @JsonIgnore
  private Integer deleted;

  @Column(name = "creation_date", nullable = false)
  private Timestamp creationDate;

  @Column(name = "update_date")
  @JsonIgnore
  private Timestamp updateDate;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = ElementoCatalogo.class, orphanRemoval = false)
  @JoinColumn(name = "group_id")
  private List<ElementoCatalogo> catalogueElements;

  @OneToMany(cascade = CascadeType.ALL, targetEntity = EntregaElementoCatalogo.class, orphanRemoval = false)
  @JoinColumn(name = "group_id")
  private List<EntregaElementoCatalogo> deliveries;

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

  public List<ElementoCatalogo> getCatalogueElements() {
    return catalogueElements;
  }

  public void setCatalogueElements(final List<ElementoCatalogo> catalogueElements) {
    this.catalogueElements = catalogueElements;
  }

  public List<EntregaElementoCatalogo> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(final List<EntregaElementoCatalogo> deliveries) {
    this.deliveries = deliveries;
  }
}
