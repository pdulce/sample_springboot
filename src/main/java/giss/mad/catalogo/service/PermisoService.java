package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.Permiso;
import giss.mad.catalogo.repository.PermisoRepository;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PermisoService {

  @Autowired
  private PermisoRepository permisoRepository;

  public final void setPermisoRepository(final PermisoRepository permisoRepository) {
    this.permisoRepository = permisoRepository;
  }

  public final Collection<Permiso> getAll() {
    return this.permisoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
  }

  public final Permiso get(final Integer idPermiso) {
    return this.permisoRepository.findByIdAndDeletedIsNull(idPermiso);
  }

  @Transactional
  public final Permiso insertar(final Permiso permiso) {
    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    permiso.setCreationDate(timeStamp);
    return this.permisoRepository.save(permiso);
  }

  @Transactional
  public final Permiso update(final Permiso permiso) {
    Permiso updatedObject = null;
    Permiso permisoBBDD = this.permisoRepository.findByIdAndDeletedIsNull(permiso.getId());
    if (permisoBBDD != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      permiso.setUpdateDate(timeStamp);
      permiso.setRoles(permisoBBDD.getRoles());
      updatedObject = this.permisoRepository.save(permiso);
    }
    return updatedObject;
  }

  @Transactional
  public final Permiso remove(final Integer idPermiso) throws ValidationRulesException {
    Permiso removedObject = null;
    Permiso permisoBBDD = this.permisoRepository.findByIdAndDeletedIsNull(idPermiso);
    if (permisoBBDD != null) {
      if (!permisoBBDD.getRoles().isEmpty()) {
        throw new ValidationRulesException("Debe desasignar antes los permisos-rol definidos para este permiso");
      }
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      permisoBBDD.setUpdateDate(timeStamp);
      permisoBBDD.setName(permisoBBDD.getName() + "[deleted at " + timeStamp.getTime() + "]");
      permisoBBDD.setDeleted(Constantes.NUMBER_1);
      removedObject = this.permisoRepository.save(permisoBBDD);
    }
    return removedObject;
  }

  /*@Transactional
  public final Permiso removePhysical(final Integer idPermiso) {
    PermisoRol permisoRol = new PermisoRol();
    permisoRol.setPrivilegeId(idPermiso);
    Collection<PermisoRol> permisosRol = this.permisoRolRepository.findAll(Example.of(permisoRol));
    for (PermisoRol permisoRolSearched: permisosRol) {
      this.permisoRolRepository.deleteById(permisoRolSearched.getId());
    }
    Permiso permisoBBDD = this.permisoRepository.getById(idPermiso);
    this.permisoRepository.delete(permisoBBDD);
    return permisoBBDD;
  }*/

}
