package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.Grupo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

  List<Grupo> findAllByDeletedIsNull(Sort sort);

  Grupo findByIdAndDeletedIsNull(Integer id);


}

