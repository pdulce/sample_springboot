package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.EntregaElementoCatalogo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EntregaElementoCatalogoRepository extends
    JpaRepository<EntregaElementoCatalogo, Integer> {

  EntregaElementoCatalogo findByNameEquals(String name);

  EntregaElementoCatalogo findByDeletedIsNullAndNameContaining(String name);

  List<EntregaElementoCatalogo> findByDeletedIsNullAndCreationDateBetween(Timestamp startDate,
      Timestamp endDate, Sort sort);

  List<EntregaElementoCatalogo> findAllByDeletedIsNull(Sort sort);

  List<EntregaElementoCatalogo> findAllByGroupIdAndDeletedIsNull(Integer groupId, Sort sort);

  List<EntregaElementoCatalogo> findAllByCatalogElementIdAndDeletedIsNull(Integer catalogElementId,
      Sort sort);

  EntregaElementoCatalogo findByIdAndDeletedIsNull(Integer id);


}

