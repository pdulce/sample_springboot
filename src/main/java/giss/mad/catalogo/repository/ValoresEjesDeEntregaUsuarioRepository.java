package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.ValoresEjesDeEntregaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ValoresEjesDeEntregaUsuarioRepository extends
    JpaRepository<ValoresEjesDeEntregaUsuario, Integer> {

  List<ValoresEjesDeEntregaUsuario> findAllByAxisAttributeIdAndDeletedIsNull(Integer axisAttributeId);
  List<ValoresEjesDeEntregaUsuario> findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(
      Integer axisAttributeId, String userValue);
  ValoresEjesDeEntregaUsuario findByAxisAttributeIdAndDeliveryCatalogElementIdAndDeletedIsNull(
      Integer axisAttributeId, Integer deliveryCatalogElementId);
  List<ValoresEjesDeEntregaUsuario> findAllByDeletedIsNull();
  ValoresEjesDeEntregaUsuario findByIdAndDeletedIsNull(Integer id);
}

