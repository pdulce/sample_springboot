package giss.mad.catalogo.service;

import giss.mad.catalogo.model.Usuario;
import giss.mad.catalogo.model.UsuarioGrupo;
import giss.mad.catalogo.model.Grupo;
import giss.mad.catalogo.model.Constantes;
import giss.mad.catalogo.model.PermisoRol;
import giss.mad.catalogo.repository.GrupoRepository;
import giss.mad.catalogo.repository.PermisoRepository;
import giss.mad.catalogo.repository.UsuarioRepository;
import giss.mad.catalogo.repository.UsuarioGrupoRepository;
import giss.mad.catalogo.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Service
public class UsuarioService {

  private static Logger logger = (Logger) LoggerFactory.getLogger(UsuarioService.class);
  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private UsuarioGrupoRepository usuarioGrupoRepository;

  @Autowired
  private RolRepository rolRepository;

  @Autowired
  private PermisoRepository permisoRepository;

  @Autowired
  private GrupoRepository grupoRepository;

  public final void setUsuarioRepository(final UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public final void setUsuarioGrupoRepository(final UsuarioGrupoRepository usuarioGrupoRepository) {
    this.usuarioGrupoRepository = usuarioGrupoRepository;
  }

  public final void setRolRepository(final RolRepository rolRepository) {
    this.rolRepository = rolRepository;
  }

  public final void setPermisoRepository(final PermisoRepository permisoRepository) {
    this.permisoRepository = permisoRepository;
  }

  public final void setGrupoRepository(final GrupoRepository grupoRepository) {
    this.grupoRepository = grupoRepository;
  }

  public final Collection<Usuario> getAll() {
    Collection<Usuario> collection = new ArrayList<>();
    Collection<Usuario> c = this.usuarioRepository.findAllByDeletedIsNull(
        Sort.by(Sort.Order.desc("id")));
    for (Usuario user : c) {
      user.setRoleName(this.rolRepository.findByIdAndDeletedIsNull(user.getRoleId()).getName());
      List<UsuarioGrupo> userGroups = this.usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(user.getId());
      for (UsuarioGrupo userGroup : userGroups) {
        userGroup.setGroupName(this.grupoRepository.findByIdAndDeletedIsNull(userGroup.getGroupId()).getName());
      }
      user.setGroups(userGroups);
      collection.add(user);
    }
    return collection;
  }

  public final Usuario get(final Integer idUsuario) {
    Usuario user = this.usuarioRepository.findByIdAndDeletedIsNull(idUsuario);
    user.setRoleName(this.rolRepository.findByIdAndDeletedIsNull(user.getRoleId()).getName());
    List<UsuarioGrupo> userGroups = this.usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(user.getId());
    for (UsuarioGrupo userGroup : userGroups) {
      userGroup.setGroupName(this.grupoRepository.findByIdAndDeletedIsNull(userGroup.getGroupId()).getName());
    }
    user.setGroups(userGroups);
    return user;
  }


  public final Usuario loginService(final String mailOrSilconOrName, final String password) {
    Usuario innerUser;
    if (mailOrSilconOrName.contains("@")) {
      innerUser = this.usuarioRepository.findByEmailAndPasswordAndDeletedIsNull(mailOrSilconOrName,
          password);
    } else if (Character.isDigit(mailOrSilconOrName.charAt(0))) {
      innerUser = this.usuarioRepository.findBySilconCodeAndPasswordAndDeletedIsNull(
          mailOrSilconOrName, password);
    } else {
      innerUser = this.usuarioRepository.findByNameAndPasswordAndDeletedIsNull(mailOrSilconOrName,
          password);
    }
    if (innerUser != null) {
      Collection<PermisoRol> privOfRole = this.rolRepository.findByIdAndDeletedIsNull(
              innerUser.getRoleId()).getPermisos();
      Collection<Integer> permisosIds = new ArrayList<>();
      for (PermisoRol rermisoRol : privOfRole) {
        permisosIds.add(rermisoRol.getPrivilegeId());
      }
      List<UsuarioGrupo> gruposDeUsuario = this.usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(
              innerUser.getId());
      for (UsuarioGrupo grupoDeUsuario : gruposDeUsuario) {
        Grupo grupo = this.grupoRepository.findByIdAndDeletedIsNull(grupoDeUsuario.getGroupId());
        grupoDeUsuario.setGroupName(grupo.getName());
      }
      innerUser.setPermisos(this.permisoRepository.findAllById(permisosIds));
      innerUser.setGroups(gruposDeUsuario);
      innerUser.setRoleName(
              this.rolRepository.findByIdAndDeletedIsNull(innerUser.getRoleId()).getName());
    }
    System.out.println(innerUser.getPermisos());
    return innerUser;
  }

  @Transactional
  public final Usuario insertar(final Usuario usuario) {
    Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    usuario.setCreationDate(timeStamp);
    if (usuario.getGroups() != null) {
      usuario.getGroups().clear();
    }
    Usuario savedUser = this.usuarioRepository.save(usuario);
    savedUser.setRoleName(this.rolRepository.findByIdAndDeletedIsNull(usuario.getRoleId()).getName());
    return savedUser;
  }

  @Transactional
  public final List<UsuarioGrupo> insertGroupsAssociated(final Integer idUser, final List<UsuarioGrupo> grupos) {
    List<UsuarioGrupo> newGrupos = new ArrayList<>();
    if (!grupos.isEmpty()) {
      for (UsuarioGrupo grupo : grupos) {
        grupo.setUserId(idUser);
        UsuarioGrupo grupoDeUsuarioBBDD = this.usuarioGrupoRepository.findByGroupIdAndUserIdAndDeletedIsNull(
                grupo.getGroupId(), grupo.getUserId());
        if (grupoDeUsuarioBBDD == null) {
          Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
          grupo.setCreationDate(timeStamp);
          grupo = this.usuarioGrupoRepository.saveAndFlush(grupo);
          grupo.setGroupName(
                  this.grupoRepository.findByIdAndDeletedIsNull(grupo.getGroupId()).getName());
          newGrupos.add(grupo);
        } else {
          grupoDeUsuarioBBDD.setGroupName(
                  this.grupoRepository.findByIdAndDeletedIsNull(grupoDeUsuarioBBDD.getGroupId())
                          .getName());
          newGrupos.add(grupoDeUsuarioBBDD);
        }
      }
    }
    return newGrupos;
  }

  @Transactional
  public final Usuario update(final Usuario usuarioArg) {
    Usuario updatedObject = null;
    Usuario usuarioBBDD = this.usuarioRepository.findByIdAndDeletedIsNull(usuarioArg.getId());
    if (usuarioBBDD != null) {
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      usuarioBBDD.setUpdateDate(timeStamp);
      usuarioBBDD.setName(usuarioArg.getName());
      usuarioBBDD.setPassword(usuarioArg.getPassword());
      usuarioBBDD.setSilconCode(usuarioArg.getSilconCode());
      usuarioBBDD.setEmail(usuarioArg.getEmail());
      List<UsuarioGrupo> newGroups = new ArrayList<>();
      for (UsuarioGrupo userGroup : usuarioArg.getGroups()) {
        userGroup.setUserId(usuarioArg.getId());
        UsuarioGrupo userGroupBBDD = this.usuarioGrupoRepository.findByGroupIdAndUserIdAndDeletedIsNull(
            userGroup.getGroupId(), userGroup.getUserId());
        if (userGroupBBDD == null) {
          userGroup.setCreationDate(timeStamp);
          userGroup.setGroupName(
              this.grupoRepository.findByIdAndDeletedIsNull(userGroup.getGroupId()).getName());
          newGroups.add(userGroup);
        } else {
          userGroupBBDD.setGroupName(
              this.grupoRepository.findByIdAndDeletedIsNull(userGroupBBDD.getGroupId()).getName());
          newGroups.add(userGroupBBDD);
        }
      }
      usuarioBBDD.setGroups(newGroups);
      usuarioBBDD.setRoleId(usuarioArg.getRoleId());
      updatedObject = this.usuarioRepository.save(usuarioBBDD);
      updatedObject.setRoleName(this.rolRepository.findByIdAndDeletedIsNull(usuarioArg.getRoleId()).getName());
    }
    return updatedObject;
  }

  @Transactional
  public final Usuario remove(final Integer idUsuario) {
    Usuario removedObject = null;
    Usuario usuarioBBDD = this.usuarioRepository.findByIdAndDeletedIsNull(idUsuario);
    if (usuarioBBDD != null) {
     this.usuarioGrupoRepository.deleteAll(this.usuarioGrupoRepository.
             findAllByUserIdAndDeletedIsNull(usuarioBBDD.getId()));
      usuarioBBDD.setGroups(new ArrayList<>());
      usuarioBBDD.setPermisos(new ArrayList<>());
      Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTime().getTime());
      usuarioBBDD.setUpdateDate(timeStamp);
      String newName = "del[".concat("]:").concat(usuarioBBDD.getName());
      String newSilconCode = "del[".concat("]:").concat(usuarioBBDD.getSilconCode());
      usuarioBBDD.setName(newName);
      usuarioBBDD.setSilconCode(newSilconCode);
      usuarioBBDD.setDeleted(Constantes.NUMBER_1);
      removedObject = this.usuarioRepository.save(usuarioBBDD);
    }
    return removedObject;
  }



  /*@Transactional
  public final Usuario removePhysical(final Integer idUsuario) {
    Usuario usuarioBBDD = this.usuarioRepository.getById(idUsuario);
    this.usuarioGrupoRepository.deleteAll(this.usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(idUsuario));
    this.usuarioRepository.delete(usuarioBBDD);
    return usuarioBBDD;
  }*/

}
