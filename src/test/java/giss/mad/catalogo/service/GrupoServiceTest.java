package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.repository.GrupoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/***
 * En este test.robot se usaron las siguientes librerías y clases para utilizar las directivas 'when' y 'verify':
 *
 * org.junit.runner.RunWith: para ejecutar la clase de prueba con el runner de Mockito MockitoJUnitRunner.
 * org.mockito.Mock: para crear un mock de la clase GrupoRepository.
 * org.mockito.InjectMocks: para inyectar el mock creado en la clase de servicio GrupoService.
 * org.mockito.Mockito: para utilizar las directivas when y verify.
 * org.assertj.core.api.Assertions: para realizar los asserts
 */
@ExtendWith(MockitoExtension.class)
public class GrupoServiceTest {

    /*Esta declaración inyecta el repository 'mockeado' en el bean de la clase service*/
    @InjectMocks
    private GrupoService grupoService;
    @Mock
    private GrupoRepository grupoRepository;

    private static Usuario usuario;
    private static Grupo grupo;
    private static UsuarioGrupo usuarioGrupo;
    private static Rol rol;
    private static Permiso permiso;
    private static PermisoRol permisoRol;
    private static List<Usuario> usuarios;
    private static List<Grupo> grupos;
    private static List<UsuarioGrupo> usuariosGrupos;
    private static List<Rol> roles;
    private static List<Permiso> permisos;
    private static List<PermisoRol> permisoRoles;

    @BeforeAll
    public static void setUp() {
        usuarioGrupo = new UsuarioGrupo();
        usuarioGrupo.setId(1099);
        usuarioGrupo.setGroupName("grupo A");
        usuarioGrupo.setUserId(199);
        usuarioGrupo.setDeleted(null);
        usuarioGrupo.setGroupId(299);
        usuariosGrupos = new ArrayList<>();
        usuariosGrupos.add(usuarioGrupo);

        grupo = new Grupo();
        grupo.setId(299);
        grupo.setName("grupo A");
        grupo.setDeleted(null);
        grupo.setDeliveries(new ArrayList<>());
        grupo.setCatalogueElements(new ArrayList<>());
        grupos = new ArrayList<>();
        grupos.add(grupo);

        usuario = new Usuario();
        usuario.setId(199);
        usuario.setName("John Doe");
        usuario.setSilconCode("99GU0000");
        usuario.setPassword("nnnn");
        usuario.setEmail("johndoe@email.com");
        usuario.setRoleId(499);
        usuario.setGroups(usuariosGrupos);
        usuarios = new ArrayList<>();
        usuarios.add(usuario);

        permisoRol = new PermisoRol();
        permisoRol.setId(699);
        permisoRol.setPrivilegeId(399);
        permisoRol.setRoleId(499);
        permisoRoles = new ArrayList<>();
        permisoRoles.add(permisoRol);

        permiso = new Permiso();
        permiso.setId(399);
        permiso.setName("permiso 1");
        permiso.setDeleted(null);
        permiso.setDescription("permiso 1 es un permiso");
        permiso.setRoles(permisoRoles);
        permisos = new ArrayList<>();
        permisos.add(permiso);

        rol = new Rol();
        rol.setId(499);
        rol.setName("role B");
        rol.setDeleted(null);
        rol.setPermisos(permisoRoles);
        rol.setUsuarios(usuarios);
        rol.setDescription("role B s rol B");
        roles = new ArrayList<>();
        roles.add(rol);
    }
    @Test
    public void testGetAllGrupos() {
        // Given
        List<Grupo> grupos = Arrays.asList(new Grupo(), new Grupo());
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Mockito.when(grupoRepository.findAllByDeletedIsNull(sort)).thenReturn(grupos);
        // When
        Collection<Grupo> result = grupoService.getAll();
        // Then
        Assertions.assertThat(result).isEqualTo(grupos);
        Mockito.verify(grupoRepository).findAllByDeletedIsNull(sort);
    }

    @Test
    public void testGetGrupo() {
        // given
        Mockito.when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        // when
        Grupo result = grupoService.get(299);
        // then
        Assertions.assertThat(result).isEqualTo(grupo);
        Mockito.verify(grupoRepository).findByIdAndDeletedIsNull(299);
    }

    @Test
    public void testCreateGrupo() {
        // given
        Mockito.when(grupoRepository.save(grupo)).thenReturn(grupo);
        // when
        Grupo result = grupoService.alta(grupo);
        // then
        Assertions.assertThat(result).isEqualTo(grupo);
        assertNotNull(result.getCreationDate());
        Mockito.verify(grupoRepository).save(grupo);
    }
    @Test
    public void testUpdateGrupo() {
        grupo.setDeleted(null);
        grupo.setName("grupo A");
        // given
        Mockito.when(grupoRepository.save(grupo)).thenReturn(grupo);
        Mockito.when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        // when
        Grupo result = grupoService.update(grupo);
        // then
        Assertions.assertThat(result).isEqualTo(grupo);
        assertNotNull(result.getUpdateDate());
        Mockito.verify(grupoRepository).save(grupo);
    }

    @Test
    public void testRemoveGrupo() {
        // given
        Mockito.when(grupoRepository.save(grupo)).thenReturn(grupo);
        Mockito.when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        try {
            // when
            Grupo result = grupoService.remove(299);
            // then
            Assertions.assertThat(result).isEqualTo(grupo);
            assertNotNull(result.getDeleted());
            Mockito.verify(grupoRepository).save(grupo);
        } catch (ValidationRulesException exc){
            Assertions.assertThat(1).isEqualTo(0);
        }
    }

}
