package giss.mad.catalogo.service;

import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.AtributoEjePorTipoElemento;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.TipoElementoCatalogo;
import giss.mad.catalogo.model.auxejes.TipoElementoReduced;
import giss.mad.catalogo.model.auxejes.EjeReduced;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


@Service
@Component
public class AtributoEjeService  { //extends BaseService {
  @Autowired
  private AtributoEjeRepository atributoEjeRepository;
  @Autowired
  private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

  @Autowired
  private ValorDominioRepository valorDominioRepository;
  @Autowired
  private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;

  public final void setAtributoEjePorTipoElementoRepository(final AtributoEjePorTipoElementoRepository
                                                                    atributoEjePorTipoElementoRepository) {
    this.atributoEjePorTipoElementoRepository = atributoEjePorTipoElementoRepository;
  }
  public final void setAtributoEjeRepository(final AtributoEjeRepository atributoEjeRepository) {
    this.atributoEjeRepository = atributoEjeRepository;
  }

  public final void setValorDominioRepository(final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }

  public final void setTipoElementoCatalogoRepository(final TipoElementoCatalogoRepository
                                                              tipoElementoCatalogoRepository) {
    this.tipoElementoCatalogoRepository = tipoElementoCatalogoRepository;
  }

  /*@Override
  protected String getServiceName() {
    return "AtributoEjeService";
  }*/

  private boolean atributoContenidoEnValues(final Integer idValueDomain, final List<ValorDominio> valuesDomain) {
    boolean foundValue = false;
    int i = 0;
    while (i < valuesDomain.size() && !foundValue) {
      ValorDominio valIesimo = valuesDomain.get(i);
      if (valIesimo.getId().intValue() == idValueDomain.intValue()) {
        foundValue = true;
      }
      i++;
    }
    return foundValue;
  }

  private boolean atributoContenidoEnAttrs(final Integer id, final List<AtributoEjePorTipoElemento> listaofAttrs) {
    boolean foundAttr = false;
    int i = 0;
    while (i < listaofAttrs.size() && !foundAttr) {
      AtributoEjePorTipoElemento attrIesimo = listaofAttrs.get(i);
      if (attrIesimo.getAxisAttributeId().intValue() == id.intValue()) {
        foundAttr = true;
      }
      i++;
    }
    return foundAttr;
  }

  private void setTiposAssigned(final AtributoEje attr) {
    List<TipoElementoReduced> tiposAssigned = new ArrayList<>();
    if (attr.getId() != null) {
      List<TipoElementoCatalogo> tipos = this.tipoElementoCatalogoRepository.findAllByDeletedIsNull();
      //buscamos las apariciones de ese atributo en los elementos de catalogo
      for (TipoElementoCatalogo tipoElemento : tipos) {
        if (atributoContenidoEnAttrs(attr.getId(), tipoElemento.getAtributosAsociados())) {
          TipoElementoReduced reducedTipoElemen = new TipoElementoReduced();
          reducedTipoElemen.setId(tipoElemento.getId());
          reducedTipoElemen.setName(tipoElemento.getName());
          tiposAssigned.add(reducedTipoElemen);
        }
      }
    }

    if (tiposAssigned.isEmpty()) {
      List<TipoElementoReduced> reduceds = attr.getElementypes() == null ? new ArrayList<>() : attr.getElementypes();
      for (TipoElementoReduced tipoElementReduced: reduceds) {
        tipoElementReduced.setName(this.tipoElementoCatalogoRepository.
                findByIdAndDeletedIsNull(tipoElementReduced.getId()).getName());
        tiposAssigned.add(tipoElementReduced);
      }
    }
    attr.setElementypes(tiposAssigned);
  }

  public final Collection<AtributoEje> getAll() {
    Collection<AtributoEje> c = this.atributoEjeRepository.findAllByDeletedIsNull(
            Sort.by(Sort.Order.asc("id")));
    for (AtributoEje attr: c) {
      setTiposAssigned(attr);
    }
    return c;
  }

  public final Collection<AtributoEje> getEjes() {
    return this.atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_1,
            Sort.by(Sort.Order.asc("id")));
  }
  private AtributoEje searchInList(final Integer id, final List<AtributoEje> ejesQA) {
    AtributoEje searched = null;
    boolean found = false;
    for (int i = 0; i < ejesQA.size() && !found; i++) {
      AtributoEje t = ejesQA.get(i);
      if (t.getId().intValue() == id.intValue()) {
        found = true;
        searched = t;
      }
    }
    return searched;
  }

  public final List<String> getAxisNamesOrderedById() {
    List<String> labelsOrderedById = new ArrayList<>();
    List<AtributoEje> nombresEjesOrdered = this.atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_1,
            Sort.by(Sort.Order.asc("id")));
    int min = 1;
    int last = (nombresEjesOrdered != null && !nombresEjesOrdered.isEmpty()) ? nombresEjesOrdered.
            get(nombresEjesOrdered.size() - 1).getId() : 0;
    for (int i = min; i <= last; i++) {
      AtributoEje searched = searchInList(i, nombresEjesOrdered);
      if (searched != null) {
        labelsOrderedById.add(searched.getName());
      } else {
        labelsOrderedById.add("");
      }
    }
    return labelsOrderedById;
  }


  public final Collection<EjeReduced> getEjesReduced() {
    Collection<EjeReduced> listaEjeReduceds = new ArrayList<>();
    Collection<AtributoEje> all = this.atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_1,
            Sort.by(Sort.Order.asc("id")));
    for (AtributoEje ejeOrAttr : all) {
      if (ejeOrAttr.getAxis().intValue() == Constantes.NUMBER_1) {
        EjeReduced newEjeReduced = new EjeReduced(ejeOrAttr.getId(), ejeOrAttr.getName());
        listaEjeReduceds.add(newEjeReduced);
      }
    }
    return listaEjeReduceds;
  }


  public final Collection<AtributoEje> getSoloAtributos() {
    return this.atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_0,
            Sort.by(Sort.Order.asc("id")));
  }


  public final AtributoEje get(final Integer idAtributoEje) {
    return this.atributoEjeRepository.findByIdAndDeletedIsNull(idAtributoEje);
  }

  @Transactional
  public final AtributoEje insertar(final AtributoEje attr) {
    attr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    setTiposAssigned(attr);
    AtributoEje attrBBDD = this.atributoEjeRepository.save(attr);
    attrBBDD.setDomainValues(new ArrayList<>());
    attrBBDD.setElementypes(attr.getElementypes());

    List<TipoElementoReduced> reduceds = attr.getElementypes() == null ? new ArrayList<>() : attr.getElementypes();
    for (TipoElementoReduced reduced : reduceds) {
      List<AtributoEjePorTipoElemento> attrsTipoCat =  this.atributoEjePorTipoElementoRepository.
              findAllByDeletedIsNullAndCatalogElementTypeIdAndAxisAttributeId(reduced.getId(), attr.getId());
      if (attrsTipoCat.isEmpty()) {
        AtributoEjePorTipoElemento nuevoAttrByTipoElem = new AtributoEjePorTipoElemento();
        nuevoAttrByTipoElem.setAxisAttributeId(attr.getId());
        nuevoAttrByTipoElem.setCatalogElementTypeId(reduced.getId());
        nuevoAttrByTipoElem.setForDelivery(Constantes.NUMBER_1);
        nuevoAttrByTipoElem.setForCatalogue(Constantes.NUMBER_1);
        nuevoAttrByTipoElem.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.atributoEjePorTipoElementoRepository.save(nuevoAttrByTipoElem);
      }
    }
    return attrBBDD;
  }

  @Transactional
  public final AtributoEje actualizar(final AtributoEje attr) {

    List<ValorDominio> nuevosValoresDeDominio = new ArrayList<>();
    if (attr.getDomainValues() != null) {
      nuevosValoresDeDominio.addAll(attr.getDomainValues());
    }
    AtributoEje attrBBDD = this.atributoEjeRepository.findByIdAndDeletedIsNull(attr.getId());
    if (attrBBDD != null) {
      List<TipoElementoCatalogo> tipos = this.tipoElementoCatalogoRepository.findAllByDeletedIsNull();
      //buscamos las apariciones de ese atributo en los elementos de catalogo
      for (TipoElementoCatalogo tipoElemento : tipos) {
        List<AtributoEjePorTipoElemento> atributosPorTipoElem = this.atributoEjePorTipoElementoRepository.
                findAllByDeletedIsNullAndCatalogElementTypeIdAndAxisAttributeId(tipoElemento.getId(), attr.getId());
        if (atributosPorTipoElem != null && !atributosPorTipoElem.isEmpty()) {
          this.atributoEjePorTipoElementoRepository.deleteById(atributosPorTipoElem.get(0).getId());
        }
      }

      attr.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      attr.setCreationDate(attrBBDD.getCreationDate());
      attr.setDomainValues(attrBBDD.getDomainValues());
      this.atributoEjeRepository.save(attr);
      setTiposAssigned(attr);

      if (attrBBDD.getDomainValues() != null && attrBBDD.getDomainValues().size() != nuevosValoresDeDominio.size()) {
        //desenganchamos aquellas apariciones en BBDD que no viajan en la lista 'nuevosValoresDeDominio'
        for (ValorDominio valorDominio : attrBBDD.getDomainValues()) {
          ValorDominio valorDominioBBDD = this.valorDominioRepository.findByIdAndDeletedIsNull(valorDominio.getId());
          if (!atributoContenidoEnValues(valorDominio.getId(), nuevosValoresDeDominio)) {
            // borramos ese valor de dominio del atributo
            this.valorDominioRepository.deleteById(valorDominioBBDD.getId());
          }
        }
        List<ValorDominio> lista = new ArrayList<>();
        for (ValorDominio valorDominio :  nuevosValoresDeDominio) {
          valorDominio = this.valorDominioRepository.findByIdAndDeletedIsNull(valorDominio.getId());
          lista.add(valorDominio);
        }
        attr.getDomainValues().clear();
        attr.setDomainValues(lista);
      }
      this.atributoEjeRepository.save(attr);

      List<TipoElementoReduced> reduceds = attr.getElementypes() == null ? new ArrayList<>() : attr.getElementypes();
      for (TipoElementoReduced reduced : reduceds) {
        List<AtributoEjePorTipoElemento> attrsTipoCat =  this.atributoEjePorTipoElementoRepository.
                findAllByDeletedIsNullAndCatalogElementTypeIdAndAxisAttributeId(reduced.getId(), attr.getId());
        if (attrsTipoCat.isEmpty()) {
          AtributoEjePorTipoElemento nuevoAttrByTipoElem = new AtributoEjePorTipoElemento();
          nuevoAttrByTipoElem.setCatalogElementTypeId(reduced.getId());
          nuevoAttrByTipoElem.setAxisAttributeId(attr.getId());
          nuevoAttrByTipoElem.setForDelivery(Constantes.NUMBER_1);
          nuevoAttrByTipoElem.setForCatalogue(Constantes.NUMBER_1);
          nuevoAttrByTipoElem.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
          this.atributoEjePorTipoElementoRepository.save(nuevoAttrByTipoElem);
        }
      }
    }
    return attr;
  }

  @Transactional
  public final AtributoEje borradoLogico(final Integer idattr) {
    AtributoEje attr = this.atributoEjeRepository.findByIdAndDeletedIsNull(idattr);
    if (attr != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      attr.setUpdateDate(timeStamp);
      attr.setDeleted(1);
      AtributoEje attrDeleted = null;
      int veces = Constantes.NUMBER_0;
      do {
        String codeDeleted = "old[".concat(String.valueOf(++veces).concat("]:"))
                .concat(attr.getCode());
        attrDeleted = this.atributoEjeRepository.findByCodeAndDeletedIsNull(codeDeleted);
      } while (attrDeleted != null);
      String preffix = "old[" + veces + "]:";
      String newCode = preffix.concat(attr.getCode());
      attr.setCode(newCode);
      if (newCode.length() > Constantes.POSITION_TWELVE) {
        newCode = newCode.substring(Constantes.NUMBER_0, Constantes.POSITION_NINETEEN);
      }
      attr.setCode(newCode);

      this.atributoEjeRepository.save(attr);

      for (ValorDominio valorDominio : attr.getDomainValues()) {
        this.valorDominioRepository.deleteById(valorDominio.getId());
      }

      List<AtributoEjePorTipoElemento> attrsTipoCat = this.atributoEjePorTipoElementoRepository.
              findAllByDeletedIsNullAndAxisAttributeId(attr.getId());
      if (!attrsTipoCat.isEmpty()) {
        this.atributoEjePorTipoElementoRepository.deleteAll(attrsTipoCat);
      }

    }
    return attr;
  }


  /*@Transactional
  public final AtributoEje removePhysical(final Integer idattr) {
    AtributoEje attr = this.atributoEjeRepository.findById(idattr).get();
    if (attr != null) {
      this.atributoEjeRepository.delete(attr);
    }
    return attr;
  }*/

}
