package giss.mad.catalogo.service;

import giss.mad.catalogo.model.ValorDominioDeAttrElemCat;
import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.AtributoEjePorTipoElemento;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.model.ValoresEjesDeElemenCatalogoUsuario;
import giss.mad.catalogo.model.ElementoCatalogo;
import giss.mad.catalogo.repository.ValorDominioRepository;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.ElementoCatalogoRepository;
import giss.mad.catalogo.repository.ValoresEjesDeElemenCatalogoUsuarioRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class AtributoEjePorTipoElementoService {

  @Autowired
  private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;

  @Autowired
  private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;

  @Autowired
  private ElementoCatalogoRepository elementoCatalogoRepository;
  @Autowired
  private AtributoEjeRepository atributoEjeRepository;

  @Autowired
  private ValorDominioRepository valorDominioRepository;

  public final void setValorDominioRepository(final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }

  public final void setAtributoEjePorTipoElementoRepository(
          final AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository) {
    this.atributoEjePorTipoElementoRepository = atributoEjePorTipoElementoRepository;
  }

  public final void setValoresEjesDeElemenCatalogoUsuarioRepository(
          final ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository) {
    this.valoresEjesDeElemenCatalogoUsuarioRepository = valoresEjesDeElemenCatalogoUsuarioRepository;
  }

  public final void setElementoCatalogoRepository(
          final ElementoCatalogoRepository elementoCatalogoRepository) {
    this.elementoCatalogoRepository = elementoCatalogoRepository;
  }

  public final void setAtributoEjeRepository(final AtributoEjeRepository atributoEjeRepository) {
    this.atributoEjeRepository = atributoEjeRepository;
  }

  public final Collection<AtributoEjePorTipoElemento> getAll() {
    return this.atributoEjePorTipoElementoRepository.findAllByDeletedIsNull();
  }

  public final AtributoEjePorTipoElemento get(final Integer idAtributoEje) {
    return this.atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(idAtributoEje);
  }

  public final List<AtributoEje> getAllByTipoCatalogueAndNotDelivery(final Integer idTipoElement) {

    List<AtributoEjePorTipoElemento> attrs = new ArrayList<>();
    Integer forCatalogue = Constantes.NUMBER_1;
    List<AtributoEjePorTipoElemento> result1 = this.atributoEjePorTipoElementoRepository.
        findAllByDeletedIsNullAndCatalogElementTypeIdAndForCatalogue(idTipoElement, forCatalogue,
                Sort.by(Sort.Order.asc("axisAttributeId")));
    List<Integer> aExcluir = new ArrayList<>();
    aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1").getId());
    aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3").getId());
    for (AtributoEjePorTipoElemento attr : result1) {
      if (!aExcluir.contains(attr.getAxisAttributeId().intValue())) {
        attrs.add(attr);
      }
    }
    List<AtributoEje> resultado = new ArrayList<>();
    for (AtributoEjePorTipoElemento attr : attrs) {
      AtributoEje newAttribute = new AtributoEje();
      AtributoEje attribute = this.atributoEjeRepository.findByIdAndDeletedIsNull(attr.getAxisAttributeId());
      newAttribute.setName(attribute.getName());
      newAttribute.setObservations(attribute.getObservations());
      newAttribute.setRegex(attribute.getRegex());
      newAttribute.setMandatory(attribute.getMandatory());
      newAttribute.setLongDescription(attribute.getLongDescription());
      newAttribute.setHelp(attribute.getHelp());
      newAttribute.setCode(attribute.getCode());
      newAttribute.setAxis(attribute.getAxis());
      newAttribute.setAxisAttributeCollateralId(attribute.getAxisAttributeCollateralId());
      newAttribute.setFromCapp(attribute.getFromCapp());
      newAttribute.setMultiple(attribute.getMultiple());
      newAttribute.setDomainValues(new ArrayList<>());
      newAttribute.setValuesInDomain(attribute.getValuesInDomain());
      newAttribute.setDefaultValue(attribute.getDefaultValue());
      newAttribute.setCreationDate(attribute.getCreationDate());
      newAttribute.setId(attribute.getId());
      newAttribute.setUpdateDate(attribute.getUpdateDate());

      List<ValorDominio> valoresDominioAttr = new ArrayList<>();
      for (ValorDominio valorDominio : attribute.getDomainValues()) {
        if (valorDominio.getForCatalogue().intValue() == Constantes.NUMBER_1) {
          valoresDominioAttr.add(valorDominio);
        }
      }
      newAttribute.setDomainValues(valoresDominioAttr);
      if (!(attribute.getValuesInDomain().intValue() == 1 && valoresDominioAttr.isEmpty())) {
        resultado.add(newAttribute);
      }
    }
    return resultado;
  }

  public final List<AtributoEje> getAllByIdCatalogueForDelivery(final Integer idElementCatalogue) {
    List<AtributoEje> resultado = new ArrayList<>();
    ElementoCatalogo elemen = this.elementoCatalogoRepository.findByIdAndDeletedIsNull(idElementCatalogue);
    if (elemen != null) {
      Integer forDelivery = Constantes.NUMBER_1;
      List<AtributoEjePorTipoElemento> attrs = this.atributoEjePorTipoElementoRepository.
              findAllByDeletedIsNullAndCatalogElementTypeIdAndForDelivery(elemen.getCatalogElementTypeId(), forDelivery,
                      Sort.by(Sort.Order.asc("axisAttributeId")));

      List<AtributoEjePorTipoElemento> result = new ArrayList<>();
      List<Integer> aExcluir = new ArrayList<>();
      aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1").getId());
      aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3").getId());
      for (AtributoEjePorTipoElemento attr : attrs) {
        if (!aExcluir.contains(attr.getAxisAttributeId().intValue())) {
          result.add(attr);
        }
      }

      for (AtributoEjePorTipoElemento attr : result) {
        AtributoEje newAttribute = new AtributoEje();

        AtributoEje attribute = this.atributoEjeRepository.findByIdAndDeletedIsNull(attr.getAxisAttributeId());
        newAttribute.setName(attribute.getName());
        newAttribute.setObservations(attribute.getObservations());
        newAttribute.setRegex(attribute.getRegex());
        newAttribute.setMandatory(attribute.getMandatory());
        newAttribute.setLongDescription(attribute.getLongDescription());
        newAttribute.setHelp(attribute.getHelp());
        newAttribute.setCode(attribute.getCode());
        newAttribute.setAxis(attribute.getAxis());
        newAttribute.setAxisAttributeCollateralId(attribute.getAxisAttributeCollateralId());
        newAttribute.setFromCapp(attribute.getFromCapp());
        newAttribute.setMultiple(attribute.getMultiple());
        newAttribute.setValuesInDomain(attribute.getValuesInDomain());
        newAttribute.setDefaultValue(attribute.getDefaultValue());
        newAttribute.setCreationDate(attribute.getCreationDate());
        newAttribute.setId(attribute.getId());
        newAttribute.setUpdateDate(attribute.getUpdateDate());

        if (attr.getForCatalogue().intValue() == Constantes.NUMBER_1) {
          ValoresEjesDeElemenCatalogoUsuario val = this.valoresEjesDeElemenCatalogoUsuarioRepository.
                  findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(newAttribute.getId(), idElementCatalogue);
          String valoresDefault = "";
          if (val.getDomainValues() != null && !val.getDomainValues().isEmpty()) {
            for (ValorDominioDeAttrElemCat valConcrete : val.getDomainValues()) {
              valoresDefault = String.valueOf(valConcrete.getDomainValueId()).concat(";").concat(valoresDefault);
            }
            valoresDefault = valoresDefault.substring(0, valoresDefault.length() - 1);
          }
          newAttribute.setDefaultValue(valoresDefault);
        }
        List<ValorDominio> valores = this.valorDominioRepository.
                findAllByDeletedIsNullAndAxisAttributeId(attribute.getId(), Sort.by(Sort.Order.asc("id")));
        List<ValorDominio> newValores = new ArrayList<>();
        for (ValorDominio valorDominio : valores) {
          if (valorDominio.getForDelivery().intValue() == Constantes.NUMBER_1) {
            newValores.add(valorDominio);
          }
        }
        newAttribute.setDomainValues(newValores);
        resultado.add(newAttribute);
      }
    }
    return resultado;
  }


  @Transactional
  public final AtributoEjePorTipoElemento insertar(final AtributoEjePorTipoElemento attr) {
    attr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    return this.atributoEjePorTipoElementoRepository.save(attr);
  }

  @Transactional
  public final AtributoEjePorTipoElemento update(final AtributoEjePorTipoElemento attr) {
    AtributoEjePorTipoElemento attribute = this.atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(
            attr.getId());
    if (attribute != null) {
      attribute.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      attribute = this.atributoEjePorTipoElementoRepository.save(attribute);
    }
    return attribute;
  }

  @Transactional
  public final AtributoEjePorTipoElemento remove(final Integer attrId) {
    AtributoEjePorTipoElemento attribute = this.atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(attrId);
    if (attribute != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      attribute.setUpdateDate(timeStamp);
      attribute.setDeleted(1);
      attribute = this.atributoEjePorTipoElementoRepository.save(attribute);
    }
    return attribute;
  }

  /*@Transactional
  public final void removePhysical(final Integer idattr) {
      this.atributoEjePorTipoElementoRepository.deleteById(idattr);
  }*/

}
