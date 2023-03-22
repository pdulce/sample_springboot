package giss.mad.catalogo.repository;

import giss.mad.catalogo.model.Usuario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Component
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  List<Usuario> findAllByDeletedIsNull(Sort sort);

  Usuario findByIdAndDeletedIsNull(Integer id);

  Usuario findByNameAndPasswordAndDeletedIsNull(String name, String password);

  Usuario findByEmailAndPasswordAndDeletedIsNull(String mail, String password);

  Usuario findBySilconCodeAndPasswordAndDeletedIsNull(String silconCode, String password);


}

