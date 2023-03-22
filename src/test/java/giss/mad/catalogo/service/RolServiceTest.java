package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.repository.PermisoRolRepository;
import giss.mad.catalogo.repository.RolRepository;
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
public class RolServiceTest {
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PermisoRolRepository permisoRolRepository;

    @InjectMocks
    private RolService rolService;
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
    public void testGetAllRoles() {

        when(rolRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")))).thenReturn(roles);

        Collection<Rol> result = rolService.getAll();
        assertEquals(1, result.size());
        verify(rolRepository, times(1)).
                findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
    }

    @Test
    public void testGetRolById() {
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);

        Rol result = rolService.get(499);
        assertEquals("role B", result.getName());
        verify(rolRepository, times(1)).findByIdAndDeletedIsNull(499);
    }

    @Test
    public void testCreateRol() {
        rol.setName("role B");
        rol.setDeleted(null);
        rol.setPermisos(permisoRoles);
        rol.setUsuarios(usuarios);
        when(rolRepository.save(rol)).thenReturn(rol);
        Rol result = rolService.insertar(rol);
        assertEquals("role B", result.getName());
        assertNotNull(result.getCreationDate());
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    public void testUpdateRol() {

        rol.setDeleted(null);
        rol.setName("role B");

        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(permisoRolRepository.findByRoleIdAndPrivilegeIdAndDeletedIsNull(499,399)).
                thenReturn(permisoRol);
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = rolService.update(rol);
        assertEquals("role B", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    public void testSaveChildrenOfRol() {
        permisoRol = new PermisoRol();
        permisoRol.setId(699);
        permisoRol.setPrivilegeId(399);
        permisoRol.setRoleId(499);
        permisoRoles = new ArrayList<>();
        permisoRoles.add(permisoRol);
        when(permisoRolRepository.findByRoleIdAndPrivilegeIdAndDeletedIsNull(499,399)).
                thenReturn(permisoRol);

        List<PermisoRol> result = rolService.saveChildren(499, permisoRoles);
        assertEquals(1, result.size());
        //verify(permisoRolRepository, times(1)).insertar(permisoRol);
    }

    @Test
    public void testBajaLogicaRol() {
        when(rolRepository.findByIdAndDeletedIsNull(499)).thenReturn(rol);
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = null;
        try {
            result = rolService.remove(499);
            assertEquals("role B s rol B", result.getDescription());
            assertEquals("1", String.valueOf(result.getDeleted()));
            verify(rolRepository, times(1)).save(rol);
        } catch (ValidationRulesException e) {
            throw new RuntimeException(e);
        }
    }

}
