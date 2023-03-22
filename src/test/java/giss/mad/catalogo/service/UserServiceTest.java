package giss.mad.catalogo.service;

import giss.mad.catalogo.model.*;
import giss.mad.catalogo.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioGrupoRepository usuarioGrupoRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PermisoRepository permisoRepository;
    @Mock
    private GrupoRepository grupoRepository;

    @InjectMocks
    private UsuarioService usuarioService;
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
    public void testGetAllUsuarios() {
        when(usuarioRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")))).thenReturn(usuarios);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);
        when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);

        Collection<Usuario> result = usuarioService.getAll();
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
    }

    @Test
    public void testGetUsuarioById() {
        when(usuarioRepository.findByIdAndDeletedIsNull(199)).thenReturn(usuario);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);
        when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);

        Usuario result = usuarioService.get(199);
        assertEquals("John Doe", result.getName());
        verify(usuarioRepository, times(1)).findByIdAndDeletedIsNull(199);
    }

    @Test
    public void testLoginServiceWithEmail(){
        when(usuarioRepository.findByEmailAndPasswordAndDeletedIsNull(usuario.getEmail(), usuario.getPassword())).
                thenReturn(usuario);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);
        when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        Collection<Integer> permisosIds = new ArrayList<>();
        permisosIds.add(399);
        when(permisoRepository.findAllById(permisosIds)).thenReturn(permisos);

        Usuario result = usuarioService.loginService(usuario.getEmail(), usuario.getPassword());
        assertEquals("John Doe", result.getName());
        verify(usuarioRepository, times(1)).findByEmailAndPasswordAndDeletedIsNull(
                usuario.getEmail(), usuario.getPassword());
    }

    @Test
    public void testLoginServiceWithSilconCode(){
        when(usuarioRepository.findBySilconCodeAndPasswordAndDeletedIsNull(usuario.getSilconCode(),
                usuario.getPassword())).thenReturn(usuario);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);
        when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        Collection<Integer> permisosIds = new ArrayList<>();
        permisosIds.add(399);
        when(permisoRepository.findAllById(permisosIds)).thenReturn(permisos);

        Usuario result = usuarioService.loginService(usuario.getSilconCode(), usuario.getPassword());
        assertEquals("John Doe", result.getName());
        verify(usuarioRepository, times(1)).
                findBySilconCodeAndPasswordAndDeletedIsNull(usuario.getSilconCode(), usuario.getPassword());
    }

    @Test
    public void testLoginServiceWithName(){
        when(usuarioRepository.findByNameAndPasswordAndDeletedIsNull(usuario.getName(),
                usuario.getPassword())).thenReturn(usuario);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);
        when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        Collection<Integer> permisosIds = new ArrayList<>();
        permisosIds.add(399);
        when(permisoRepository.findAllById(permisosIds)).thenReturn(permisos);

        Usuario result = usuarioService.loginService(usuario.getName(), usuario.getPassword());
        assertEquals("John Doe", result.getName());
        verify(usuarioRepository, times(1)).findByNameAndPasswordAndDeletedIsNull(
                usuario.getName(), usuario.getPassword());
    }

    @Test
    public void testCreateUsuario() {
        usuario.setDeleted(null);
        usuario.setName("John Doe");
        usuario.setSilconCode("99GU0000");
        usuario.setPermisos(permisos);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);

        Usuario result = usuarioService.insertar(usuario);
        assertEquals("John Doe", result.getName());
        assertNotNull(result.getCreationDate());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testUpdateUsuario() {
        usuario.setDeleted(null);
        usuario.setName("John Doe");

        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
//        when(usuarioGrupoRepository.findByGroupIdAndUserIdAndDeletedIsNull(299,199)).
        //        thenReturn(usuarioGrupo);
        //when(grupoRepository.findByIdAndDeletedIsNull(299)).thenReturn(grupo);
        when(usuarioRepository.findByIdAndDeletedIsNull(199)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.update(usuario);
        assertEquals("John Doe", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testBajaLogicaUsuario() {
        when(usuarioRepository.findByIdAndDeletedIsNull(199)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioGrupoRepository.findAllByUserIdAndDeletedIsNull(199)).thenReturn(usuariosGrupos);

        Usuario result = usuarioService.remove(199);
        assertEquals("johndoe@email.com", result.getEmail());
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(usuarioRepository, times(1)).save(usuario);
    }

}
