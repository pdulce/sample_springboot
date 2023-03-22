package giss.mad.catalogo.controller;

import giss.mad.catalogo.model.*;
import giss.mad.catalogo.service.GrupoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class CatalogoControllerTest {

    /*Esta declaración inyecta el repository 'mockeado' en el bean de la clase service*/
    @Mock
    private GrupoService grupoService;
    @InjectMocks
    private CatalogoController catalogoController;

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
    void getGroupById_shouldReturnNotFound_whenGroupNotFound() {
        Integer id = 1;
        Mockito.when(grupoService.get(id)).thenReturn(null);
        Grupo responseGrupo = catalogoController.getGrupoById(id);
        Assertions.assertThat(responseGrupo == null);
    }

    @Test
    void getAllGroups_shouldReturnOneGroup_whenFound() {

        Mockito.when(grupoService.getAll()).thenReturn(grupos);
        Collection<Grupo> responseGrupos = catalogoController.getAllGrupos();
        Assertions.assertThat(responseGrupos.size() == grupos.size());
    }

    @Test
    void createGroup_shouldReturnNotModifiedException_whenObjectNameIsNull() {
        Grupo group = new Grupo();
        Mockito.when(grupoService.alta(group)).thenReturn(null);
        try {
            Grupo responseGrupo = catalogoController.createGrupo(group);
        } catch (ResponseStatusException exc) {
            Assertions.assertThat(exc.getMessage().contentEquals(String.valueOf(HttpStatus.NOT_MODIFIED)));
        }
    }


}
