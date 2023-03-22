package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.ValorDominioDeAttrElemCat;
import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.AtributoEjePorTipoElemento;
import giss.mad.catalogo.model.auxejes.CatalogueNode;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.ElementoCatalogo;
import giss.mad.catalogo.model.Rol;
import giss.mad.catalogo.model.Usuario;
import giss.mad.catalogo.model.UsuarioGrupo;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import giss.mad.catalogo.model.ValoresEjesDeElemenCatalogoUsuario;
import giss.mad.catalogo.model.TipoElementoCatalogo;
import giss.mad.catalogo.model.filters.CatalogueElementFilter;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.GrupoRepository;
import giss.mad.catalogo.repository.ElementoCatalogoRepository;
import giss.mad.catalogo.repository.EntregaElementoCatalogoRepository;
import giss.mad.catalogo.repository.RolRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
import giss.mad.catalogo.repository.UsuarioRepository;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
import giss.mad.catalogo.repository.ValoresEjesDeElemenCatalogoUsuarioRepository;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


import java.sql.Timestamp;

import javax.transaction.Transactional;

@Service
@Component
public class ElementoCatalogoService  {

  @Autowired
  private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;

  @Autowired
  private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;

  @Autowired
  private EntregaElementoCatalogoRepository entregaElementoCatalogoRepository;
  @Autowired
  private ElementoCatalogoRepository elemenCatalogoRepository;

  @Autowired
  private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

  @Autowired
  private GrupoRepository grupoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private RolRepository rolRepository;

  @Autowired
  private AtributoEjeRepository atributoEjeRepository;

  @Autowired
  private ValorDominioRepository valorDominioRepository;

  @Autowired
  private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;


  public final void setValoresEjesDeElemenCatalogoUsuarioRepository(
          final ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository) {
    this.valoresEjesDeElemenCatalogoUsuarioRepository = valoresEjesDeElemenCatalogoUsuarioRepository;
  }

  public final void setEntregaElementoCatalogoRepository(final EntregaElementoCatalogoRepository
                                                                 entregaElementoCatalogoRepository) {
    this.entregaElementoCatalogoRepository = entregaElementoCatalogoRepository;
  }

  public final void setElemenCatalogoRepository(final ElementoCatalogoRepository elemenCatalogoRepository) {
    this.elemenCatalogoRepository = elemenCatalogoRepository;
  }

  public final void setGrupoRepository(final GrupoRepository grupoRepository) {
    this.grupoRepository = grupoRepository;
  }

  public final void setTipoElementoCatalogoRepository(
          final TipoElementoCatalogoRepository tipoElementoCatalogoRepository) {
    this.tipoElementoCatalogoRepository = tipoElementoCatalogoRepository;
  }

  public final void setUsuarioRepository(final UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public final void setRolRepository(final RolRepository rolRepository) {
    this.rolRepository = rolRepository;
  }

  public final void setAtributoEjeRepository(final AtributoEjeRepository atributoEjeRepository) {
    this.atributoEjeRepository = atributoEjeRepository;
  }

  public final void setValorDominioRepository(final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }
  public final void setAtributoEjePorTipoElementoRepository(
          final AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository) {
    this.atributoEjePorTipoElementoRepository = atributoEjePorTipoElementoRepository;
  }
  public final void setValorDominioCondicionadoRepository(
          final ValorDominioCondicionadoRepository valorDominioCondicionadoRepository) {
    this.valorDominioCondicionadoRepository = valorDominioCondicionadoRepository;
  }


  private List<ValoresEjesDeElemenCatalogoUsuario> valoresInterseccion(
          final Collection<ValoresEjesDeElemenCatalogoUsuario> listaA,
          final Collection<ValoresEjesDeElemenCatalogoUsuario> listaB) {
    List<ValoresEjesDeElemenCatalogoUsuario> resultado = new ArrayList<>();
    for (ValoresEjesDeElemenCatalogoUsuario valorListaA : listaA) {
      for (ValoresEjesDeElemenCatalogoUsuario valorListaB : listaB) {
        if (valorListaB.getCatalogElementId().intValue() == valorListaA.getCatalogElementId().intValue()) {
          resultado.add(valorListaA);
        }
      }
    }
    return resultado;
  }

  private List<ValoresEjesDeElemenCatalogoUsuario> getMergeList(
          final List<List<ValoresEjesDeElemenCatalogoUsuario>> listaDeListasAMezclar) {
    List<ValoresEjesDeElemenCatalogoUsuario> valoresInterseccionAcumulados = new ArrayList<>();
    for (int i = Constantes.NUMBER_0; i < listaDeListasAMezclar.size(); i++) {
      if ((i + 1) == listaDeListasAMezclar.size()) {
        if (i == Constantes.NUMBER_0) {
          valoresInterseccionAcumulados.addAll(listaDeListasAMezclar.get(Constantes.NUMBER_0));
        }
      } else {
        List<ValoresEjesDeElemenCatalogoUsuario> listaIesima = listaDeListasAMezclar.get(i);
        List<ValoresEjesDeElemenCatalogoUsuario> listaIesimaMasUno = listaDeListasAMezclar.get(i + 1);
        if (listaIesima.isEmpty() || listaIesimaMasUno.isEmpty()) {
          valoresInterseccionAcumulados = new ArrayList<>();
          i = listaDeListasAMezclar.size();
        }
        List<ValoresEjesDeElemenCatalogoUsuario> valoresIntersectadoPar = valoresInterseccion(
                listaIesima, listaIesimaMasUno);
        if (valoresIntersectadoPar.isEmpty()) {
          valoresInterseccionAcumulados = new ArrayList<>();
          i = listaDeListasAMezclar.size();
        }
        if (!valoresInterseccionAcumulados.isEmpty()) {
          List<ValoresEjesDeElemenCatalogoUsuario> valoresIntersectadoPar2 = valoresInterseccion(
                  valoresInterseccionAcumulados, valoresIntersectadoPar);
          if (valoresIntersectadoPar2.isEmpty()) {
            valoresInterseccionAcumulados = new ArrayList<>();
            i = listaDeListasAMezclar.size();
          }
          valoresInterseccionAcumulados.clear();
          valoresInterseccionAcumulados.addAll(valoresIntersectadoPar2);
        } else {
          valoresInterseccionAcumulados.addAll(valoresIntersectadoPar);
        }
      }
    }
    return valoresInterseccionAcumulados;
  }
    public final ElementoCatalogo getById(final Integer idElementoCat) {
    return this.elemenCatalogoRepository.findByIdAndDeletedIsNull(idElementoCat);
  }
  public final ElementoCatalogo getByName(final String nameOfElemenCat) {
    return this.elemenCatalogoRepository.findByNameAndDeletedIsNull(nameOfElemenCat);
  }
  public final ElementoCatalogo getByCappCode(final String cappCode) {
    return this.elemenCatalogoRepository.findByCappCodeAndDeletedIsNull(cappCode);
  }
  public final List<ElementoCatalogo> getByCollateralId(final Integer idParent) {
    ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(idParent);
    List<ElementoCatalogo> retorno = new ArrayList<>();
    if (parent != null) {
       retorno = this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementCollateralId(parent.getId(),
               Sort.by(Sort.Order.desc("id")));
      for (ElementoCatalogo elem : retorno) {
        if (elem.getCatalogElementCollateralId() != null) {
          ElementoCatalogo parentEl = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
                  elem.getCatalogElementCollateralId());
          parentEl.setSubElements(null);
          parentEl.setAttributeValuesCollection(null);
          parentEl.setDeliveryCollection(null);
          elem.setParentElement(parentEl);
        }
      }
    }
    return retorno;
  }
  public final Collection<ElementoCatalogo> getAllByGroup(final Integer userId) {
    List<ElementoCatalogo> elements = null;
    if (userId == null) {
      elements = this.elemenCatalogoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
    } else {
      elements = new ArrayList<>();
      Usuario usuario = this.usuarioRepository.findByIdAndDeletedIsNull(userId);
      if (usuario == null) {
        elements = this.elemenCatalogoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
      } else {
        for (UsuarioGrupo userGroup : usuario.getGroups()) {
          elements.addAll(
                  this.elemenCatalogoRepository.findAllByGroupIdAndDeletedIsNull(userGroup.getGroupId(),
                          Sort.by(Sort.Order.desc("id"))));
        }
      }
    }
    for (ElementoCatalogo elem: elements) {
      if (elem.getCatalogElementCollateralId() != null) {
        ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
                elem.getCatalogElementCollateralId());
        parent.setSubElements(null);
        parent.setAttributeValuesCollection(null);
        parent.setDeliveryCollection(null);
        elem.setParentElement(parent);
      }
    }
    return elements;
  }
  public final Collection<CatalogueElementFilter> searchByFilterForList(final CatalogueElementFilter filter) {
    Collection<CatalogueElementFilter> c = new ArrayList<>();
    Collection<ElementoCatalogo> elements = searchElementsByFilter(filter);
    for (ElementoCatalogo element : elements) {
      CatalogueElementFilter eleResumen = new CatalogueElementFilter();
      eleResumen.setId(element.getId());
      eleResumen.setIdOfCatalogueElementType(element.getCatalogElementTypeId());
      eleResumen.setCatalogueElementTypeName(
          tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(element.
                  getCatalogElementTypeId()).getName());
      Integer preffixAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("preffix")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValuePreffix = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(preffixAttrId, element.getId());
      eleResumen.setPreffix(valorDomainOrUserValuePreffix.getUserValue());
      Integer codeAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("code")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueCode = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(codeAttrId, element.getId());
      eleResumen.setCode(valorDomainOrUserValueCode.getUserValue());
      Integer nameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("name")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueName = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
          nameAttrId, element.getId());
      eleResumen.setName(valorDomainOrUserValueName.getUserValue());
      Integer responsibleAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("responsible")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueResponsible = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(responsibleAttrId,
              element.getId());
      if (valorDomainOrUserValueResponsible != null) {
        eleResumen.setResponsible(valorDomainOrUserValueResponsible.getUserValue());
      }
      Integer serviceNameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("serviceName")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueServiceName = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(serviceNameAttrId,
              element.getId());
      if (valorDomainOrUserValueServiceName != null) {
        eleResumen.setServiceName(valorDomainOrUserValueServiceName.getUserValue());
      }
      Integer functionalAreaNameAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("functionalAreaName")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueFunctionalAreaName = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
          functionalAreaNameAttrId, element.getId());
      if (valorDomainOrUserValueFunctionalAreaName != null) {
        eleResumen.setFunctionalAreaName(valorDomainOrUserValueFunctionalAreaName.getUserValue());
      }
      Integer computerProcessingAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("computerProcessingId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueComputerProccessing = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
          computerProcessingAttrId, element.getId());
      if (valorDomainOrUserValueComputerProccessing != null) {
        eleResumen.setComputerProcessingId(
            valorDomainOrUserValueComputerProccessing.getDomainValues().iterator().next().getDomainValueId());
        eleResumen.setComputerProcessing(valorDominioRepository.findByIdAndDeletedIsNull(
            valorDomainOrUserValueComputerProccessing.getDomainValues().iterator().next().getDomainValueId()).
                getName());
      }
      Integer groupAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("groupId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueGroup = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
          groupAttrId, element.getId());
      if (valorDomainOrUserValueGroup != null) {
        eleResumen.setGroupId(valorDomainOrUserValueGroup.getDomainValues().iterator().next().getDomainValueId());
        eleResumen.setGroup(valorDominioRepository.findByIdAndDeletedIsNull(
            valorDomainOrUserValueGroup.getDomainValues().iterator().next().getDomainValueId()).getName());
      }
      Integer situationAttrId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("situationId")).getId();
      ValoresEjesDeElemenCatalogoUsuario valorDomainOrUserValueSituation = this.
          valoresEjesDeElemenCatalogoUsuarioRepository.
          findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
          situationAttrId, element.getId());
      if (valorDomainOrUserValueSituation != null) {
        eleResumen.setSituationId(valorDomainOrUserValueSituation.getDomainValues().iterator().next().
                getDomainValueId());
        eleResumen.setSituation(valorDominioRepository.findByIdAndDeletedIsNull(
            valorDomainOrUserValueSituation.getDomainValues().iterator().next().getDomainValueId()).getName());
      }
      c.add(eleResumen);
    }

    return c;
  }

  private List<ValoresEjesDeElemenCatalogoUsuario> getListOfValuesOfElems(
          final List<ValoresEjesDeElemenCatalogoUsuario> values, final Integer valueIdSearched) {

    List<ValoresEjesDeElemenCatalogoUsuario> listaRetorno = new ArrayList<>();
    for (ValoresEjesDeElemenCatalogoUsuario val: values) {
      if (val.getDomainValues() != null) {
        for (ValorDominioDeAttrElemCat valIesimo: val.getDomainValues()) {
          if (valIesimo.getDomainValueId().intValue() == valueIdSearched.intValue()) {
            listaRetorno.add(val);
          }
        }
      }
    }
    return listaRetorno;
  }

  public final Collection<ElementoCatalogo> searchElementsByFilter(final CatalogueElementFilter filter) {
    List<List<ValoresEjesDeElemenCatalogoUsuario>> listaDeListasAMezclar = new ArrayList<>();
    Boolean conFiltroDeAtributos = false;
    if (filter.getPreffix() != null && !"".contentEquals(filter.getPreffix())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("preffix")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getPreffix()));
      conFiltroDeAtributos = true;
    }
    if (filter.getName() != null && !"".contentEquals(filter.getName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("name")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getCode() != null && !"".contentEquals(filter.getCode())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("code")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getCode()));
      conFiltroDeAtributos = true;
    }
    if (filter.getResponsible() != null && !"".contentEquals(filter.getResponsible())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("responsible")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getResponsible()));
      conFiltroDeAtributos = true;
    }
    if (filter.getServiceName() != null && !"".contentEquals(filter.getServiceName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("serviceName")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getServiceName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getFunctionalAreaName() != null && !"".contentEquals(
        filter.getFunctionalAreaName())) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("functionalAreaName")).getId();
      listaDeListasAMezclar.add(this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(axisId, filter.getFunctionalAreaName()));
      conFiltroDeAtributos = true;
    }
    if (filter.getComputerProcessingId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("computerProcessingId")).getId();
      List<ValoresEjesDeElemenCatalogoUsuario> listaValuesDeElem = this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
                      ? new ArrayList<>() : listaValuesDeElem, filter.getComputerProcessingId()));
      conFiltroDeAtributos = true;
    }
    if (filter.getGroupId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("groupId")).getId();
      List<ValoresEjesDeElemenCatalogoUsuario> listaValuesDeElem = this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
                      ? new ArrayList<>() : listaValuesDeElem, filter.getGroupId()));
      conFiltroDeAtributos = true;
    }
    if (filter.getSituationId() != null) {
      Integer axisId = this.atributoEjeRepository.findByCodeAndDeletedIsNull(
          CatalogueElementFilter.getCodeOf("situationId")).getId();
      List<ValoresEjesDeElemenCatalogoUsuario> listaValuesDeElem = this.valoresEjesDeElemenCatalogoUsuarioRepository.
              findAllByAxisAttributeIdAndDeletedIsNull(axisId);
      listaDeListasAMezclar.add(getListOfValuesOfElems(listaValuesDeElem == null
                      ? new ArrayList<>() : listaValuesDeElem, filter.getSituationId()));
      conFiltroDeAtributos = true;
    }
    List<ValoresEjesDeElemenCatalogoUsuario> valoresInterseccionAcumulados =
        getMergeList(listaDeListasAMezclar);
    Collection<Integer> idsOfElements = new ArrayList<>();
    for (ValoresEjesDeElemenCatalogoUsuario valorFound : valoresInterseccionAcumulados) {
      idsOfElements.add(valorFound.getCatalogElementId());
    }
    Collection<ElementoCatalogo> res = new ArrayList<>();
    if (!idsOfElements.isEmpty()) {
      for (Integer idElement : idsOfElements) {
        ElementoCatalogo elem = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(idElement);
        if (filter.getIdOfCatalogueElementType() != null) {
          if (elem.getCatalogElementTypeId().intValue() == filter.getIdOfCatalogueElementType().intValue()) {
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
            res.addAll(this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementTypeId(
                filter.getIdOfCatalogueElementType(), Sort.by(Sort.Order.desc("id"))));
          } else {
            Rol rol = rolRepository.findByIdAndDeletedIsNull(usuario.getRoleId());
            if (rol.getId().intValue() == Constantes.NUMBER_1) {
              res.addAll(this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementTypeId(
                      filter.getIdOfCatalogueElementType(), Sort.by(Sort.Order.desc("id"))));
            } else {
              List<ElementoCatalogo> elements = new ArrayList<>();
              for (UsuarioGrupo userGroup : usuario.getGroups()) {
                res.addAll(this.elemenCatalogoRepository.findAllByDeletedIsNullAndGroupIdAndCatalogElementTypeId(
                        userGroup.getGroupId(), filter.getIdOfCatalogueElementType(),
                            Sort.by(Sort.Order.desc("id"))));
              }
            }
          }
        } else {
          if (usuario == null) {
            res.addAll(this.elemenCatalogoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id"))));
          } else {
            List<ElementoCatalogo> elements = new ArrayList<>();
            for (UsuarioGrupo userGroup : usuario.getGroups()) {
              res.addAll(this.elemenCatalogoRepository.findAllByGroupIdAndDeletedIsNull(
                  userGroup.getGroupId(), Sort.by(Sort.Order.desc("id"))));
            }
          }
        }
      } else {
        Usuario usuario = filter.getUserId() == null ? null
                : this.usuarioRepository.findByIdAndDeletedIsNull(filter.getUserId());
        if (usuario == null) {
          res.addAll(this.elemenCatalogoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id"))));
        } else {
          List<ElementoCatalogo> elements = new ArrayList<>();
          for (UsuarioGrupo userGroup : usuario.getGroups()) {
            res.addAll(this.elemenCatalogoRepository.findAllByGroupIdAndDeletedIsNull(
                    userGroup.getGroupId(), Sort.by(Sort.Order.desc("id"))));
          }
        }
      }
    }
    for (ElementoCatalogo elem: res) {
      if (elem.getCatalogElementCollateralId() != null) {
        ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
                elem.getCatalogElementCollateralId());
        parent.setSubElements(null);
        parent.setAttributeValuesCollection(null);
        parent.setDeliveryCollection(null);
        elem.setParentElement(parent);
      }
    }
    return res;
  }

  public final List<ElementoCatalogo> getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(
      final Integer userId, final Integer idTypeOfCatalogue) {
    List<ElementoCatalogo> listOfElemens = new ArrayList<>();
    if (userId == null) {
      listOfElemens = this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementTypeId(
          idTypeOfCatalogue, Sort.by(Sort.Order.desc("id")));
    } else {
      Usuario usuario = this.usuarioRepository.findByIdAndDeletedIsNull(userId);
      if (usuario == null) {
        listOfElemens = this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementTypeId(
                idTypeOfCatalogue, Sort.by(Sort.Order.desc("id")));
      } else {
        Rol rol = rolRepository.findByIdAndDeletedIsNull(usuario.getRoleId());
        if (rol.getId().intValue() == Constantes.NUMBER_1) { //"admin"
          listOfElemens = this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementTypeId(
                  idTypeOfCatalogue, Sort.by(Sort.Order.desc("id")));
        } else {
          for (UsuarioGrupo userGroup : usuario.getGroups()) {
            listOfElemens.addAll(
                    this.elemenCatalogoRepository.findAllByDeletedIsNullAndGroupIdAndCatalogElementTypeId(
                            userGroup.getGroupId(),
                            idTypeOfCatalogue, Sort.by(Sort.Order.desc("id"))));
          }
        }
      }
    }
    List<ElementoCatalogo> cResult = new ArrayList<>();
    for (ElementoCatalogo elementoCatalogo : listOfElemens) {
      elementoCatalogo.setDeliveryCollection(
          this.entregaElementoCatalogoRepository.findAllByCatalogElementIdAndDeletedIsNull(
              elementoCatalogo.getId(), Sort.by(Sort.Order.desc("id"))));
      elementoCatalogo.setSubElements(
          this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementCollateralId(
              elementoCatalogo.getId(), Sort.by(Sort.Order.desc("id"))));

      // añadimos atributos que hayan sido asignados a este tipo/nivel de elemento de catalogo y no viajen en la
      List<AtributoEjePorTipoElemento> attrs = new ArrayList<>();
      Integer forCatalogue = Constantes.NUMBER_1;
      List<AtributoEjePorTipoElemento> result1 = this.atributoEjePorTipoElementoRepository.
              findAllByDeletedIsNullAndCatalogElementTypeIdAndForCatalogue(elementoCatalogo.getCatalogElementTypeId(),
                      forCatalogue, Sort.by(Sort.Order.asc("axisAttributeId")));
      List<Integer> aExcluir = new ArrayList<>();
      aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1").getId());
      aExcluir.add(this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3").getId());
      for (AtributoEjePorTipoElemento attr : result1) {
        if (!aExcluir.contains(attr.getAxisAttributeId().intValue())) {
          attrs.add(attr);
        }
      }
      for (AtributoEjePorTipoElemento attr : attrs) {
        if (!estaContenido(attr.getAxisAttributeId(), elementoCatalogo.getAttributeValuesCollection())) {
          ValoresEjesDeElemenCatalogoUsuario newvalueAttr = new ValoresEjesDeElemenCatalogoUsuario();
          newvalueAttr.setCatalogElementId(elementoCatalogo.getId());
          newvalueAttr.setAxisAttributeId(attr.getAxisAttributeId());
          newvalueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
          List<ValorDominioDeAttrElemCat> newValuesOfDomain = new ArrayList<>();
          newvalueAttr.setDomainValues(newValuesOfDomain);
          elementoCatalogo.getAttributeValuesCollection().add(newvalueAttr);
        }
      }
      if (elementoCatalogo.getCatalogElementCollateralId() != null) {
        ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
                elementoCatalogo.getCatalogElementCollateralId());
        parent.setSubElements(null);
        parent.setAttributeValuesCollection(null);
        parent.setDeliveryCollection(null);
        elementoCatalogo.setParentElement(parent);
      }
      cResult.add(elementoCatalogo);
    }
    return cResult;
  }

  private boolean estaContenido(final Integer idAxis, final List<ValoresEjesDeElemenCatalogoUsuario> valoresEjes) {
    boolean found = false;
    for (int i = 0; i < valoresEjes.size() && !found; i++) {
      if (valoresEjes.get(i).getAxisAttributeId().intValue() == idAxis.intValue()) {
        found = true;
      }
    }
    return found;
  }

  public final List<ElementoCatalogo> getFreeElementsByUserGroupIdAndCatalogueTypeId(final Integer userId,
      final Integer idparentCatalogueType) {
    List<ElementoCatalogo> c1 = new ArrayList<>();
    if (userId == null) {
      c1 = this.elemenCatalogoRepository.
          findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndCatalogElementTypeId(
          idparentCatalogueType, Sort.by(Sort.Order.desc("id")));
    } else {
      Usuario usuario = this.usuarioRepository.findByIdAndDeletedIsNull(userId);
      if (usuario == null) {
        c1 = this.elemenCatalogoRepository.
                  findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndCatalogElementTypeId(
                        idparentCatalogueType, Sort.by(Sort.Order.desc("id")));
      } else {
        Rol rol = rolRepository.findByIdAndDeletedIsNull(usuario.getRoleId());
        if (rol.getName().toLowerCase().contains("administrador")) {
          c1 = this.elemenCatalogoRepository.
                  findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndCatalogElementTypeId(
                          idparentCatalogueType, Sort.by(Sort.Order.desc("id")));
        } else {
          for (UsuarioGrupo userGroup : usuario.getGroups()) {
            c1.addAll(
                    this.elemenCatalogoRepository.
                            findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndGroupIdAndCatalogElementTypeId(
                                    userGroup.getGroupId(),
                                    idparentCatalogueType, Sort.by(Sort.Order.desc("id"))));
          }
        }
      }
    }
    List<ElementoCatalogo> cResult = new ArrayList<>();
    for (ElementoCatalogo elementoCatalogo : c1) {
      elementoCatalogo.setDeliveryCollection(
          this.entregaElementoCatalogoRepository.findAllByCatalogElementIdAndDeletedIsNull(
              elementoCatalogo.getId(), Sort.by(Sort.Order.desc("id"))));
      elementoCatalogo.setSubElements(
          this.elemenCatalogoRepository.findAllByDeletedIsNullAndCatalogElementCollateralId(
              elementoCatalogo.getId(), Sort.by(Sort.Order.desc("id"))));
      cResult.add(elementoCatalogo);
    }
    return cResult;
  }


  private void recursiveHierarchy(final List<CatalogueNode> listaOut, final ElementoCatalogo elemCatalogo,
                                  final Integer seachById) {

    CatalogueNode hierarchyLevel0 = new CatalogueNode();
    hierarchyLevel0.setId(elemCatalogo.getId());
    String preffix = elemCatalogo.getId().intValue() == seachById.intValue() ? "[*]" : "";
    hierarchyLevel0.setName(preffix.concat(elemCatalogo.getName()));
    hierarchyLevel0.setCreationDate(elemCatalogo.getCreationDate());
    hierarchyLevel0.setCappCode(elemCatalogo.getCappCode());
    hierarchyLevel0.setType(this.tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(elemCatalogo.
            getCatalogElementTypeId()).getName());
    hierarchyLevel0.setGroup(this.grupoRepository.findByIdAndDeletedIsNull(elemCatalogo.getGroupId()).getName());
    List<String> tags = new ArrayList<>();
    tags.add(Constantes.TAGS_NODE[elemCatalogo.getCatalogElementTypeId() - 1]);
    hierarchyLevel0.setTags(tags);
    hierarchyLevel0.setImg(Constantes.IMG_NODE[elemCatalogo.getCatalogElementTypeId() - 1]);
    int pid = elemCatalogo.getCatalogElementCollateralId() == null ? -1 : elemCatalogo.getCatalogElementCollateralId();
    hierarchyLevel0.setPid(pid);

    listaOut.add(hierarchyLevel0);

    if (elemCatalogo.getSubElements() == null || elemCatalogo.getSubElements().isEmpty()) {
      return;
    }

    for (ElementoCatalogo subElement : elemCatalogo.getSubElements()) {
      recursiveHierarchy(listaOut, subElement, seachById);
    }

  }

  private ElementoCatalogo getElementTop(final ElementoCatalogo elemCatalogo) {
    ElementoCatalogo searchTop = elemCatalogo;
    while (searchTop.getCatalogElementCollateralId() != null) {
      searchTop = getById(searchTop.getCatalogElementCollateralId());
    }
    return searchTop;
  }

  public final List<CatalogueNode> getHierarchyById(final Integer idOfElement) {
    ElementoCatalogo elementRoot = getElementTop(this.elemenCatalogoRepository.findByIdAndDeletedIsNull(idOfElement));
    List<CatalogueNode> listOfNodes = new ArrayList<>();
    recursiveHierarchy(listOfNodes, elementRoot, idOfElement);
    return listOfNodes;
  }

  @Transactional
  public final ElementoCatalogo insertar(final ElementoCatalogo elementoCatIn) throws ValidationRulesException {
    if (elementoCatIn.getParentElement() != null && elementoCatIn.getCatalogElementCollateralId() == null) {
      elementoCatIn.setCatalogElementCollateralId(elementoCatIn.getParentElement().getId());
    }
    evaluateBusinessRules(elementoCatIn, true);
    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    elementoCatIn.setCreationDate(timeStamp);
    if (elementoCatIn.getGroupId() == null) {
      elementoCatIn.setGroupId(Constantes.NUMBER_1);
    }

    List<ElementoCatalogo> actualChildrenIds = new ArrayList<>();
    if (elementoCatIn.getSubElements() != null && !elementoCatIn.getSubElements().isEmpty()) {
      actualChildrenIds.addAll(elementoCatIn.getSubElements());
      elementoCatIn.getSubElements().clear();
      elementoCatIn.setSubElements(new ArrayList<>());
    } else {
      elementoCatIn.setSubElements(new ArrayList<>());
    }
    elementoCatIn.setDeliveryCollection(new ArrayList<>());

    AtributoEje ejePrefijo = this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1");
    AtributoEje ejeCodigo = this.atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3");
    boolean existATTR1 = false;
    boolean existATTR3 = false;
    for (ValoresEjesDeElemenCatalogoUsuario value : elementoCatIn.getAttributeValuesCollection()) {
      if (ejePrefijo != null && value.getAxisAttributeId().intValue() == ejePrefijo.getId().intValue()) {
        existATTR1 = true;
      } else if (ejeCodigo != null && value.getAxisAttributeId().intValue() == ejeCodigo.getId().intValue()) {
        existATTR3 = true;
      }
    }
    if (!existATTR1 && ejePrefijo != null) {
      ValoresEjesDeElemenCatalogoUsuario valorPrefijoAttr = new ValoresEjesDeElemenCatalogoUsuario();
      valorPrefijoAttr.setAxisAttributeId(ejePrefijo.getId());
      valorPrefijoAttr.setCreationDate(timeStamp);
      valorPrefijoAttr.setUserValue(elementoCatIn.getCappCode());
      elementoCatIn.getAttributeValuesCollection().add(valorPrefijoAttr);
    }
    if (!existATTR3 && ejeCodigo != null) {
      ValoresEjesDeElemenCatalogoUsuario valorCodigoAttr = new ValoresEjesDeElemenCatalogoUsuario();
      valorCodigoAttr.setAxisAttributeId(ejeCodigo.getId());
      valorCodigoAttr.setCreationDate(timeStamp);
      valorCodigoAttr.setUserValue(elementoCatIn.getCappCode());
      elementoCatIn.getAttributeValuesCollection().add(valorCodigoAttr);
    }

    List<ValoresEjesDeElemenCatalogoUsuario> actualvaluesAttrs = new ArrayList<>();
    actualvaluesAttrs.addAll(elementoCatIn.getAttributeValuesCollection());
    elementoCatIn.setAttributeValuesCollection(null);
    ElementoCatalogo elemSaved = this.elemenCatalogoRepository.save(elementoCatIn);

    List<ValoresEjesDeElemenCatalogoUsuario> valuesAttrsConDomainVals = new ArrayList<>();
    for (ValoresEjesDeElemenCatalogoUsuario valorUserOrDomainId : actualvaluesAttrs) {
      valorUserOrDomainId.setCreationDate(timeStamp);
      valorUserOrDomainId.setCatalogElementId(elemSaved.getId());
      if (valorUserOrDomainId.getDomainValues() != null) {
        for (ValorDominioDeAttrElemCat valDomain: valorUserOrDomainId.getDomainValues()) {
          valDomain.setCreationDate(timeStamp);
        }
      }
      valuesAttrsConDomainVals.add(valorUserOrDomainId);
    }
    elemSaved.setAttributeValuesCollection(actualvaluesAttrs);
    elemSaved = this.elemenCatalogoRepository.save(elemSaved);

    if (elemSaved.getCatalogElementCollateralId() != null) {
      ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
              elemSaved.getCatalogElementCollateralId());
      parent.setSubElements(null);
      parent.setAttributeValuesCollection(null);
      parent.setDeliveryCollection(null);
      elemSaved.setParentElement(parent);
    }
    return elemSaved;
  }


  @Transactional
  public final ElementoCatalogo onlyUpdate(final ElementoCatalogo elementoCatIn) {
    elementoCatIn.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    return this.elemenCatalogoRepository.save(elementoCatIn);
  }


  @Transactional
  public final ElementoCatalogo actualizar(final ElementoCatalogo elementoCatIn) throws ValidationRulesException {
    if (elementoCatIn.getGroupId() == null) {
       elementoCatIn.setGroupId(1);
    }
    if (elementoCatIn.getParentElement() != null && elementoCatIn.getCatalogElementCollateralId() == null) {
      elementoCatIn.setCatalogElementCollateralId(elementoCatIn.getParentElement().getId());
    }
    evaluateBusinessRules(elementoCatIn, false);
    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    ElementoCatalogo elementoCatBBDD = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(elementoCatIn.getId());
    if (elementoCatBBDD != null) {
      elementoCatIn.setCatalogElementTypeId(elementoCatBBDD.getCatalogElementTypeId());
      elementoCatIn.setCreationDate(elementoCatBBDD.getCreationDate());
      elementoCatIn.setUpdateDate(timeStamp);
      elementoCatIn.setDeliveryCollection(elementoCatBBDD.getDeliveryCollection());

      List<ElementoCatalogo> newSubElements = new ArrayList<>();
      if (elementoCatIn.getSubElements() != null && !elementoCatIn.getSubElements().isEmpty()) {
        for (ElementoCatalogo elementChild : elementoCatIn.getSubElements()) {
          newSubElements.add(this.elemenCatalogoRepository.findByIdAndDeletedIsNull(elementChild.getId()));
        }
        elementoCatIn.getSubElements().clear();
        elementoCatIn.setSubElements(newSubElements);
      }
      List<ValoresEjesDeElemenCatalogoUsuario> atrValuesAcumulated = new ArrayList<>();
      for (ValoresEjesDeElemenCatalogoUsuario valorInputBBDD : elementoCatBBDD.getAttributeValuesCollection()) {
        // si ese atributo está en la lista de atributos de elemento de entrada, lo actualizo

        ValoresEjesDeElemenCatalogoUsuario valuedeBBDDExisteEnPantalla =
                buscarValorByAttributeId(valorInputBBDD.getAxisAttributeId(),
                        elementoCatIn.getAttributeValuesCollection());
        if (valuedeBBDDExisteEnPantalla != null) {
          // entonces actualizo el userValue y la lista de domainValues de ese elemento en el de BBDD
          valorInputBBDD.setUserValue(valuedeBBDDExisteEnPantalla.getUserValue());
          valorInputBBDD.getDomainValues().clear();
          if (valorInputBBDD.getDomainValues() != null) {
            valorInputBBDD.getDomainValues().addAll(valuedeBBDDExisteEnPantalla.getDomainValues());
            for (ValorDominioDeAttrElemCat valDomainInput: valorInputBBDD.getDomainValues()) {
              valDomainInput.setCreationDate(timeStamp);
              valDomainInput.setValorEjeElemCatId(valorInputBBDD.getId());
            }
          }
        }
        atrValuesAcumulated.add(valorInputBBDD);
      }

      for (ValoresEjesDeElemenCatalogoUsuario valorInputScreen : elementoCatIn.getAttributeValuesCollection()) {
        // si ese atributo que viene de pantalla no está en la lista de atributos acumulada entonces toca incluirlo
        if (buscarValorByAttributeId(valorInputScreen.getAxisAttributeId(), atrValuesAcumulated) == null) {
          valorInputScreen.setCatalogElementId(elementoCatBBDD.getId());
          valorInputScreen.setCreationDate(timeStamp);
          if (valorInputScreen.getDomainValues() != null) {
            for (ValorDominioDeAttrElemCat valDomainInput: valorInputScreen.getDomainValues()) {
              valDomainInput.setCreationDate(timeStamp);
            }
          }
          atrValuesAcumulated.add(valorInputScreen);
        }
      }

      elementoCatIn.getAttributeValuesCollection().clear();
      elementoCatIn.setAttributeValuesCollection(atrValuesAcumulated);
      elementoCatIn.setUpdateDate(timeStamp);

      elementoCatBBDD = this.elemenCatalogoRepository.save(elementoCatIn);
      if (elementoCatIn.getCatalogElementCollateralId() != null) {
        ElementoCatalogo parent = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(
                elementoCatIn.getCatalogElementCollateralId());
        parent.setSubElements(null);
        parent.setAttributeValuesCollection(null);
        parent.setDeliveryCollection(null);
        elementoCatBBDD.setParentElement(parent);
      }
    }
    if (elementoCatBBDD.getSubElements() == null) {
      elementoCatBBDD.setSubElements(new ArrayList<>());
    }
    if (elementoCatBBDD.getDeliveryCollection() == null) {
      elementoCatBBDD.setDeliveryCollection(new ArrayList<>());
    }
    return elementoCatBBDD;
  }


  @Transactional
  public final ElementoCatalogo borradoLogico(final Integer idElementCat) throws ValidationRulesException {
    ElementoCatalogo removedObject = null;
    ElementoCatalogo elementoCat = this.elemenCatalogoRepository.findByIdAndDeletedIsNull(idElementCat);
    if (elementoCat != null) {
      if (elementoCat.getSubElements() != null && !elementoCat.getSubElements().isEmpty()) {
        throw new ValidationRulesException("No se puede eliminar <" + elementoCat.getCappCode()
          + "> porque existen elementos descendientes bajo éste. Elimine previamente sus elementos descendientes");
      }
      Timestamp tm = new Timestamp(Calendar.getInstance().getTime().getTime());
      for (ValoresEjesDeElemenCatalogoUsuario valorUserOrDomainId : elementoCat.getAttributeValuesCollection()) {
        valorUserOrDomainId.setDomainValues(null);
        valorUserOrDomainId.setUserValue(null);
        valorUserOrDomainId.setUpdateDate(tm);
        valorUserOrDomainId.setDeleted(1);
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valorUserOrDomainId);
      }

      elementoCat.setDeleted(Constantes.NUMBER_1);
      elementoCat.setUpdateDate(tm);
      ElementoCatalogo elementoCatDeleted;
      int veces = Constantes.NUMBER_0;
      do {
        String cappcodeDeleted = "[".concat(String.valueOf(++veces).concat("]:")).concat(elementoCat.getCappCode());
        elementoCatDeleted = this.elemenCatalogoRepository.findByCappCodeEquals(cappcodeDeleted);
      } while (elementoCatDeleted != null);
      String preffix = "[" + veces + "]:";
      String newcappcode = preffix.concat(elementoCat.getCappCode());
      elementoCat.setCappCode(newcappcode);
      String newname = preffix.concat(elementoCat.getName().concat("-pId:"
              + elementoCat.getCatalogElementCollateralId()));
      if (newname.length() > Constantes.POSITION_FIFTY) {
        newname = newname.substring(Constantes.NUMBER_0, Constantes.POSITION_FORTY_NINE);
      }
      elementoCat.setName(newname);
      elementoCat.setCatalogElementCollateralId(null);
      removedObject = this.elemenCatalogoRepository.save(elementoCat);
    }
    return removedObject;
  }

  @Transactional
  private ValoresEjesDeElemenCatalogoUsuario buscarValorByAttributeId(final Integer idAttr,
                                                                      final List<ValoresEjesDeElemenCatalogoUsuario>
                                                                              listaValoresAttrs) {
    ValoresEjesDeElemenCatalogoUsuario valSearched = null;
    for (ValoresEjesDeElemenCatalogoUsuario val : listaValoresAttrs) {
      if (val.getAxisAttributeId() == idAttr) {
        valSearched = val;
      }
    }
    return valSearched;
  }

  private void evaluateBusinessRules(final ElementoCatalogo elementoCatIn, final boolean creation) throws
          ValidationRulesException {
    ElementoCatalogo found = this.getByCappCode(elementoCatIn.getCappCode());
    if (creation && found != null && found.getId() != null && found.getId() > 0) {
      throw new ValidationRulesException(
              "El código CAPP <" + elementoCatIn.getCappCode() + "> ya existe en el Catálogo");
    }
    if (elementoCatIn.getCatalogElementTypeId().intValue() == TipoElementoCatalogo.ELEMENTO_PROMOCIONABLE
            && elementoCatIn.getCatalogElementCollateralId() == null) {
      throw new ValidationRulesException(
              "Debe seleccionar una aplicación para el elemento promocionable <" + elementoCatIn.getCappCode() + ">");
    }
    if (elementoCatIn.getAttributeValuesCollection() == null
            || elementoCatIn.getAttributeValuesCollection().isEmpty())  {
      throw new ValidationRulesException("No llegan atributos de este elemento de catálogo");
    }
    for (ValoresEjesDeElemenCatalogoUsuario valorUserOrDomainId : elementoCatIn.
            getAttributeValuesCollection()) {
      AtributoEje attrObjectSlave = this.atributoEjeRepository.findByIdAndDeletedIsNull(
              valorUserOrDomainId.getAxisAttributeId());
      if (attrObjectSlave != null && attrObjectSlave.getAxisAttributeCollateralId() != null) {
        ValorDominio valorDominioSlave = this.valorDominioRepository.findByIdAndDeletedIsNull(
                valorUserOrDomainId.getDomainValues() != null && !valorUserOrDomainId.getDomainValues().isEmpty()
                        ? valorUserOrDomainId.getDomainValues().iterator().next().getDomainValueId() : null);
        String valorSelectedSlave = "";
        if (valorUserOrDomainId.getUserValue() != null && (valorUserOrDomainId.getDomainValues() == null
                || valorUserOrDomainId.getDomainValues().isEmpty())) {
          valorSelectedSlave = valorUserOrDomainId.getUserValue();
        } else {
          valorSelectedSlave = valorDominioSlave.getName();
        }
        String nombreAtributoSlave = this.atributoEjeRepository.findByIdAndDeletedIsNull(
                valorUserOrDomainId.getAxisAttributeId()).getName();
        String nombreAtributoMaster = this.atributoEjeRepository.findByIdAndDeletedIsNull(
                attrObjectSlave.getAxisAttributeCollateralId()).getName();
        ValoresEjesDeElemenCatalogoUsuario valorMasterObject = buscarValorByAttributeId(
                attrObjectSlave.getAxisAttributeCollateralId(),
                elementoCatIn.getAttributeValuesCollection());
        if (valorMasterObject != null) {
          ValorDominio valorDominioMasterSelected = this.valorDominioRepository.
                  findByIdAndDeletedIsNull(valorMasterObject.getDomainValues() != null
                          && !valorMasterObject.getDomainValues().isEmpty()
                          ? valorMasterObject.getDomainValues().iterator().next().getDomainValueId() : null);
          String valorSelectedMaster = "";
          if (valorMasterObject.getUserValue() != null
                  && (valorMasterObject.getDomainValues() == null || valorMasterObject.getDomainValues().isEmpty())) {
            valorSelectedMaster = valorMasterObject.getUserValue();
          } else {
            valorSelectedMaster = this.valorDominioRepository.findByIdAndDeletedIsNull(
                    valorMasterObject.getDomainValues().iterator().next().getDomainValueId()).getName();
          }
          List<ValorDominioCondicionadoPor> valoresCondicionadosDeSlave = this.
                  valorDominioCondicionadoRepository.findAllByDeletedIsNullAndDomainValueId(
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

  /*@Transactional
  public final ElementoCatalogo removePhysical(final Integer idElementoCat) {
    ElementoCatalogo elementoCat = this.elemenCatalogoRepository.findById(idElementoCat).get();
    if (elementoCat != null) {

      ElementoCatalogo filter = new ElementoCatalogo();
      filter.setCatalogElementCollateralId(idElementoCat);
      Collection<ElementoCatalogo> subElements = this.elemenCatalogoRepository.findAll(Example.of(filter));
      this.elemenCatalogoRepository.deleteAll(subElements);

      EntregaElementoCatalogo entregaFilter = new EntregaElementoCatalogo();
      entregaFilter.setCatalogElementId(idElementoCat);
      Collection<EntregaElementoCatalogo> entregasHijas =
              this.entregaElementoCatalogoRepository.findAll(Example.of(entregaFilter));
      this.entregaElementoCatalogoRepository.deleteAll(entregasHijas);

      ValoresEjesDeElemenCatalogoUsuario filterEjes = new ValoresEjesDeElemenCatalogoUsuario();
      filterEjes.setCatalogElementId(idElementoCat);
      Collection<ValoresEjesDeElemenCatalogoUsuario> ejesDeIdCatlogo = this.
              valoresEjesDeElemenCatalogoUsuarioRepository.findAll(Example.of(filterEjes));
      this.valoresEjesDeElemenCatalogoUsuarioRepository.deleteAll(ejesDeIdCatlogo);

      elementoCat.setSubElements(null);
      elementoCat.setDeliveryCollection(null);
      elementoCat.setAttributeValuesCollection(null);
      this.elemenCatalogoRepository.delete(elementoCat);
    }
    return elementoCat;
  }*/

}
