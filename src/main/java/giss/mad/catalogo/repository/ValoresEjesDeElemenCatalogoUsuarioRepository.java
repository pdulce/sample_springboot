package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.ValoresEjesDeElemenCatalogoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ValoresEjesDeElemenCatalogoUsuarioRepository extends
    JpaRepository<ValoresEjesDeElemenCatalogoUsuario, Integer> {
  List<ValoresEjesDeElemenCatalogoUsuario> findAllByAxisAttributeIdAndDeletedIsNull(Integer axisAttributeId);
  List<ValoresEjesDeElemenCatalogoUsuario> findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(
      Integer axisAttributeId, String userValue);
  ValoresEjesDeElemenCatalogoUsuario findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(
      Integer axisAttributeId, Integer catalogElementId);
  List<ValoresEjesDeElemenCatalogoUsuario> findAllByDeletedIsNull();
  ValoresEjesDeElemenCatalogoUsuario findByIdAndDeletedIsNull(Integer id);
}

