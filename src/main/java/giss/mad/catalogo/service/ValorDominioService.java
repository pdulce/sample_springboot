package giss.mad.catalogo.service;

import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import giss.mad.catalogo.model.ValorDominio;

import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import javax.transaction.Transactional;

@Service
public class ValorDominioService {

  @Autowired
  private ValorDominioRepository valorDominioRepository;

  @Autowired
  private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

  @Autowired
  private AtributoEjeRepository atributoEjeRepository;

  public final void setValorDominioCondicionadoRepository(final ValorDominioCondicionadoRepository
                                                                  valorDominioCondicionadoRepository) {
    this.valorDominioCondicionadoRepository = valorDominioCondicionadoRepository;
  }
  public final void setValorDominioRepository(final ValorDominioRepository valorDominioRepository) {
    this.valorDominioRepository = valorDominioRepository;
  }

  public final void setAtributoEjeRepository(final AtributoEjeRepository atributoEjeRepository) {
    this.atributoEjeRepository = atributoEjeRepository;
  }

  private void chargeAttrNameInfo(final ValorDominio v) {
    if (v.getAxisAttributeId() != null) {
      v.setAttributeName(this.atributoEjeRepository.findByIdAndDeletedIsNull(v.getAxisAttributeId()).getName());
      if (v.getMasterDomainValues() != null) {
        for (ValorDominioCondicionadoPor conditionedBy : v.getMasterDomainValues()) {
          conditionedBy.setName(this.valorDominioRepository.getById(conditionedBy.getDomainValueId()).getName());
          conditionedBy.setAttributeName(this.atributoEjeRepository.
                  findByIdAndDeletedIsNull(v.getAxisAttributeId()).getName());
        }
      }
    }
  }

  public final Collection<ValorDominio> getAll() {
    Collection<ValorDominio> c = this.valorDominioRepository.findAllByDeletedIsNull(Sort.by("id"));
    for (ValorDominio v: c) {
      chargeAttrNameInfo(v);
    }
    return c;
  }

  private ValorDominio searchInList(final Integer id, final List<ValorDominio> valoresDominio) {
    ValorDominio searched = null;
    boolean found = false;
    for (int i = 0; i < valoresDominio.size() && !found; i++) {
      ValorDominio t = valoresDominio.get(i);
      if (t.getId().intValue() == id.intValue()) {
        found = true;
        searched = t;
      }
    }
    return searched;
  }

  public final List<String> getlabelsOrderById() {
    List<String> labelsOrderedById = new ArrayList<>();
    List<ValorDominio> valoresDominio = this.valorDominioRepository.findAllByDeletedIsNull(
            Sort.by(Sort.Order.asc("id")));
    int min = 1;
    int last = (valoresDominio != null && !valoresDominio.isEmpty()) ? valoresDominio.get(valoresDominio.size() - 1).
            getId() : 0;
    for (int i = min; i <= last; i++) {
      ValorDominio searched = searchInList(i, valoresDominio);
      if (searched != null) {
        labelsOrderedById.add(searched.getName());
      } else {
        labelsOrderedById.add("");
      }
    }
    return labelsOrderedById;
  }



  public final ValorDominio get(final Integer valorDomino) {
    return this.valorDominioRepository.findByIdAndDeletedIsNull(valorDomino);
  }

  public final List<ValorDominio> getByAttributeId(final Integer attributeId) {
    return this.valorDominioRepository.findAllByDeletedIsNullAndAxisAttributeId(attributeId,
        Sort.by(Sort.Order.asc("id")));
  }

  public final List<ValorDominio> getAllOfCollateralAttrId(final Integer attributeId) {
    List<ValorDominio> values = new ArrayList<>();
    if (attributeId != -1) {
      AtributoEje atributo = this.atributoEjeRepository.findByIdAndDeletedIsNull(attributeId);
      if (atributo.getAxisAttributeCollateralId() != null) {
        values = getByAttributeId(atributo.getAxisAttributeCollateralId());
        for (ValorDominio v : values) {
          chargeAttrNameInfo(v);
        }
      }
    }
    return values;
  }

  public final List<ValorDominio> getByName(final String name) {
    return this.valorDominioRepository.findByName(name);
  }
  @Transactional
  public final ValorDominio insertar(final ValorDominio valorDominio) {
    valorDominio.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    ValorDominio valReceived = this.valorDominioRepository.save(valorDominio);
    chargeAttrNameInfo(valReceived);
    return valReceived;
  }

  private ValorDominioCondicionadoPor valueConditionedInList(final ValorDominioCondicionadoPor valueCondPor,
                                         final List<ValorDominioCondicionadoPor> listOfDomainVals) {
    ValorDominioCondicionadoPor foundValue = null;
    boolean foundDomainValue = false;
    int i = 0;
    while (i < listOfDomainVals.size() && !foundDomainValue) {
      ValorDominioCondicionadoPor val = listOfDomainVals.get(i);
      if (val.getDomainValueCollateralId().intValue() == valueCondPor.getDomainValueCollateralId().intValue()) {
        foundDomainValue = true;
        foundValue = val;
      }
      i++;
    }
    return foundValue;
  }

  @Transactional
  public final ValorDominio actualizar(final ValorDominio valorDominio) {
    List<ValorDominioCondicionadoPor> nuevosValuesMaster = new ArrayList();
    if (valorDominio.getMasterDomainValues() != null) {
      nuevosValuesMaster.addAll(valorDominio.getMasterDomainValues());
    }
    ValorDominio valorDominioBBDD = this.valorDominioRepository.findByIdAndDeletedIsNull(valorDominio.getId());
    List<ValorDominioCondicionadoPor> listaAntiguaValuesMaster = new ArrayList<>();
    if (valorDominioBBDD.getMasterDomainValues() != null) {
      listaAntiguaValuesMaster.addAll(valorDominioBBDD.getMasterDomainValues());
    }
    for (ValorDominioCondicionadoPor valorDominioCondicionadoPor : nuevosValuesMaster) {
      ValorDominioCondicionadoPor nuevoValueMaster  = valueConditionedInList(valorDominioCondicionadoPor,
              listaAntiguaValuesMaster);
      ValorDominio valorDominioCollateralBBDD = this.valorDominioRepository.findByIdAndDeletedIsNull(
              valorDominioCondicionadoPor.getDomainValueCollateralId());
      String attrName = this.atributoEjeRepository.findByIdAndDeletedIsNull(valorDominioCollateralBBDD.
              getAxisAttributeId()).getName();
      if (nuevoValueMaster == null) {
        // damos de insertar el elemento
        valorDominioCondicionadoPor.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominioCondicionadoPor.setName(this.valorDominioRepository.
                findByIdAndDeletedIsNull(valorDominioCondicionadoPor.getDomainValueCollateralId()).getName());
        valorDominioCondicionadoPor.setAttributeName(attrName); //nombre del atributo de este valor de dominio
        valorDominioCondicionadoPor.setDomainValueId(valorDominio.getId());
        valorDominioCondicionadoPor.setDomainValueCollateralId(valorDominioCondicionadoPor.
                getDomainValueCollateralId());
      } else {
        valorDominioCondicionadoPor.setId(nuevoValueMaster.getId());
        valorDominioCondicionadoPor.setCreationDate(nuevoValueMaster.getCreationDate());
        valorDominioCondicionadoPor.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominioCondicionadoPor.setName(this.valorDominioRepository.
                findByIdAndDeletedIsNull(valorDominioCondicionadoPor.getDomainValueCollateralId()).getName());
        valorDominioCondicionadoPor.setAttributeName(attrName); //nombre del atributo de este valor de dominio
        valorDominioCondicionadoPor.setDomainValueId(nuevoValueMaster.getDomainValueId());
        valorDominioCondicionadoPor.setDomainValueCollateralId(nuevoValueMaster.getDomainValueCollateralId());
      }
    }

    valorDominio.setCreationDate(valorDominioBBDD.getCreationDate());
    valorDominio.setUpdateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    valorDominio.setMasterDomainValues(nuevosValuesMaster);
    this.valorDominioRepository.save(valorDominio);

    //borramos la lista de valores condicionados que no exista en el objeto de entrada
    for (ValorDominioCondicionadoPor oldvalue : listaAntiguaValuesMaster) {
      if (valueConditionedInList(oldvalue, valorDominio.getMasterDomainValues()) == null) {
        this.valorDominioCondicionadoRepository.delete(this.valorDominioCondicionadoRepository.
                findByIdAndDeletedIsNull(oldvalue.getId()));
      }
    }
    chargeAttrNameInfo(valorDominio);
    return valorDominio;
  }

  @Transactional
  public final ValorDominio borradoLogico(final Integer valorDominioId) {
    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    ValorDominio valorDominio = this.valorDominioRepository.findByIdAndDeletedIsNull(valorDominioId);
    List<ValorDominioCondicionadoPor> listaAntiguaValuesMaster = new ArrayList<>();
    if (valorDominio != null) {
      if (valorDominio.getMasterDomainValues() != null) {
        listaAntiguaValuesMaster.addAll(valorDominio.getMasterDomainValues());
      }
      valorDominio.setUpdateDate(timeStamp);
      valorDominio.setName(valorDominio.getName() + "[deleted at " + timeStamp.getTime() + "]");
      valorDominio.setDeleted(1);
      for (ValorDominioCondicionadoPor valConditionedMaster: listaAntiguaValuesMaster) {
        valConditionedMaster.setUpdateDate(timeStamp);
        valConditionedMaster.setDeleted(1);
      }
      this.valorDominioRepository.save(valorDominio);
    }
    //borramos la lista de valores condicionados previos
   /*for (ValorDominioCondicionadoPor oldvalue : listaAntiguaValuesMaster){
      valorDominio.setUpdateDate(timeStamp);
      this.valorDominioCondicionadoRepository.save(this.valorDominioCondicionadoRepository.
              findByIdAndDeletedIsNull(oldvalue.getId()));
    }*/
    return valorDominio;
  }


   /*@Transactional
  public final ValorDominio removePhysical(final Integer idValorDominioIn) {
    ValorDominio valorDominio = this.valorDominioRepository.findById(idValorDominioIn).get();
    if (valorDominio != null) {
      this.valorDominioRepository.delete(valorDominio);
    }
    return valorDominio;
  }*/



}
