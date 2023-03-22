package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.AtributoEje;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoEjeRepository extends JpaRepository<AtributoEje, Integer> {

  AtributoEje findByCodeAndDeletedIsNull(String code);

  List<AtributoEje> findByAxisAndDeletedIsNull(Integer axis, Sort sort);

  List<AtributoEje> findAllByDeletedIsNull(Sort sort);

  AtributoEje findByIdAndDeletedIsNull(Integer id);

}

