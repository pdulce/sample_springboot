package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.Permiso;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

  List<Permiso> findAllByDeletedIsNull(Sort sort);

  Permiso findByIdAndDeletedIsNull(Integer id);

  Permiso findByNameAndDeletedIsNull(String name);


}

