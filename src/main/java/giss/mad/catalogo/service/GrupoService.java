package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.Grupo;
import giss.mad.catalogo.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

@Service
public class GrupoService {
  @Autowired
  private GrupoRepository grupoRepository;

  public final void setGrupoRepository(final GrupoRepository grupoRepository) {
    this.grupoRepository = grupoRepository;
  }

  public final Collection<Grupo> getAll() {
    return this.grupoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
  }

  public final Grupo get(final Integer idGrupo) {
    return this.grupoRepository.findByIdAndDeletedIsNull(idGrupo);
  }

  @Transactional
  public final Grupo alta(final Grupo grupo) {
    grupo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
    return this.grupoRepository.save(grupo);
  }

  @Transactional
  public final Grupo update(final Grupo grupo) {
    Grupo updatedObject = null;
    Grupo grupoBBDD = this.grupoRepository.findByIdAndDeletedIsNull(grupo.getId());
    if (grupoBBDD != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      grupo.setUpdateDate(timeStamp);
      grupo.setCreationDate(grupoBBDD.getCreationDate());
      grupo.setCatalogueElements(grupoBBDD.getCatalogueElements());
      grupo.setDeliveries(grupoBBDD.getDeliveries());
      updatedObject = this.grupoRepository.save(grupo);
    }
    return updatedObject;
  }

  @Transactional
  public final Grupo remove(final Integer idGrupo) throws ValidationRulesException {
    Grupo removedObject = null;
    Grupo grupoBBDD = this.grupoRepository.findByIdAndDeletedIsNull(idGrupo);
    if (grupoBBDD != null) {
      if (!grupoBBDD.getCatalogueElements().isEmpty()) {
        throw new ValidationRulesException("Debe eliminar antes los elementos de catálogo asociados a este grupo");
      }
      if (!grupoBBDD.getDeliveries().isEmpty()) {
        throw new ValidationRulesException(
            "Debe eliminar antes las entregas de los elementos de catálogo asociados a este grupo");
      }
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      grupoBBDD.setUpdateDate(timeStamp);
      grupoBBDD.setName(grupoBBDD.getName() + "[deleted at " + timeStamp.getTime() + "]");
      grupoBBDD.setDeleted(1);
      removedObject = this.grupoRepository.save(grupoBBDD);
    }
    return removedObject;
  }

  /*@Transactional
  public final Grupo removePhysical(final Integer idGrupo) {
    Grupo grupoBBDD = this.grupoRepository.findById(idGrupo).get();
    if (grupoBBDD != null) {
      this.grupoRepository.delete(grupoBBDD);
    }
    return grupoBBDD;
  }*/

}
