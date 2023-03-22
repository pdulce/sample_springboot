package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.PermisoRol;
import giss.mad.catalogo.model.Rol;
import giss.mad.catalogo.repository.PermisoRolRepository;
import giss.mad.catalogo.repository.RolRepository;
import giss.mad.catalogo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


import javax.transaction.Transactional;

@Service
public class RolService {
  @Autowired
  private RolRepository rolRepository;
  @Autowired
  private PermisoRolRepository permisoRolRepository;
  @Autowired
  private UsuarioRepository usuarioRepository;

  public final void setRolRepository(final RolRepository rolRepository) {
    this.rolRepository = rolRepository;
  }

  public final void setUsuarioRepository(final UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public final void setPermisoRolRepository(final PermisoRolRepository permisoRolRepository) {
    this.permisoRolRepository = permisoRolRepository;
  }

  public final Collection<Rol> getAll() {
    return this.rolRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
  }

  public final Rol get(final Integer idRol) {
    return this.rolRepository.findByIdAndDeletedIsNull(idRol);
  }

  @Transactional
  public final Rol insertar(final Rol roleCloned) {
    Timestamp creationDate = new Timestamp(Calendar.getInstance().getTime().getTime());
    List<PermisoRol> permisos = roleCloned.getPermisos() == null ? new ArrayList<>() : roleCloned.getPermisos();
    for (PermisoRol permisoRol: permisos) {
      permisoRol.setCreationDate(creationDate);
    }
    roleCloned.setDescription(roleCloned.getDescription() == null ? roleCloned.getName() : roleCloned.getDescription());
    roleCloned.setCreationDate(creationDate);
    return this.rolRepository.save(roleCloned);
  }

  @Transactional
  public final Rol update(final Rol rol) {
    Rol updatedObject = null;
    Rol rolBBDD = this.rolRepository.findByIdAndDeletedIsNull(rol.getId());
    if (rolBBDD != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      rolBBDD.setUpdateDate(timeStamp);
      rolBBDD.setName(rol.getName());
      List<PermisoRol> newPermisos = new ArrayList<>();
      for (PermisoRol permisoRol : rol.getPermisos()) {
        permisoRol.setRoleId(rol.getId());
        PermisoRol permisoBBDD = this.permisoRolRepository.findByRoleIdAndPrivilegeIdAndDeletedIsNull(
            permisoRol.getRoleId(), permisoRol.getPrivilegeId());
        if (permisoBBDD == null) {
          permisoRol.setCreationDate(timeStamp);
          newPermisos.add(permisoRol);
        } else {
          newPermisos.add(permisoBBDD);
        }
      }
      rolBBDD.getPermisos().clear();
      rolBBDD.getPermisos().addAll(newPermisos);
      updatedObject = this.rolRepository.save(rolBBDD);
    }
    return updatedObject;
  }

  @Transactional
  public final List<PermisoRol> saveChildren(final Integer idRole, final List<PermisoRol> permisos) {
    List<PermisoRol> newPermisos = new ArrayList<>();
    if (!permisos.isEmpty()) {
      for (PermisoRol permisoRol : permisos) {
        permisoRol.setRoleId(idRole);
        PermisoRol permisoBBDD = this.permisoRolRepository.findByRoleIdAndPrivilegeIdAndDeletedIsNull(
                permisoRol.getRoleId(), permisoRol.getPrivilegeId());
        if (permisoBBDD == null) {
          permisoRol.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
          newPermisos.add(this.permisoRolRepository.save(permisoRol));
        } else {
          newPermisos.add(permisoBBDD);
        }
      }
    }
    return newPermisos;
  }

  @Transactional
  public final Rol remove(final Integer idRol) throws ValidationRulesException {
    Rol removedObject = null;
    /*Usuario user = new Usuario();
    user.setRoleId(idRol);
    Collection<Usuario> users = this.usuarioRepository.findAll(Example.of(user));
    if (users != null && !users.isEmpty()){
      throw new ValidationRulesException("No es posible borrar un rol sin haber desasig ado previamente " +
              "los usuarios asignados");
    } else {*/

      Rol rolBBDD = this.rolRepository.findByIdAndDeletedIsNull(idRol);
      if (rolBBDD != null) {
        if (!rolBBDD.getPermisos().isEmpty()) {
          rolBBDD.getPermisos().clear();
        }
        if (!rolBBDD.getUsuarios().isEmpty()) {
          rolBBDD.getUsuarios().clear();
        }
        Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        rolBBDD.setUpdateDate(timeStamp);
        rolBBDD.setName(rolBBDD.getName() + "[deleted at " + timeStamp.getTime() + "]");
        rolBBDD.setDeleted(1);
        removedObject = this.rolRepository.save(rolBBDD);

        PermisoRol permisoRol = new PermisoRol();
        permisoRol.setRoleId(idRol);
        Collection<PermisoRol> permisosRol = this.permisoRolRepository.findAll(Example.of(permisoRol));
        for (PermisoRol permisoRolSearched : permisosRol) {
          this.permisoRolRepository.deleteById(permisoRolSearched.getId());
        }
      }
    //}
    return removedObject;
  }

  /*@Transactional
  public final Rol removePhysical(final Integer idRol) {

    Rol rolBBDD = this.rolRepository.getById(idRol);
    Usuario user = new Usuario();
    user.setRoleId(idRol);
    Collection<Usuario> users = this.usuarioRepository.findAll(Example.of(user));
    for (Usuario userSearched: users) {
      this.usuarioRepository.deleteById(userSearched.getId());
    }

    PermisoRol permisoRol = new PermisoRol();
    permisoRol.setRoleId(idRol);
    Collection<PermisoRol> permisosRol = this.permisoRolRepository.findAll(Example.of(permisoRol));
    for (PermisoRol permisoRolSearched: permisosRol) {
      this.permisoRolRepository.deleteById(permisoRolSearched.getId());
    }
    this.rolRepository.delete(rolBBDD);
    return rolBBDD;
  }*/

}
