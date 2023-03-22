package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.ValorDominioDeAttrEntrega;
import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.ElementoCatalogo;
import giss.mad.catalogo.model.EntregaElementoCatalogo;
import giss.mad.catalogo.model.Rol;
import giss.mad.catalogo.model.Usuario;
import giss.mad.catalogo.model.UsuarioGrupo;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import giss.mad.catalogo.model.ValoresEjesDeEntregaUsuario;
import giss.mad.catalogo.model.ValoresEjesDeElemenCatalogoUsuario;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.ElementoCatalogoRepository;
import giss.mad.catalogo.repository.EntregaElementoCatalogoRepository;
import giss.mad.catalogo.repository.RolRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
import giss.mad.catalogo.repository.UsuarioRepository;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
import giss.mad.catalogo.repository.ValoresEjesDeEntregaUsuarioRepository;
import giss.mad.catalogo.repository.ValoresEjesDeElemenCatalogoUsuarioRepository;
import giss.mad.catalogo.model.filters.DeliveryExtended;
import giss.mad.catalogo.model.filters.DeliveryFilter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import javax.transaction.Transactional;

@Service
@Component
public class EntregaElementoCatalogoService {

  @Autowired
  private ValoresEjesDeEntregaUsuarioRepository valoresEjesDeEntregaUsuarioRepository;

  @Autowired
  private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;
  @Autowired
  private EntregaElementoCatalogoRepository entregaElementoCatalogoRepository;

  @Autowired
  private ElementoCatalogoRepository elementoCatalogoPadreEntregaRepository;
  @Autowired
  private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

  @Autowired
  private AtributoEjeRepository atributoEjeRepository;

  @Autowired
  private RolRepository rolRepository;
  @Autowired
  private ValorDominioRepository valorDominioRepository;

  @Autowired
  private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  private List<ValoresEjesDeEntregaUsuario> valoresInterseccion(
          final Collection<ValoresEjesDeEntregaUsuario> listaA,
          final Collection<ValoresEjesDeEntregaUsuario> listaB) {
    List<ValoresEjesDeEntregaUsuario> resultado = new ArrayList<>();
    for (ValoresEjesDeEntregaUsuario valorListaA : listaA) {
      for (ValoresEjesDeEntregaUsuario valorListaB : listaB) {
        if (valorListaB.getDeliveryCatalogElementId().intValue()
                == valorListaA.getDeliveryCatalogElementId().intValue()) {
          resultado.add(valorListaA);
        }
      }
    }
    return resultado;
  }

  private ValoresEjesDeEntregaUsuario buscarValorByAttributeId(final Integer idAttr,
                                                               final List<ValoresEjesDeEntregaUsuario>
                                                                       listaValoresAttrs) {
    ValoresEjesDeEntregaUsuario retorno = null;
    for (ValoresEjesDeEntregaUsuario val : listaValoresAttrs) {
      if (val.getAxisAttributeId().intValue() == idAttr.intValue()) {
        retorno = val;
      }
    }
    return retorno;
  }

  private Boolean incluirEnResultados(final EntregaElementoCatalogo elem,
                                      final DeliveryFilter filter) {
    Boolean retorno = true;
    ElementoCatalogo elementParent = this.elementoCatalogoPadreEntregaRepository.
            findByIdAndDeletedIsNull(elem.getCatalogElementId());
    if (filter.getIdOfCatalogueElementType() != null) {
      if (elementParent.getCatalogElementTypeId() != filter.getIdOfCatalogueElementType()) {
        retorno = false;
      }
    }
    if (filter.getCatalogueElementName() != null && !"".contentEquals(
            filter.getCatalogueElementName())) {
      if (!elementParent.getName().contains(filter.getCatalogueElementName())) {
        retorno = false;
      }
    }
    return retorno;
  }

  public final void setValoresEjesDeEntregaUsuarioRepository(
          final ValoresEjesDeEntregaUsuarioRepository valoresEjesDeEntregaUsuarioRepository) {
    this.valoresEjesDeEntregaUsuarioRepository = valoresEjesDeEntregaUsuarioRepository;
  }

  public final void setValoresEjesDeElemenCatalogoUsuarioRepository(
          final ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository) {
    this.valoresEjesDeElemenCatalogoUsuarioRepository = valoresEjesDeElemenCatalogoUsuarioRepository;
  }

  public final void setEntregaElementoCatalogoRepository(
          final EntregaElementoCatalogoRepository entregaElementoCatalogoRepository) {
    this.entregaElementoCatalogoRepository = entregaElementoCatalogoRepository;
  }

  public final void setElementoCatalogoPadreEntregaRepository(
          final ElementoCatalogoRepository elementoCatalogoPadreEntregaRepository) {
    this.elementoCatalogoPadreEntregaRepository = elementoCatalogoPadreEntregaRepository;
  }

  public final void setTipoElementoCatalogoRepository(
          final TipoElementoCatalogoRepository tipoElementoCatalogoRepository) {
    this.tipoElementoCatalogoRepository = tipoElementoCatalogoRepository;
  }

  public final void setAtributoEjeRepository(final AtributoEjeRepository atributoEjeRepository) {
    this.atributoEjeRepository = atributoEjeRepository;
  }

  public final void setRolRepository(final RolRepository rolRepository) {
    this.rolRepository = rolRepository;
  }

  public final void setValorDominioRepository(
          final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }

  public final void setValorDominioCondicionadoRepository(
          final ValorDominioCondicionadoRepository valorDominioCondicionadoRepository) {
    this.valorDominioCondicionadoRepository = valorDominioCondicionadoRepository;
  }

  public final void setUsuarioRepository(final UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public final Collection<DeliveryExtended> getAllFiltered(final DeliveryExtended deliveryExtendedIn) {
    Collection<EntregaElementoCatalogo> c = new ArrayList<>();
    if (deliveryExtendedIn != null && deliveryExtendedIn.getStartDate() != null) {
      if (deliveryExtendedIn.getEndDate() == null) {
        deliveryExtendedIn.setEndDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      }
      c = this.entregaElementoCatalogoRepository.findByDeletedIsNullAndCreationDateBetween(
          deliveryExtendedIn.getStartDate(), deliveryExtendedIn.getEndDate(),
          Sort.by(Sort.Order.desc("id")));
    } else {
      Usuario usuario = (deliveryExtendedIn == null || deliveryExtendedIn.getUserId() == null) ? null
          : this.usuarioRepository.findByIdAndDeletedIsNull(deliveryExtendedIn.getUserId());
      if (usuario == null) {
        c = this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
            Sort.by(Sort.Order.desc("id")));
      } else {
        if (usuario.getRoleId().intValue() == Constantes.NUMBER_1) {
          c = this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
              Sort.by(Sort.Order.desc("id")));
        } else {
          for (UsuarioGrupo grupo : usuario.getGroups()) {
            c.addAll(this.entregaElementoCatalogoRepository.findAllByGroupIdAndDeletedIsNull(
                grupo.getId(), Sort.by(Sort.Order.desc("id"))));
          }
        }
      }
    }
    Collection<DeliveryExtended> c1 = new ArrayList<>();
    for (EntregaElementoCatalogo delivery : c) {
      ElementoCatalogo elem = this.elementoCatalogoPadreEntregaRepository.findByIdAndDeletedIsNull(
          delivery.getCatalogElementId());
      if (elem != null) {
        DeliveryExtended deliveryExtended = new DeliveryExtended();
        deliveryExtended.setId(delivery.getId());
        deliveryExtended.setCappCode(elem.getCappCode());
        deliveryExtended.setName(delivery.getName());
        deliveryExtended.setCatalogElementTypeId(elem.getCatalogElementTypeId());
        deliveryExtended.setCatalogElementId(delivery.getCatalogElementId());
        deliveryExtended.setCreationDate(delivery.getCreationDate());
        deliveryExtended.setUpdateDate(delivery.getUpdateDate());
        deliveryExtended.setAttributeValuesCollection(delivery.getAttributeValuesCollection());
        if (deliveryExtendedIn == null) {
          c1.add(deliveryExtended);
        } else if ((deliveryExtendedIn.getName() != null
                && !deliveryExtendedIn.getName().contentEquals(""))
                && !(!deliveryExtended.getName().toLowerCase()
                .contains(deliveryExtendedIn.getName().toLowerCase())
                && !deliveryExtended.getCappCode().toLowerCase()
                .contains(deliveryExtendedIn.getName().toLowerCase()))) {
          c1.add(deliveryExtended);
        }
      }
    }
    return c1;
  }
  public final Collection<DeliveryFilter> searchByFilterForList(final DeliveryFilter filter) {
    Collection<DeliveryFilter> c = new ArrayList<>();
    Collection<EntregaElementoCatalogo> elements = searchElementsByFilter(filter);
    for (EntregaElementoCatalogo element : elements) {

      DeliveryFilter eleResumen = new DeliveryFilter();
      eleResumen.setId(element.getId());
      ElementoCatalogo elementParent = this.elementoCatalogoPadreEntregaRepository.
          findByIdAndDeletedIsNull(element.getCatalogElementId());
      Integer elementType = elementParent.getCatalogElementTypeId();
      eleResumen.setIdOfCatalogueElementType(elementType);
      eleResumen.setCatalogueElementTypeName(
          this.tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(elementType).getName());
      eleResumen.setCatalogueElementName(elementParent.getName());

      Integer nameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("name")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueName = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.
              findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(nameAttrId,
                      elementParent.getId());
      eleResumen.setName(valorDomainOrUserValueName.getUserValue());

      Integer responsibleAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("responsible")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueResponsible = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      responsibleAttrId, elementParent.getId());
      eleResumen.setResponsible(valorDomainOrUserValueResponsible.getUserValue());

      Integer serviceNameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("serviceName")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueServiceName = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      serviceNameAttrId, elementParent.getId());
      eleResumen.setServiceName(valorDomainOrUserValueServiceName.getUserValue());

      Integer functionalAreaNameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("functionalAreaName")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueFunctionalAreaName = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      functionalAreaNameAttrId, elementParent.getId());
      eleResumen.setFunctionalAreaName(valorDomainOrUserValueFunctionalAreaName.getUserValue());

      Integer computerProcessingAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("computerProcessingId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueComputerProccessing = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      computerProcessingAttrId, elementParent.getId());
      eleResumen.setComputerProcessingId(valorDomainOrUserValueComputerProccessing.
              getDomainValues().iterator().next().getDomainValueId());
      eleResumen.setComputerProcessing(valorDominioRepository.findByIdAndDeletedIsNull(
          valorDomainOrUserValueComputerProccessing.getDomainValues().iterator().next().getDomainValueId()).getName());

      Integer groupAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("groupId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueGroup = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      groupAttrId, elementParent.getId());
      eleResumen.setGroupId(valorDomainOrUserValueGroup.getDomainValues().iterator().next().getDomainValueId());
      eleResumen.setGroup(valorDominioRepository.findByIdAndDeletedIsNull(
          valorDomainOrUserValueGroup.getDomainValues().iterator().next().getDomainValueId()).getName());

      Integer situationAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("situationId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueSituation = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
                      situationAttrId, elementParent.getId());
      eleResumen.setSituationId(valorDomainOrUserValueSituation.getDomainValues().iterator().next().getDomainValueId());
      eleResumen.setSituation(valorDominioRepository.findByIdAndDeletedIsNull(
          valorDomainOrUserValueSituation.getDomainValues().iterator().next().getDomainValueId()).getName());

      c.add(eleResumen);
    }
    return c;
  }

  private List<ValoresEjesDeEntregaUsuario> getListOfValuesOfElems(
          final List<ValoresEjesDeEntregaUsuario> values, final Integer valueIdSearched) {

    List<ValoresEjesDeEntregaUsuario> listaRetorno = new ArrayList<>();
    for (ValoresEjesDeEntregaUsuario val: values) {
      if (val.getDomainValues() != null) {
        for (ValorDominioDeAttrEntrega valIesimo: val.getDomainValues()) {
          if (valIesimo.getDomainValueId().intValue() == valueIdSearched.intValue()) {
            listaRetorno.add(val);
          }
        }
      }
    }
    return listaRetorno;
  }

  public final Collection<EntregaElementoCatalogo> searchElementsByFilter(final DeliveryFilter filter) {
    List<List<ValoresEjesDeEntregaUsuario>> listaDeListasAMezclar = new ArrayList<>();
    Boolean conFiltroDeAtributos = false;
    if (filter.getName() != null && !"".contentEquals(filter.getName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("name")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId,
                  filter.getName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getResponsible() != null && !"".contentEquals(filter.getResponsible())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("responsible")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getResponsible()));
      conFiltroDeAtributos = true;
    }
    if (filter.getServiceName() != null && !"".contentEquals(filter.getServiceName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("serviceName")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId,  filter.getServiceName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getFunctionalAreaName() != null && !"".contentEquals(
        filter.getFunctionalAreaName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("functionalAreaName")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getFunctionalAreaName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getComputerProcessingId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("computerProcessingId")).getId();
      List<ValoresEjesDeEntregaUsuario> listaValuesDeElem = this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
              ? new ArrayList<>() : listaValuesDeElem, filter.getComputerProcessingId()));
      conFiltroDeAtributos = true;
    }
    if (filter.getGroupId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("groupId")).getId();
      List<ValoresEjesDeEntregaUsuario> listaValuesDeElem = this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
              ? new ArrayList<>() : listaValuesDeElem, filter.getGroupId()));
      conFiltroDeAtributos = true;
    }
    if (filter.getSituationId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          DeliveryFilter.getCodeOf("situationId")).getId();
      List<ValoresEjesDeEntregaUsuario> listaValuesDeElem = this.valoresEjesDeEntregaUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
              ? new ArrayList<>() : listaValuesDeElem, filter.getSituationId()));
      conFiltroDeAtributos = true;
    }
    List<ValoresEjesDeEntregaUsuario> valoresInterseccionAcumulados = new ArrayList<>();
    for (int i = Constantes.NUMBER_0; i < listaDeListasAMezclar.size(); i++) {
      if (i + 1 == listaDeListasAMezclar.size()) {
        if (i == Constantes.NUMBER_0) {
          valoresInterseccionAcumulados.addAll(listaDeListasAMezclar.get(Constantes.NUMBER_0));
        }
      } else {
        List<ValoresEjesDeEntregaUsuario> listaIesima = listaDeListasAMezclar.get(i);
        List<ValoresEjesDeEntregaUsuario> listaIesimaMasUno = listaDeListasAMezclar.get(i + 1);
        if (listaIesima.isEmpty() || listaIesimaMasUno.isEmpty()) {
          valoresInterseccionAcumulados = new ArrayList<>();
          i = listaDeListasAMezclar.size();
        }
        List<ValoresEjesDeEntregaUsuario> valoresIntersectadoPar = valoresInterseccion(listaIesima,
                listaIesimaMasUno);
        if (valoresIntersectadoPar.isEmpty()) {
          valoresInterseccionAcumulados = new ArrayList<>();
          i = listaDeListasAMezclar.size();
        }
        if (!valoresInterseccionAcumulados.isEmpty()) {
          List<ValoresEjesDeEntregaUsuario> valoresIntersectadoParSecond = valoresInterseccion(
                  valoresInterseccionAcumulados, valoresIntersectadoPar);
          if (valoresIntersectadoParSecond.isEmpty()) {
            valoresInterseccionAcumulados = new ArrayList<>();
            i = listaDeListasAMezclar.size();
          }
          valoresInterseccionAcumulados.clear();
          valoresInterseccionAcumulados.addAll(valoresIntersectadoParSecond);
        } else {
          valoresInterseccionAcumulados.addAll(valoresIntersectadoPar);
        }
      }
    }
    Collection<Integer> idsOfElements = new ArrayList<>();
    for (ValoresEjesDeEntregaUsuario valorFound : valoresInterseccionAcumulados) {
      idsOfElements.add(valorFound.getDeliveryCatalogElementId());
    }
    Collection<EntregaElementoCatalogo> res = new ArrayList<>();
    if (!idsOfElements.isEmpty()) {
      for (Integer idElement : idsOfElements) {
        EntregaElementoCatalogo elem = this.entregaElementoCatalogoRepository.findByIdAndDeletedIsNull(idElement);
        if (filter.getIdOfCatalogueElementType() != null) {
          if (this.elementoCatalogoPadreEntregaRepository.findByIdAndDeletedIsNull(elem.getCatalogElementId()).
                  getCatalogElementTypeId().intValue() == filter.getIdOfCatalogueElementType().intValue()) {
            res.add(elem);
          }
        } else {
          res.add(elem);
        }
      }
    } else {
      if (!conFiltroDeAtributos) {
        Usuario usuario = filter.getUserId() == null ? null
                : this.usuarioRepository.findByIdAndDeletedIsNull(filter.getUserId());
        if (filter.getIdOfCatalogueElementType() != null) {
          if (usuario == null) {
            res.addAll(this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
                    Sort.by(Sort.Order.desc("id"))));
          } else {
            Rol rol = rolRepository.findByIdAndDeletedIsNull(usuario.getRoleId());
            if (rol.getId().intValue() == Constantes.NUMBER_1) {
              res.addAll(this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
                      Sort.by(Sort.Order.desc("id"))));
            } else {
              List<EntregaElementoCatalogo> elements = new ArrayList<>();
              for (UsuarioGrupo userGroup : usuario.getGroups()) {
                res.addAll(this.entregaElementoCatalogoRepository.findAllByGroupIdAndDeletedIsNull(
                                        userGroup.getGroupId(), Sort.by(Sort.Order.desc("id"))));
              }
            }
          }
        } else {
          if (usuario == null) {
            res.addAll(this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
                    Sort.by(Sort.Order.desc("id"))));
          } else {
            for (UsuarioGrupo userGroup : usuario.getGroups()) {
              res.addAll(this.entregaElementoCatalogoRepository.findAllByGroupIdAndDeletedIsNull(
                      userGroup.getGroupId(), Sort.by(Sort.Order.desc("id"))));
            }
          }
        }
      } else {
        res.addAll(this.entregaElementoCatalogoRepository.findAllByDeletedIsNull(
                Sort.by(Sort.Order.desc("id"))));
      }
    }
    return res;
  }

  public final EntregaElementoCatalogo getById(final Integer idEntrega) {
    return this.entregaElementoCatalogoRepository.findByIdAndDeletedIsNull(idEntrega);
  }

  public final Collection<EntregaElementoCatalogo> getByIdOfElement(
      final Integer idElementOfThisDelivery) {
    Collection<EntregaElementoCatalogo> c = this.entregaElementoCatalogoRepository.
            findAllByCatalogElementIdAndDeletedIsNull(idElementOfThisDelivery, Sort.by(Sort.Order.desc("id")));
    //quitemos el atributo ATTR1 o ATTR3
    for (EntregaElementoCatalogo entrega: c) {
      List newAttrs = new ArrayList();
      for (ValoresEjesDeEntregaUsuario valores: entrega.getAttributeValuesCollection()) {
        if (!this.atributoEjeRepository.findByIdAndDeletedIsNull(valores.getAxisAttributeId()).getCode().
                equals("ATTR3")) {
          newAttrs.add(valores);
        }
      }
      entrega.setAttributeValuesCollection(newAttrs);
    }
    return c;
  }

  public final EntregaElementoCatalogo getByName(final String nameOfEntrega) {
    return this.entregaElementoCatalogoRepository.findByDeletedIsNullAndNameContaining(
        nameOfEntrega);
  }

  /*@Transactional
  public final EntregaElementoCatalogo removePhysical(final Integer idEntregaElementoCat) {
    EntregaElementoCatalogo entregaElementoCat = this.entregaElementoCatalogoRepository.
            findById(idEntregaElementoCat).get();
    if (entregaElementoCat != null) {
      ValoresEjesDeEntregaUsuario filterEjes = new ValoresEjesDeEntregaUsuario();
      filterEjes.setDeliveryCatalogElementId(idEntregaElementoCat);
      Collection<ValoresEjesDeEntregaUsuario> ejesDeIdCatlogo = this.
              valoresEjesDeEntregaUsuarioRepository.findAll(Example.of(filterEjes));
      this.valoresEjesDeEntregaUsuarioRepository.deleteAll(ejesDeIdCatlogo);

      this.entregaElementoCatalogoRepository.delete(entregaElementoCat);
    }
    return entregaElementoCat;
  }*/

  @Transactional
  public final EntregaElementoCatalogo insertar(final EntregaElementoCatalogo entregaElementoCatalogo)
      throws ValidationRulesException {
    EntregaElementoCatalogo found = this.entregaElementoCatalogoRepository.findByDeletedIsNullAndNameContaining(
        entregaElementoCatalogo.getName());
    if (found != null && found.getId() != null && found.getId() > 0) {
      throw new ValidationRulesException("El nombre de entrega <" + entregaElementoCatalogo.getName()
          + "> ya existe en el Catálogo");
    }
    evaluateBusinessRules(entregaElementoCatalogo);

    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    List<ValoresEjesDeEntregaUsuario> valoresEjesyAttrs = new ArrayList<>();
    valoresEjesyAttrs.addAll(entregaElementoCatalogo.getAttributeValuesCollection());

    entregaElementoCatalogo.getAttributeValuesCollection().clear();
    entregaElementoCatalogo.setAttributeValuesCollection(new ArrayList<>());
    entregaElementoCatalogo.setCreationDate(timeStamp);
    ElementoCatalogo elementoCatalogo = this.elementoCatalogoPadreEntregaRepository.
            findByIdAndDeletedIsNull(entregaElementoCatalogo.getCatalogElementId());
    if (entregaElementoCatalogo.getGroupId() == null) {
      entregaElementoCatalogo.setGroupId(elementoCatalogo.getGroupId());
    }
    EntregaElementoCatalogo entregaElementoCatalogoSaved = this.entregaElementoCatalogoRepository.
        save(entregaElementoCatalogo);
    for (ValoresEjesDeEntregaUsuario valorUserOrDomainId : valoresEjesyAttrs) {
      valorUserOrDomainId.setId(null);
      valorUserOrDomainId.setDeliveryCatalogElementId(entregaElementoCatalogoSaved.getId());
      valorUserOrDomainId.setCreationDate(timeStamp);
      //actualizo el de BBDD con el que traiga el argumento de entrada de este method
      if (valorUserOrDomainId.getDomainValues() != null && !valorUserOrDomainId.getDomainValues().isEmpty()) {
        for (ValorDominioDeAttrEntrega valDomain: valorUserOrDomainId.getDomainValues()) {
          valDomain.setCreationDate(valorUserOrDomainId.getCreationDate());
        }
      }
    }
    entregaElementoCatalogoSaved.setAttributeValuesCollection(valoresEjesyAttrs);
    return this.entregaElementoCatalogoRepository.save(entregaElementoCatalogoSaved);
  }

  @Transactional
  public final EntregaElementoCatalogo actualizar(final EntregaElementoCatalogo entregaElementoCatalogoIn)
  throws ValidationRulesException {

    if (entregaElementoCatalogoIn.getGroupId() == null) {
      entregaElementoCatalogoIn.setGroupId(1);
    }
    EntregaElementoCatalogo elementoEntregaBBDD = this.entregaElementoCatalogoRepository.findByIdAndDeletedIsNull(
            entregaElementoCatalogoIn.getId());
    if (elementoEntregaBBDD != null) {

      evaluateBusinessRules(elementoEntregaBBDD);

      List<ValoresEjesDeEntregaUsuario> valoresEjesyAttrs = new ArrayList<>();
      valoresEjesyAttrs.addAll(entregaElementoCatalogoIn.getAttributeValuesCollection());

      entregaElementoCatalogoIn.setCatalogElementId(elementoEntregaBBDD.getCatalogElementId());
      entregaElementoCatalogoIn.setGroupId(elementoEntregaBBDD.getGroupId());
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());

      entregaElementoCatalogoIn.getAttributeValuesCollection().clear();
      entregaElementoCatalogoIn.setAttributeValuesCollection(new ArrayList<>());
      entregaElementoCatalogoIn.setCreationDate(elementoEntregaBBDD.getCreationDate());
      entregaElementoCatalogoIn.setUpdateDate(timeStamp);

      List<ValoresEjesDeEntregaUsuario> nuevosValoresAttrs = new ArrayList<>();
      for (ValoresEjesDeEntregaUsuario valorInput : valoresEjesyAttrs) {
        for (ValoresEjesDeEntregaUsuario valBBDD: elementoEntregaBBDD.getAttributeValuesCollection()) {
          if (valBBDD.getAxisAttributeId().intValue() == valorInput.getAxisAttributeId().intValue()) {
            //actualizamos el valor en el, objeto BBDD de domainvalue y uservalue
            if (valBBDD.getDomainValues() != null && !valBBDD.getDomainValues().isEmpty()) {
              for (ValorDominioDeAttrEntrega valDomainBBDD: valBBDD.getDomainValues()) {
                for (ValorDominioDeAttrEntrega valDomainInput : valorInput.getDomainValues()) {
                  if (valDomainInput.getValorEjeEntregaId().intValue()
                          == valDomainBBDD.getValorEjeEntregaId().intValue()) {
                    if (valDomainBBDD.getDomainValueId().intValue() != valDomainInput.getDomainValueId().intValue()) {
                      valDomainBBDD.setDomainValueId(valDomainInput.getDomainValueId());
                      valDomainBBDD.setUpdateDate(timeStamp);
                    }
                  }
                }
              }
            }
          }
          if (estaContenido(valorInput.getAxisAttributeId(), elementoEntregaBBDD.getAttributeValuesCollection())) {
            valBBDD.setDeliveryCatalogElementId(entregaElementoCatalogoIn.getId());
            nuevosValoresAttrs.add(valBBDD);
          } else {
            valorInput.setDeliveryCatalogElementId(entregaElementoCatalogoIn.getId());
            valorInput.setCreationDate(timeStamp);
            if (valorInput.getDomainValues() != null && !valorInput.getDomainValues().isEmpty()) {
              for (ValorDominioDeAttrEntrega valDomainInput: valorInput.getDomainValues()) {
                valDomainInput.setCreationDate(timeStamp);
              }
            }
            nuevosValoresAttrs.add(valorInput);
          }
        }
      }
      entregaElementoCatalogoIn.setAttributeValuesCollection(nuevosValoresAttrs);
      elementoEntregaBBDD = this.entregaElementoCatalogoRepository.save(entregaElementoCatalogoIn);
    }
    return elementoEntregaBBDD;
  }

  private boolean estaContenido(final Integer idAxis, final List<ValoresEjesDeEntregaUsuario> valoresEjes) {
    boolean found = false;
    for (int i = 0; i < valoresEjes.size() && !found; i++) {
      if (valoresEjes.get(i).getAxisAttributeId().intValue() == idAxis.intValue()) {
        found = true;
      }
    }
    return found;
  }
  private void evaluateBusinessRules(final EntregaElementoCatalogo entregaElementoCatalogoIn) throws
          ValidationRulesException {
    List<ValoresEjesDeEntregaUsuario> valoresEjesyAttrs = new ArrayList<>();
    valoresEjesyAttrs.addAll(entregaElementoCatalogoIn.getAttributeValuesCollection());
    for (ValoresEjesDeEntregaUsuario valorUserOrDomainId : valoresEjesyAttrs) {
      AtributoEje attrObjectSlave = this.atributoEjeRepository.findByIdAndDeletedIsNull(
              valorUserOrDomainId.getAxisAttributeId());
      if (attrObjectSlave.getAxisAttributeCollateralId() != null) {
        ValorDominio valorDominioSlave = this.valorDominioRepository.findByIdAndDeletedIsNull(
                valorUserOrDomainId.getDomainValues().iterator().next().getDomainValueId());
        String valorSelectedSlave = "";
        if (valorUserOrDomainId.getUserValue() != null
                && valorUserOrDomainId.getDomainValues().iterator().next().getDomainValueId() == null) {
          valorSelectedSlave = valorUserOrDomainId.getUserValue();
        } else {
          valorSelectedSlave = valorDominioSlave.getName();
        }
        String nombreAtributoSlave = this.atributoEjeRepository.findByIdAndDeletedIsNull(
                valorUserOrDomainId.getAxisAttributeId()).getName();
        String nombreAtributoMaster = this.atributoEjeRepository.findByIdAndDeletedIsNull(
                attrObjectSlave.getAxisAttributeCollateralId()).getName();
        ValoresEjesDeEntregaUsuario valorMasterObject = buscarValorByAttributeId(
                attrObjectSlave.getAxisAttributeCollateralId(), valoresEjesyAttrs);
        if (valorMasterObject != null) {
          ValorDominio valorDominioMasterSelected = this.valorDominioRepository.
                  findByIdAndDeletedIsNull(valorMasterObject.getDomainValues().iterator().next().getDomainValueId());
          String valorSelectedMaster = "";
          if (valorMasterObject.getUserValue() != null
                  && (valorMasterObject.getDomainValues() == null || valorMasterObject.getDomainValues().isEmpty())) {
            valorSelectedMaster = valorMasterObject.getUserValue();
          } else {
            valorSelectedMaster = this.valorDominioRepository.findByIdAndDeletedIsNull(
                    valorMasterObject.getDomainValues().iterator().next().getDomainValueId()).getName();
          }
          List<ValorDominioCondicionadoPor> valoresCondicionadosDeSlave =
                  this.valorDominioCondicionadoRepository.findAllByDeletedIsNullAndDomainValueId(
                          valorUserOrDomainId.getDomainValues().iterator().next().getDomainValueId());
          if (!valoresCondicionadosDeSlave.isEmpty()) {
            if (this.valorDominioCondicionadoRepository.
                    findByDeletedIsNullAndDomainValueCollateralIdAndDomainValueId(
                            valorDominioMasterSelected.getId(), valorDominioSlave.getId()) == null) {
              throw new ValidationRulesException(
                      "El valor <" + valorSelectedSlave + "> del atributo " + nombreAtributoSlave
                              + " no está permitido para el valor <" + valorSelectedMaster + "> del atributo "
                              + nombreAtributoMaster);
            }
          }
        }
      }
    }

  }

  private ValoresEjesDeEntregaUsuario buscarValorAtributoX(final List<ValoresEjesDeEntregaUsuario> lista,
                                                                  final Integer attrXId) {
    ValoresEjesDeEntregaUsuario retorno = null;
    boolean notFound = true;
    int i = 0;
    while (i < lista.size() && notFound) {
      ValoresEjesDeEntregaUsuario valInUserOrDomainId = lista.get(i);
      if (valInUserOrDomainId.getAxisAttributeId().intValue() == attrXId.intValue()) {
        notFound = false;
        retorno = valInUserOrDomainId;
      }
      i++;
    }
    return retorno;
  }
  @Transactional
  public final EntregaElementoCatalogo remove(final Integer idTipoEntregaElementoCat) {
    EntregaElementoCatalogo removedObject = null;
    EntregaElementoCatalogo entregaElementoCat = this.entregaElementoCatalogoRepository.
        findByIdAndDeletedIsNull(
        idTipoEntregaElementoCat);
    if (entregaElementoCat != null) {
      entregaElementoCat.setDeleted(Constantes.NUMBER_1);
      entregaElementoCat.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
      EntregaElementoCatalogo entregaDeleted;
      int veces = Constantes.NUMBER_0;
      do {
        String nameDeleted = "borrado[".concat(String.valueOf(++veces).concat("]:"))
            .concat(entregaElementoCat.getName());
        if (nameDeleted.length() > Constantes.POSITION_FIFTY) {
          nameDeleted = nameDeleted.substring(Constantes.NUMBER_0,
              Constantes.POSITION_FORTY_NINE);
        }
        entregaDeleted = this.entregaElementoCatalogoRepository.findByNameEquals(nameDeleted);
      } while (entregaDeleted != null);

      String preffix = "borrado[" + veces + "]:";
      String newname = preffix.concat(entregaElementoCat.getName());
      if (newname.length() > Constantes.POSITION_FIFTY) {
        newname = newname.substring(Constantes.NUMBER_0, Constantes.POSITION_FORTY_NINE);
      }
      entregaElementoCat.setName(newname);
      for (ValoresEjesDeEntregaUsuario valorUserOrDomainId : entregaElementoCat.
          getAttributeValuesCollection()) {
        valorUserOrDomainId.setDeleted(Constantes.NUMBER_1);
        valorUserOrDomainId.setUpdateDate(entregaElementoCat.getUpdateDate());
      }
      removedObject = this.entregaElementoCatalogoRepository.saveAndFlush(entregaElementoCat);
    }
    return removedObject;
  }


}
