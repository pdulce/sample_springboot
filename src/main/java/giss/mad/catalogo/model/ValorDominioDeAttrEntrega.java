package giss.mad.catalogo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "VALORDOMINIODEATTRENTREGA", schema = "MACA_CATALOGO")
public final class ValorDominioDeAttrEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "valordominiodeattrentrega_seq")
    @SequenceGenerator(sequenceName = "valordominiodeattrentrega_seq", allocationSize = 1,
            name = "valordominiodeattrentrega_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "domain_value_id")
    private Integer domainValueId;
    @Column(name = "valor_eje_entrega_id")
    private Integer valorEjeEntregaId;

    @Column(name = "is_deleted")
    @JsonIgnore
    private Integer deleted;

    @Column(name = "creation_date", nullable = false)
    @JsonIgnore
    private Timestamp creationDate;

    @Column(name = "update_date")
    @JsonIgnore
    private Timestamp updateDate;

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

    public Integer getValorEjeEntregaId() {
        return valorEjeEntregaId;
    }

    public void setValorEjeEntregaId(final Integer valorEjeEntregaId) {
        this.valorEjeEntregaId = valorEjeEntregaId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(final Integer isDeleted) {
        this.deleted = isDeleted;
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
}
