package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.ValorDominio;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValorDominioRepository extends JpaRepository<ValorDominio, Integer> {

  List<ValorDominio> findByName(String name);
  List<ValorDominio> findAllByDeletedIsNull();
  List<ValorDominio> findAllByDeletedIsNull(Sort sort);
  ValorDominio findByIdAndDeletedIsNull(Integer id);
  List<ValorDominio> findAllByDeletedIsNullAndAxisAttributeId(Integer axisAttributeId, Sort sort);

}

