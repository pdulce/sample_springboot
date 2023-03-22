package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.PermisoRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRolRepository extends JpaRepository<PermisoRol, Integer> {


  PermisoRol findByRoleIdAndPrivilegeIdAndDeletedIsNull(Integer roleId, Integer privilegeId);

  List<PermisoRol> findAllByPrivilegeIdAndDeletedIsNull(Integer privilegeId);

  List<PermisoRol> findAllByDeletedIsNull();

  PermisoRol findByIdAndDeletedIsNull(Integer id);


}

