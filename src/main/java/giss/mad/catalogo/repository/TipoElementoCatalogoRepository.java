package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.TipoElementoCatalogo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoElementoCatalogoRepository extends
    JpaRepository<TipoElementoCatalogo, Integer> {

  List<TipoElementoCatalogo> findAllByDeletedIsNull();

  List<TipoElementoCatalogo> findAllByDeletedIsNull(Sort sort);
  TipoElementoCatalogo findByIdAndDeletedIsNull(Integer id);


}
