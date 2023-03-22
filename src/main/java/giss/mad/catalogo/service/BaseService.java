package giss.mad.catalogo.service;

import giss.mad.catalogo.model.Permiso;
import giss.mad.catalogo.model.PermisoRol;
import giss.mad.catalogo.model.Rol;
import giss.mad.catalogo.repository.PermisoRepository;
import giss.mad.catalogo.repository.PermisoRolRepository;
import giss.mad.catalogo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de la que heredan los service de MACA Spring Boot
 */
@Service
@Component
public class BaseService {

    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private PermisoRolRepository permisoRolRepository;
    @Autowired
    private RolRepository rolRepository;

    public final void setPermisoRepository(final PermisoRepository permisoRolRepository) {
        this.permisoRepository = permisoRolRepository;
    }
    public final void setPermisoRolRepository(final PermisoRolRepository permisoRolRepository) {
        this.permisoRolRepository = permisoRolRepository;
    }

    public final void setRolRepository(final RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    private List<String> getDatabaseRoles() {
        List<String> roles = new ArrayList<>();
        if (null/*getServiceName()*/ != null) {
            // buscamos esta clase de servicio para ver qué roles tiene asignados
            Permiso servicePermiso = this.permisoRepository.findByNameAndDeletedIsNull(null/*getServiceName()*/);

            List<PermisoRol> permisosRolesAssigned =
                    this.permisoRolRepository.findAllByPrivilegeIdAndDeletedIsNull(servicePermiso.getId());
            for (PermisoRol permisoRol: permisosRolesAssigned) {
                Rol role = this.rolRepository.findByIdAndDeletedIsNull(permisoRol.getRoleId());
                roles.add(role.getName());
            }
        }
        return roles;
    }

}
