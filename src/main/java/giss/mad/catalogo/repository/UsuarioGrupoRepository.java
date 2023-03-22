package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.UsuarioGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Integer> {

  UsuarioGrupo findByGroupIdAndUserIdAndDeletedIsNull(Integer groupId, Integer userId);

  List<UsuarioGrupo> findAllByDeletedIsNull();

  List<UsuarioGrupo> findAllByUserIdAndDeletedIsNull(Integer userId);

  UsuarioGrupo findByIdAndDeletedIsNull(Integer id);


}

