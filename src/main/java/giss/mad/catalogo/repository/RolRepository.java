package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.Rol;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

  List<Rol> findAllByDeletedIsNull(Sort sort);

  Rol findByIdAndDeletedIsNull(Integer id);


}

