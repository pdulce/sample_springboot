package giss.mad.catalogo.service;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.repository.PermisoRepository;
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
public class PermisoServiceTest {
    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoService permisoService;
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
    public void testGetAllPermisos() {

        when(permisoRepository.findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")))).thenReturn(permisos);

        Collection<Permiso> result = permisoService.getAll();
        assertEquals(1, result.size());
        verify(permisoRepository, times(1)).
                findAllByDeletedIsNull(Sort.by(Sort.Order.desc("id")));
    }

    @Test
    public void testGetPermisoById() {
        when(permisoRepository.findByIdAndDeletedIsNull(399)).thenReturn(permiso);

        Permiso result = permisoService.get(399);
        assertEquals("permiso 1", result.getName());
        verify(permisoRepository, times(1)).findByIdAndDeletedIsNull(399);
    }

    @Test
    public void testCreatePermiso() {
        permiso.setDeleted(null);
        permiso.setName("permiso 1");
        permiso.setRoles(permisoRoles);
        when(permisoRepository.save(permiso)).thenReturn(permiso);

        Permiso result = permisoService.insertar(permiso);
        assertEquals("permiso 1", result.getName());
        assertNotNull(result.getCreationDate());
        verify(permisoRepository, times(1)).save(permiso);
    }

    @Test
    public void testUpdatePermiso() {
        permiso.setDeleted(null);
        permiso.setName("permiso 1");

        when(permisoRepository.findByIdAndDeletedIsNull(399)).thenReturn(permiso);
        when(permisoRepository.save(permiso)).thenReturn(permiso);

        Permiso result = permisoService.update(permiso);
        assertEquals("permiso 1", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(permisoRepository, times(1)).save(permiso);
    }

    @Test
    public void testBajaLogicaPermiso()  {
        permiso.setRoles(new ArrayList<>());
        when(permisoRepository.findByIdAndDeletedIsNull(399)).thenReturn(permiso);
        when(permisoRepository.save(permiso)).thenReturn(permiso);
        try {
            Permiso result = permisoService.remove(399);
            assertEquals("permiso 1 es un permiso", result.getDescription());
            assertEquals("1", String.valueOf(result.getDeleted()));
            verify(permisoRepository, times(1)).save(permiso);
        } catch (ValidationRulesException validationExc){
            assertEquals("1", "0", "Error : " + validationExc.getMessage());
        }
    }



}
