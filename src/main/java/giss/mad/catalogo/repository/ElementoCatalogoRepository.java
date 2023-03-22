package giss.mad.catalogo.repository;


import giss.mad.catalogo.model.ElementoCatalogo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ElementoCatalogoRepository extends JpaRepository<ElementoCatalogo, Integer> {

  ElementoCatalogo findByIdAndDeletedIsNull(Integer id);
  ElementoCatalogo findByCappCodeAndDeletedIsNull(String cappCode);

  ElementoCatalogo findByCappCodeEquals(String cappCode);

  ElementoCatalogo findByNameAndDeletedIsNull(String name);

  List<ElementoCatalogo> findAllByDeletedIsNull(Sort sort);

  List<ElementoCatalogo> findAllByGroupIdAndDeletedIsNull(Integer groupId, Sort sort);

  List<ElementoCatalogo> findAllByDeletedIsNullAndCatalogElementTypeId(Integer catalogElementTypeId,
      Sort sort);

  List<ElementoCatalogo> findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndGroupIdAndCatalogElementTypeId(
      Integer groupId, Integer catalogElementTypeId, Sort sort);

  List<ElementoCatalogo> findAllByDeletedIsNullAndGroupIdAndCatalogElementTypeId(Integer groupId,
      Integer catalogElementTypeId, Sort sort);

  List<ElementoCatalogo> findAllByDeletedIsNullAndCatalogElementCollateralId(
      Integer catalogElementCollateralId, Sort sort);

  List<ElementoCatalogo> findAllByDeletedIsNullAndCatalogElementCollateralIdIsNullAndCatalogElementTypeId(
      Integer catalogElementTypeId, Sort sort);


}

