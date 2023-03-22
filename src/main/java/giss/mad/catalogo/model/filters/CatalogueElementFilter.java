package giss.mad.catalogo.model.filters;


import com.fasterxml.jackson.annotation.JsonIgnore;


import java.util.Map;

public final class CatalogueElementFilter {

  @JsonIgnore
  private Integer id;

  private Integer userId;
  private String catalogueElementTypeName;
  private Integer idOfCatalogueElementType;
  private String preffix;
  private String name;

  private String code;

  private String responsible;

  private String serviceName;

  private String functionalAreaName;

  private String computerProcessing;
  private Integer computerProcessingId;

  private String group;
  private Integer groupId;

  private String situation;
  private Integer situationId;

  public CatalogueElementFilter() {
  }

  public static String getCodeOf(final String codeOfAttribute) {
    Map<String, String> relation = Map.of("preffix", "ATTR1",
        "name", "ATTR2",
        "code", "ATTR3",
        "responsible", "ATTR6",
        "serviceName", "ATTR11",
        "functionalAreaName", "ATTR12",
        "computerProcessingId", "ATTR18",
        "groupId", "ATTR19",
        "situationId", "ATTR33");

    return relation.get(codeOfAttribute);
  }

  public Integer getIdOfCatalogueElementType() {
    return idOfCatalogueElementType;
  }

  public void setIdOfCatalogueElementType(final Integer idOfCatalogueElementType) {
    this.idOfCatalogueElementType = idOfCatalogueElementType;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getPreffix() {
    return preffix;
  }

  public void setPreffix(final String preffix) {
    this.preffix = preffix;
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

  public String getResponsible() {
    return responsible;
  }

  public void setResponsible(final String responsible) {
    this.responsible = responsible;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(final String serviceName) {
    this.serviceName = serviceName;
  }

  public String getFunctionalAreaName() {
    return functionalAreaName;
  }

  public void setFunctionalAreaName(final String functionalAreaName) {
    this.functionalAreaName = functionalAreaName;
  }

  public Integer getComputerProcessingId() {
    return computerProcessingId;
  }

  public void setComputerProcessingId(final Integer computerProcessingId) {
    this.computerProcessingId = computerProcessingId;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(final Integer groupId) {
    this.groupId = groupId;
  }

  public Integer getSituationId() {
    return situationId;
  }

  public void setSituationId(final Integer situationId) {
    this.situationId = situationId;
  }

  public String getComputerProcessing() {
    return computerProcessing;
  }

  public void setComputerProcessing(final String computerProcessing) {
    this.computerProcessing = computerProcessing;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(final String group) {
    this.group = group;
  }

  public String getSituation() {
    return situation;
  }

  public void setSituation(final String situation) {
    this.situation = situation;
  }

  public String getCatalogueElementTypeName() {
    return catalogueElementTypeName;
  }

  public void setCatalogueElementTypeName(final String catalogueElementTypeName) {
    this.catalogueElementTypeName = catalogueElementTypeName;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(final Integer userId) {
    this.userId = userId;
  }
}
