package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ValorDominioCondicionadoRepository extends
    JpaRepository<ValorDominioCondicionadoPor, Integer> {

  List<ValorDominioCondicionadoPor> findAllByDeletedIsNull();
  ValorDominioCondicionadoPor findByIdAndDeletedIsNull(Integer id);
  ValorDominioCondicionadoPor findByDeletedIsNullAndDomainValueCollateralIdAndDomainValueId(
      Integer domainValueCollateralId, Integer domainValueId);
  List<ValorDominioCondicionadoPor> findAllByDeletedIsNullAndDomainValueId(Integer domainValueId);


}

