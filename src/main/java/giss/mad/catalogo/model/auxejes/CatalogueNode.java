package giss.mad.catalogo.model.auxejes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;

public class CatalogueNode {
    private Integer id;

    private Integer pid = -1;

    private List<String> tags;

    private String tipoElem;

    private String name;

    private String img;

    @JsonIgnore
    private String type;
    @JsonIgnore
    private String cappCode;

    private String group;

    private Timestamp creationDate;

    public final Integer getId() {
        return id;
    }

    public final void setId(final Integer id) {
        this.id = id;
    }

    public final Integer getPid() {
        return pid;
    }

    public final void setPid(final Integer pid) {
        this.pid = pid;
    }

    public final List<String> getTags() {
        return tags;
    }

    public final void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public final String getTipoElem() {
        tipoElem = getType().concat(" (").concat(getCappCode()).concat(")");
        return tipoElem;
    }

    public final void setTipoElem(final String tipoElem) {
        this.tipoElem = tipoElem;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getImg() {
        return img;
    }

    public final void setImg(final String img) {
        this.img = img;
    }

    public final String getType() {
        return type;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final String getCappCode() {
        return cappCode;
    }

    public final void setCappCode(final String cappCode) {
        this.cappCode = cappCode;
    }

    public final String getGroup() {
        return group;
    }

    public final void setGroup(final String group) {
        this.group = group;
    }

    public final Timestamp getCreationDate() {
        return creationDate;
    }

    public final void setCreationDate(final Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
