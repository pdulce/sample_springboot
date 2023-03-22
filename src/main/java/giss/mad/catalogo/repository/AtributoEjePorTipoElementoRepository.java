package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.AtributoEjePorTipoElemento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoEjePorTipoElementoRepository extends
    JpaRepository<AtributoEjePorTipoElemento, Integer> {


  List<AtributoEjePorTipoElemento> findAllByDeletedIsNull();

  AtributoEjePorTipoElemento findByIdAndDeletedIsNull(Integer id);

  List<AtributoEjePorTipoElemento> findAllByDeletedIsNullAndAxisAttributeId(Integer axisAttributeId);
  List<AtributoEjePorTipoElemento> findAllByDeletedIsNullAndCatalogElementTypeIdAndForCatalogue(
      Integer catalogElementType,
      Integer forCatalogue,
      Sort sort);

  List<AtributoEjePorTipoElemento> findAllByDeletedIsNullAndCatalogElementTypeIdAndForDelivery(
      Integer catalogElementType,
      Integer forDelivery,
      Sort sort);

  List<AtributoEjePorTipoElemento> findAllByDeletedIsNullAndCatalogElementTypeIdAndAxisAttributeId(
          Integer catalogElementType, Integer axisAttributeId);

}

