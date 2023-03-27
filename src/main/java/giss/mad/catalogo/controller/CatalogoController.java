package giss.mad.catalogo.controller;

import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.ElementoCatalogo;
import giss.mad.catalogo.model.EntregaElementoCatalogo;
import giss.mad.catalogo.model.Grupo;
import giss.mad.catalogo.model.Permiso;
import giss.mad.catalogo.model.Rol;
import giss.mad.catalogo.model.TipoElementoCatalogo;
import giss.mad.catalogo.model.Usuario;
import giss.mad.catalogo.model.UsuarioGrupo;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.model.auxejes.CatalogueNode;
import giss.mad.catalogo.model.auxejes.EjeReduced;
import giss.mad.catalogo.repository.GrupoRepository;
import giss.mad.catalogo.repository.RolRepository;
import giss.mad.catalogo.repository.UsuarioRepository;
import giss.mad.catalogo.repository.UsuarioGrupoRepository;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.ValoresEjesDeEntregaUsuarioRepository;
import giss.mad.catalogo.repository.ValoresEjesDeElemenCatalogoUsuarioRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.PermisoRolRepository;
import giss.mad.catalogo.repository.PermisoRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.ElementoCatalogoRepository;
import giss.mad.catalogo.repository.EntregaElementoCatalogoRepository;
import giss.mad.catalogo.service.BaseService;
import giss.mad.catalogo.service.AtributoEjeService;
import giss.mad.catalogo.service.AtributoEjePorTipoElementoService;
import giss.mad.catalogo.service.ElementoCatalogoService;
import giss.mad.catalogo.service.EntregaElementoCatalogoService;
import giss.mad.catalogo.service.GrupoService;
import giss.mad.catalogo.service.PermisoService;
import giss.mad.catalogo.service.RolService;
import giss.mad.catalogo.service.TipoElementoCatalogoService;
import giss.mad.catalogo.service.UsuarioService;
import giss.mad.catalogo.service.ValorDominioCondicionadoService;
import giss.mad.catalogo.service.ValorDominioService;
import giss.mad.catalogo.model.filters.CatalogueElementFilter;
import giss.mad.catalogo.model.filters.DeliveryExtended;
import giss.mad.catalogo.model.filters.DeliveryFilter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
    RequestMethod.DELETE})
@RequestMapping("/catalogo")
public final class CatalogoController {

  @Autowired
  private EntregaElementoCatalogoRepository entregaElementoCatalogoRepository;
  @Autowired
  private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;

  @Autowired
  private AtributoEjeRepository atributoEjeRepository;
  @Autowired
  private GrupoRepository grupoRepository;

  @Autowired
  private ElementoCatalogoRepository elementoCatalogoRepository;
  @Autowired
  private PermisoRepository permisoRepository;

  @Autowired
  private PermisoRolRepository permisoRolRepository;

  @Autowired
  private RolRepository rolRepository;
  @Autowired
  private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

  @Autowired
  private UsuarioGrupoRepository usuarioGrupoRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

  @Autowired
  private ValorDominioRepository valorDominioRepository;

  @Autowired
  private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;
  @Autowired
  private ValoresEjesDeEntregaUsuarioRepository valoresEjesDeEntregaUsuarioRepository;

  @Autowired
  private BaseService baseService;
  @Autowired
  private AtributoEjeService atributoEjeService;
  @Autowired
  private TipoElementoCatalogoService tipoElementoCatalogoService;
  @Autowired
  private AtributoEjePorTipoElementoService atributoEjePorTipoElementoService;

  @Autowired
  private ValorDominioService valorDominioService;

  @Autowired
  private ValorDominioCondicionadoService valorDominioCondicionadoService;

  @Autowired
  private ElementoCatalogoService elementoCatalogoService;

  @Autowired
  private EntregaElementoCatalogoService entregaElementoCatalogoService;

  @Autowired
  private PermisoService permisoService;

  @Autowired
  private GrupoService grupoService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private RolService rolService;

  @PostConstruct
  public void initServiceImpl() {

    this.baseService.setPermisoRolRepository(this.permisoRolRepository);
    this.baseService.setRolRepository(this.rolRepository);
    this.baseService.setPermisoRepository(this.permisoRepository);

    this.atributoEjeService.setAtributoEjeRepository(this.atributoEjeRepository);
    this.atributoEjeService.setTipoElementoCatalogoRepository(this.tipoElementoCatalogoRepository);
    this.atributoEjeService.setAtributoEjePorTipoElementoRepository(this.atributoEjePorTipoElementoRepository);
    this.atributoEjeService.setValorDominioRepository(this.valorDominioRepository);

    this.tipoElementoCatalogoService.setTipoElementoCatalogoRepository(this.tipoElementoCatalogoRepository);
    this.tipoElementoCatalogoService.setAtributoEjePorTipoElementoRepository(this.atributoEjePorTipoElementoRepository);

    this.atributoEjePorTipoElementoService.setAtributoEjeRepository(this.atributoEjeRepository);
    this.atributoEjePorTipoElementoService.setValorDominioRepository(this.valorDominioRepository);
    this.atributoEjePorTipoElementoService.setAtributoEjePorTipoElementoRepository(
            this.atributoEjePorTipoElementoRepository);
    this.atributoEjePorTipoElementoService.setElementoCatalogoRepository(this.elementoCatalogoRepository);
    this.atributoEjePorTipoElementoService.setValoresEjesDeElemenCatalogoUsuarioRepository(
            this.valoresEjesDeElemenCatalogoUsuarioRepository);

    this.valorDominioService.setValorDominioRepository(this.valorDominioRepository);
    this.valorDominioService.setAtributoEjeRepository(this.atributoEjeRepository);
    this.valorDominioService.setValorDominioCondicionadoRepository(this.valorDominioCondicionadoRepository);

    this.valorDominioCondicionadoService.setValorDominioRepository(this.valorDominioRepository);
    this.valorDominioCondicionadoService.setValorDominioCondicionadoRepository(
            this.valorDominioCondicionadoRepository);

    this.elementoCatalogoService.setEntregaElementoCatalogoRepository(this.entregaElementoCatalogoRepository);
    this.elementoCatalogoService.setElemenCatalogoRepository(this.elementoCatalogoRepository);
    this.elementoCatalogoService.setAtributoEjeRepository(this.atributoEjeRepository);
    this.elementoCatalogoService.setValoresEjesDeElemenCatalogoUsuarioRepository(
            this.valoresEjesDeElemenCatalogoUsuarioRepository
    );
    this.elementoCatalogoService.setTipoElementoCatalogoRepository(this.tipoElementoCatalogoRepository);
    this.elementoCatalogoService.setGrupoRepository(this.grupoRepository);
    this.elementoCatalogoService.setRolRepository(this.rolRepository);
    this.elementoCatalogoService.setUsuarioRepository(this.usuarioRepository);
    this.elementoCatalogoService.setValorDominioCondicionadoRepository(this.valorDominioCondicionadoRepository);
    this.elementoCatalogoService.setAtributoEjePorTipoElementoRepository(this.atributoEjePorTipoElementoRepository);
    this.elementoCatalogoService.setValorDominioRepository(this.valorDominioRepository);

    this.entregaElementoCatalogoService.setElementoCatalogoPadreEntregaRepository(this.elementoCatalogoRepository);
    this.entregaElementoCatalogoService.setEntregaElementoCatalogoRepository(this.entregaElementoCatalogoRepository);
    this.entregaElementoCatalogoService.setValoresEjesDeEntregaUsuarioRepository(
            this.valoresEjesDeEntregaUsuarioRepository);
    this.entregaElementoCatalogoService.setValoresEjesDeElemenCatalogoUsuarioRepository(
            this.valoresEjesDeElemenCatalogoUsuarioRepository);
    this.entregaElementoCatalogoService.setAtributoEjeRepository(this.atributoEjeRepository);
    this.entregaElementoCatalogoService.setTipoElementoCatalogoRepository(this.tipoElementoCatalogoRepository);
    this.entregaElementoCatalogoService.setRolRepository(this.rolRepository);
    this.entregaElementoCatalogoService.setValorDominioRepository(this.valorDominioRepository);
    this.entregaElementoCatalogoService.setUsuarioRepository(this.usuarioRepository);
    this.entregaElementoCatalogoService.setValorDominioCondicionadoRepository(this.valorDominioCondicionadoRepository);

    this.permisoService.setPermisoRepository(this.permisoRepository);

    this.grupoService.setGrupoRepository(this.grupoRepository);

    this.usuarioService.setUsuarioRepository(this.usuarioRepository);
    this.usuarioService.setGrupoRepository(this.grupoRepository);
    this.usuarioService.setRolRepository(this.rolRepository);
    this.usuarioService.setUsuarioGrupoRepository(this.usuarioGrupoRepository);
    this.usuarioService.setPermisoRepository(this.permisoRepository);

    this.rolService.setRolRepository(this.rolRepository);
    this.rolService.setPermisoRolRepository(this.permisoRolRepository);
    this.rolService.setUsuarioRepository(this.usuarioRepository);
  }

  @GetMapping("/usuario/getAll")
  public Collection<Usuario> getAllUsers() {
    return this.usuarioService.getAll();
  }

  @PostMapping("/usuario/login")
  public Usuario loginService(final @RequestBody @NotEmpty @NotNull Usuario usuarioObject) {
    String name = usuarioObject.getName();
    String password = usuarioObject.getPassword();
    Usuario user = this.usuarioService.loginService(name, password);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    //asignamos el token...como?
    return user;
  }

  @PostMapping("/usuario/create")
  public Usuario createUser(final @RequestBody @NotEmpty @NotNull Usuario userReceived) {
    //authenticationService.getToken();
    List<UsuarioGrupo> gruposDeUsuario = new ArrayList<>();
    if (userReceived.getGroups() != null && !userReceived.getGroups().isEmpty()) {
      gruposDeUsuario.addAll(userReceived.getGroups());
    }
    Usuario usuario = this.usuarioService.insertar(userReceived);
    if (usuario == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    List<UsuarioGrupo> gruposAdded = this.usuarioService.insertGroupsAssociated(usuario.getId(),
        gruposDeUsuario);
    usuario.setGroups(gruposAdded);
    return usuario;
  }

  @PutMapping("/usuario/update")
  public Usuario updateUser(final @RequestBody @NotEmpty @NotNull Usuario usuarioReceived) {
    Usuario user = this.usuarioService.update(usuarioReceived);
    if (user == null) {
       throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return user;
  }

  @DeleteMapping("/usuario/delete/{id}")
  public void deleteUser(final @PathVariable @NotEmpty @NotNull Integer id) {
    Usuario user = this.usuarioService.remove(id);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }
  @GetMapping("/roles/getAll")
  public Collection<Rol> getAllRoles() {
    //authenticationService.getToken();
    return this.rolService.getAll();
  }
  @GetMapping("/roles/getAllPrivileges")
  public Collection<Permiso> getAllPrivileges() {
    return this.permisoService.getAll();
  }

  @PostMapping("/roles/create")
  public Rol createRole(final @RequestBody @NotNull @NotEmpty Rol roleReceived) {
    Rol role = this.rolService.insertar(roleReceived);
    if (role == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return role;
  }

  @PutMapping("/roles/update")
  public Rol updateRole(final @RequestBody @NotNull @NotEmpty Rol roleReceived) {
    Rol role = this.rolService.update(roleReceived);
    if (role == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    role.setPermisos(roleReceived.getPermisos());
    return role;
  }

  @DeleteMapping("/roles/delete/{id}")
  public void deleteRole(final @PathVariable Integer id) {
    try {
      Rol role = this.rolService.remove(id);
      if (role == null) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
      }
    } catch (ValidationRulesException valExc) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }
  @PostMapping("/roles/createPrivilege")
  public Permiso createPrivilege(final @RequestBody @NotNull @NotEmpty Permiso permisoObject) {
    Permiso permiso = this.permisoService.insertar(permisoObject);
    if (permiso == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return permiso;
  }

  @PutMapping("/roles/updatePrivilege")
  public Permiso updatePrivilege(final @RequestBody @NotNull @NotEmpty Permiso permisoObject) {
    Permiso permiso = this.permisoService.update(permisoObject);
    if (permiso == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return permiso;
  }

  @DeleteMapping("/roles/deletePrivilege/{id}")
  public void deletePrivilege(final @PathVariable @NotNull @NotEmpty Integer id) {
    Permiso permiso = null;
    try {
      permiso = this.permisoService.remove(id);
    } catch (ValidationRulesException exc) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    if (permiso == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }

  }

  @GetMapping("/permisos/getAll")
  public Collection<Permiso> getAllPermisos() {
    return this.permisoService.getAll();
  }

  @PostMapping("/permisos/create")
  public Permiso createPermiso(final @RequestBody @NotNull @NotEmpty Permiso permisoObject) {
    Permiso permisoCreated = this.permisoService.insertar(permisoObject);
    if (permisoCreated == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return permisoCreated;
  }

  @PutMapping("/permisos/update")
  public Permiso updatePermiso(final @RequestBody @NotNull @NotEmpty Permiso permisoObject) {
    Permiso permisoUpdated = this.permisoService.update(permisoObject);
    if (permisoUpdated == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return permisoUpdated;
  }

  @DeleteMapping("/permisos/delete/{id}")
  public void deletePermiso(final @PathVariable @NotNull @NotEmpty Integer id) {
    try {
      Permiso permisoRemoved = this.permisoService.remove(id);
      if (permisoRemoved == null) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
      }
    } catch (ValidationRulesException exc) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("/grupos/getAll")
  public Collection<Grupo> getAllGrupos() {
    return this.grupoService.getAll();
  }


  @GetMapping("/grupos/getById/{id}")
  public Grupo getGrupoById(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.grupoService.get(id);
  }

  @PostMapping("/grupos/create")
  public Grupo createGrupo(final @RequestBody @NotNull @NotEmpty Grupo grupoObject) {
    Grupo grupoCreated = this.grupoService.alta(grupoObject);
    if (grupoCreated == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return grupoCreated;
  }

  @PutMapping("/grupos/update")
  public Grupo updateGrupo(final @RequestBody @NotNull @NotEmpty Grupo grupoObject) {
    Grupo grupoUpdated = this.grupoService.update(grupoObject);
    if (grupoUpdated == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return grupoUpdated;
  }

  @DeleteMapping("/grupos/delete/{id}")
  public void deleteGrupo(final @PathVariable @NotNull @NotEmpty Integer id) {
    try {
      Grupo grupoRemoved = this.grupoService.remove(id);
      if (grupoRemoved == null) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
      }
    } catch (ValidationRulesException exc) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Failed to Delete this group. Reason: " + exc.getMessage());
    }
  }

  @GetMapping("/tipoelemento/getAll")
  public Collection<TipoElementoCatalogo> getTiposElementos() {
    return this.tipoElementoCatalogoService.getAll();
  }

  @GetMapping("/tipoelemento/getNamesOrderedById")
  public Collection<String> getNamesOrderedById() {
    return this.tipoElementoCatalogoService.getLabelsOrdered();
  }

  @GetMapping("/tipoelemento/getById/{id}")
  public TipoElementoCatalogo getTipoElemento(final @PathVariable @NotNull @NotEmpty Integer id) {
    TipoElementoCatalogo tipoElementoCatalogo = this.tipoElementoCatalogoService.get(id);
    if (tipoElementoCatalogo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return tipoElementoCatalogo;
  }

  @PostMapping("/tipoelemento/create")
  public TipoElementoCatalogo createTipoElemento(
          final @RequestBody @NotNull @NotEmpty TipoElementoCatalogo tipoElementoCatalogo) {
    return this.tipoElementoCatalogoService.insertar(tipoElementoCatalogo);
  }

  @PutMapping("/tipoelemento/update")
  public TipoElementoCatalogo updateTipoElemento(
          final @RequestBody @NotNull @NotEmpty TipoElementoCatalogo tipoElementoCatalogo) {
    return this.tipoElementoCatalogoService.actualizar(tipoElementoCatalogo);
  }

  @DeleteMapping("/tipoelemento/delete/{id}")
  public void deleteTipoElemento(final @PathVariable @NotNull @NotEmpty Integer id) {
    TipoElementoCatalogo c = this.tipoElementoCatalogoService.borradoLogico(id);
    if (c == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("/atributoeje/getAll")
  public Collection<AtributoEje> getAtributosYEjes() {
    return this.atributoEjeService.getAll();
  }

  @GetMapping("/atributoeje/getByTypeOfIdElementOfcatalogue/{id}")
  public Collection<AtributoEje> getByTypeOfIdElementOfcatalogue(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.atributoEjePorTipoElementoService.getAllByTipoCatalogueAndNotDelivery(id);
  }

  @GetMapping("/atributoeje/getByIdElementAndDelivery/{idElement}")
  public Collection<AtributoEje> getByIdElementAndDelivery(final @PathVariable @NotNull @NotEmpty Integer idElement) {
    return this.atributoEjePorTipoElementoService.getAllByIdCatalogueForDelivery(idElement);
  }

  @GetMapping("/ejes/getAll")
  public Collection<AtributoEje> getEjes() {
    return this.atributoEjeService.getEjes();
  }

  @GetMapping("/ejes/getNamesOrderedById")
  public Collection<String> getAxisNamesOrderedById() {
    return this.atributoEjeService.getAxisNamesOrderedById();
  }

  @GetMapping("/ejes/getreduced")
  public Collection<EjeReduced> getSoloEjes() {
    return this.atributoEjeService.getEjesReduced();
  }

  @GetMapping("/atributos/getAll")
  public Collection<AtributoEje> getAtributos() {
    return this.atributoEjeService.getSoloAtributos();
  }

  @GetMapping("/atributoeje/getById/{id}")
  public AtributoEje getAtributo(final @PathVariable @NotNull @NotEmpty Integer id) {
    AtributoEje atributo = this.atributoEjeService.get(id);
    if (atributo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return atributo;
  }

  @PostMapping("/atributoeje/create")
  public AtributoEje createAtributo(final @RequestBody @NotNull @NotEmpty AtributoEje atributoEje) {
    return this.atributoEjeService.insertar(atributoEje);
  }

  @PutMapping("/atributoeje/update")
  public AtributoEje updateAtributo(final @RequestBody @NotNull @NotEmpty AtributoEje atributoEje) {
    AtributoEje atributoEjeSaved = this.atributoEjeService.actualizar(atributoEje);
    if (atributoEjeSaved == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return atributoEjeSaved;
  }

  @DeleteMapping("/atributoeje/delete/{id}")
  public void deleteAtributo(final @PathVariable @NotNull @NotEmpty Integer id) {
    AtributoEje c = this.atributoEjeService.borradoLogico(id);
    if (c == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("/valordominio/getAll")
  public Collection<ValorDominio> getValoresDominio() {
    return this.valorDominioService.getAll();
  }

  @GetMapping("/valordominio/getNamesOfValoresDominio")
  public Collection<String> getNamesOfValoresDominio() {
    return this.valorDominioService.getlabelsOrderById();
  }

  @GetMapping("/valordominio/getById/{id}")
  public ValorDominio getValorDominioById(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.valorDominioService.get(id);
  }

  @GetMapping("/valordominio/getByName/{name}")
  public List<ValorDominio> getValorDominioByName(final @PathVariable @NotNull @NotEmpty String name) {
    return this.valorDominioService.getByName(name);
  }

  @GetMapping("/valordominio/getByAttributeId/{id}")
  public List<ValorDominio> getByAttributeId(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.valorDominioService.getByAttributeId(id);
  }

  @GetMapping("/valordominio/getAllOfCollateralAttrId/{id}")
  public List<ValorDominio> getAllOfCollateralAttrId(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.valorDominioService.getAllOfCollateralAttrId(id);
  }

  @PostMapping("/valordominio/create")
  public ValorDominio createValorDominio(final @RequestBody @NotNull @NotEmpty ValorDominio valorDominio) {
    return this.valorDominioService.insertar(valorDominio);
  }

  @PutMapping("/valordominio/update")
  public ValorDominio updateValorDominio(final @RequestBody @NotNull @NotEmpty ValorDominio valorDominio) {
    ValorDominio valorDominioSaved = this.valorDominioService.actualizar(valorDominio);
    if (valorDominioSaved == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return valorDominioSaved;
  }

  @DeleteMapping("/valordominio/delete/{id}")
  public void deleteValorDominio(final @PathVariable @NotNull @NotEmpty Integer id) {
    ValorDominio valorDominioRemoved = this.valorDominioService.borradoLogico(id);
    if (valorDominioRemoved == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("/elementocatalogo/getAll/{userId}")
  public Collection<ElementoCatalogo> getElementosDeCatalogo(final @PathVariable @NotNull @NotEmpty Integer userId) {
    return this.elementoCatalogoService.getAllByGroup(userId);
  }

  @GetMapping("/elementocatalogo/getAll")
  public Collection<ElementoCatalogo> getElementosDeCatalogo() {
    return this.elementoCatalogoService.getAllByGroup(null);
  }

  @GetMapping("/elementocatalogo/getById/{id}")
  public ElementoCatalogo getElementById(final @PathVariable @NotNull @NotEmpty Integer id) {
    ElementoCatalogo ele = this.elementoCatalogoService.getById(id);
    if (ele == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
    return ele;
  }

  @PostMapping("/elementocatalogo/create")
  public ElementoCatalogo createElement(final @RequestBody @NotNull @NotEmpty ElementoCatalogo elementoCatalogo) {
    if (this.elementoCatalogoService.getByCappCode(elementoCatalogo.getCappCode()) != null) {
      String message = "Elemento de catálogo ya existe con ese código CAPP " + elementoCatalogo.getCappCode();
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }

    List<ElementoCatalogo> actualChildrenIds = new ArrayList<>();
    if (elementoCatalogo.getSubElements() != null && !elementoCatalogo.getSubElements()
        .isEmpty()) {
      actualChildrenIds.addAll(elementoCatalogo.getSubElements());
      elementoCatalogo.getSubElements().clear();
      elementoCatalogo.setSubElements(new ArrayList<>());
    }

    ElementoCatalogo createdElemen = null;
    try {
      createdElemen = this.elementoCatalogoService.insertar(elementoCatalogo);
    } catch (ValidationRulesException exc) {
      String message = exc.getMessage();
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }

    for (ElementoCatalogo child : actualChildrenIds) {
      ElementoCatalogo hijo = this.elementoCatalogoService.getById(child.getId());
      hijo.setCatalogElementCollateralId(elementoCatalogo.getId());
      this.elementoCatalogoService.onlyUpdate(hijo);
      createdElemen.getSubElements().add(hijo);
    }
    return createdElemen;

  }

  @PutMapping("/elementocatalogo/update")
  public ElementoCatalogo updateElement(final @RequestBody @NotNull @NotEmpty ElementoCatalogo elementoCatalogo) {
    try {
      ElementoCatalogo updatedElemen = this.elementoCatalogoService.actualizar(elementoCatalogo);
      if (updatedElemen == null) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
      }
      return updatedElemen;
    } catch (ValidationRulesException exc) {
      String message = exc.getMessage();
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @DeleteMapping("/elementocatalogo/delete/{id}")
  public void deleteElementOfcatalogue(final @PathVariable @NotNull @NotEmpty Integer id) {
    try {
      ElementoCatalogo c = this.elementoCatalogoService.borradoLogico(id);
      if (c == null) {
        throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
      }
    } catch (ValidationRulesException e) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("/elementocatalogo/getByCappCode/{cappCode}")
  public ElementoCatalogo getElementCatalogueByCappCode(final @PathVariable @NotNull @NotEmpty String cappCode) {
    ElementoCatalogo ele = this.elementoCatalogoService.getByCappCode(cappCode);
    if (ele == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ele;
  }

  @GetMapping("/elementocatalogo/getByName/{name}")
  public ElementoCatalogo getElementCatalogueByName(final @PathVariable @NotNull @NotEmpty String name) {
    ElementoCatalogo ele = this.elementoCatalogoService.getByName(name);
    if (ele == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ele;
  }

  @GetMapping("/elementocatalogo/getSubElementsByIdElement/{id}")
  public Collection<ElementoCatalogo> getSubElementsOfCatalogueElement(final @PathVariable @NotNull @NotEmpty
                                                                         Integer id) {
    return this.elementoCatalogoService.getByCollateralId(id);
  }

  @GetMapping("/elementocatalogo/getElementosPromo/{userId}")
  public Collection<ElementoCatalogo> getElementsPromo(final @PathVariable @NotNull @NotEmpty Integer userId) {
    return this.elementoCatalogoService.
        getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(userId, TipoElementoCatalogo.ELEMENTO_PROMOCIONABLE);
  }

  @GetMapping("/elementocatalogo/getElementosPromo")
  public Collection<ElementoCatalogo> getElementsPromo() {
    return this.elementoCatalogoService.getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(null,
                TipoElementoCatalogo.ELEMENTO_PROMOCIONABLE);
  }


  @GetMapping("/elementocatalogo/getFreeElementsPromo/{userId}")
  public Collection<ElementoCatalogo> getFreeElementsPromo(final @PathVariable @NotNull @NotEmpty Integer userId) {
    return this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(userId,
            TipoElementoCatalogo.ELEMENTO_PROMOCIONABLE);
  }

  @GetMapping("/elementocatalogo/getFreeElementsPromo")
  public Collection<ElementoCatalogo> getFreeElementsPromo() {
    return this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(null,
            TipoElementoCatalogo.ELEMENTO_PROMOCIONABLE);
  }

  @GetMapping("/elementocatalogo/getAplicaciones/{userId}")
  public Collection<ElementoCatalogo> getAplicaciones(final @PathVariable @NotNull @NotEmpty
                                                      Integer userId) {
    return this.elementoCatalogoService.
            getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(userId, TipoElementoCatalogo.APLICACION);
  }

  @GetMapping("/elementocatalogo/getAplicaciones")
  public Collection<ElementoCatalogo> getAplicaciones() {
    return this.elementoCatalogoService.
            getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(null, TipoElementoCatalogo.APLICACION);
  }

  @GetMapping("/elementocatalogo/getFreeAplicaciones/{userId}")
  public Collection<ElementoCatalogo> getFreeAplicaciones(final @PathVariable @NotNull @NotEmpty
                                                                     Integer userId) {
    return this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(userId,
            TipoElementoCatalogo.APLICACION);
  }

  @GetMapping("/elementocatalogo/getFreeAplicaciones")
  public Collection<ElementoCatalogo> getFreeAplicaciones() {
    return this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(null,
            TipoElementoCatalogo.APLICACION);
  }

  @GetMapping("/elementocatalogo/getAgrupacionesFunc/{userId}")
  public Collection<ElementoCatalogo> getElementsAgrupacionesFunc(final @PathVariable @NotNull @NotEmpty
                                                                    Integer userId) {
    return this.elementoCatalogoService.
        getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(userId, TipoElementoCatalogo.AGRUPACION_FUNCIONAL);
  }

  @GetMapping("/elementocatalogo/getAgrupacionesFunc")
  public Collection<ElementoCatalogo> getElementsAgrupacionesFunc() {
    return this.elementoCatalogoService.
        getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(null, TipoElementoCatalogo.AGRUPACION_FUNCIONAL);
  }

  @GetMapping("/elementocatalogo/getFreeAplicOAgrupFunc")
  public Collection<ElementoCatalogo> getFreeAplicOAgrupFunc() {
    Collection<ElementoCatalogo> c1 = this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(
            null,  TipoElementoCatalogo.APLICACION);
    Collection<ElementoCatalogo> c2 = this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(
            null,  TipoElementoCatalogo.AGRUPACION_FUNCIONAL);
    Collection<ElementoCatalogo> c = new ArrayList<>();
    c.addAll(c1);
    c.addAll(c2);
    return c;
  }

  @GetMapping("/elementocatalogo/getFreeAplicOAgrupFunc/{userId}")
  public Collection<ElementoCatalogo> getFreeAplicOAgrupFunc(final @PathVariable @NotNull @NotEmpty
                                                               Integer userId) {
    Collection<ElementoCatalogo> c1 = this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(
            userId,  TipoElementoCatalogo.APLICACION);
    Collection<ElementoCatalogo> c2 = this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(
            userId,  TipoElementoCatalogo.AGRUPACION_FUNCIONAL);
    Collection<ElementoCatalogo> c = new ArrayList<>();
    c.addAll(c1);
    c.addAll(c2);
    return c;
  }

  @GetMapping("/elementocatalogo/getProyectos/{groupId}")
  public Collection<ElementoCatalogo> getElementsPoyectos(final @PathVariable @NotNull @NotEmpty Integer groupId) {
    return this.elementoCatalogoService.getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(groupId,
            TipoElementoCatalogo.PROYECTO);
  }

  @GetMapping("/elementocatalogo/getProyectos")
  public Collection<ElementoCatalogo> getElementsPoyectos() {
    return this.elementoCatalogoService.
            getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(null, TipoElementoCatalogo.PROYECTO);
  }

  @PostMapping("/elementocatalogo/search")
  public Collection<CatalogueElementFilter> searchElementsSummarized(
     final @RequestBody @NotNull @NotEmpty CatalogueElementFilter elementFilter) {
    return elementoCatalogoService.searchByFilterForList(elementFilter);
  }

  @PostMapping("/elementocatalogo/searchComplete")
  public Collection<ElementoCatalogo> searchElements(
     final @RequestBody @NotNull @NotEmpty CatalogueElementFilter elementFilter) {
    return elementoCatalogoService.searchElementsByFilter(elementFilter);
  }

  @PostMapping("/entrega/create")
  public EntregaElementoCatalogo createDelivery(
      final @RequestBody @NotNull @NotEmpty EntregaElementoCatalogo entregaElementoCatalogo) {
    if (this.entregaElementoCatalogoService.getByName(entregaElementoCatalogo.getName()) != null) {
      String message = "Entrega de elemento de catálogo ya existe con el nombre <"
          + entregaElementoCatalogo.getName() + ">";
      throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
    EntregaElementoCatalogo entregaElementoCatalogo1;
    try {
      entregaElementoCatalogo1 = this.entregaElementoCatalogoService.insertar(entregaElementoCatalogo);
    } catch (ValidationRulesException exc) {
      String message = exc.getMessage();
      throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
    return entregaElementoCatalogo1;
  }

  @PutMapping("/entrega/update")
  public EntregaElementoCatalogo updateDelivery(
          final @RequestBody @NotNull @NotEmpty EntregaElementoCatalogo entregaElementoCatalogo) {
    EntregaElementoCatalogo entregaElementoCatalogo1 = null;
    try {
      entregaElementoCatalogo1 = this.entregaElementoCatalogoService.actualizar(
            entregaElementoCatalogo);
    } catch (ValidationRulesException exc) {
      String message = exc.getMessage();
      throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
    if (entregaElementoCatalogo1 == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              "Failed to actualizar delivery of element of catalogue specified");
    }
    return entregaElementoCatalogo1;
  }

  @DeleteMapping("/entrega/delete/{id}")
  public void deleteDelivery(final @PathVariable @NotNull @NotEmpty Integer id) {
    EntregaElementoCatalogo c = this.entregaElementoCatalogoService.remove(id);
    if (c == null) {
      throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
    }
  }

  @PostMapping("/entrega/search")
  public Collection<DeliveryFilter> searchDeliverysSummarized(
      final @RequestBody @NotEmpty DeliveryFilter deliveryFilter) {
    return entregaElementoCatalogoService.searchByFilterForList(deliveryFilter);
  }

  @PostMapping("/entrega/searchComplete")
  public Collection<EntregaElementoCatalogo> searchDeliverys(
      final @RequestBody @NotEmpty DeliveryFilter deliveryFilter) {
    return entregaElementoCatalogoService.searchElementsByFilter(deliveryFilter);
  }

  @GetMapping("/entrega/getAll/{groupId}")
  public Collection<DeliveryExtended> getAllDeliveries2(final @PathVariable @NotNull @NotEmpty Integer groupId) {
    return this.entregaElementoCatalogoService.getAllFiltered(null);
  }

  @GetMapping("/entrega/getAll")
  public Collection<DeliveryExtended> getAllDeliveries2() {
    return this.entregaElementoCatalogoService.getAllFiltered(null);
  }

  @GetMapping("/entrega/getAllFiltered/{groupId}")
  public Collection<DeliveryExtended> getAllDeliveries3(final @PathVariable @NotNull @NotEmpty Integer groupId) {
    DeliveryExtended deliveryExtended = new DeliveryExtended();
    deliveryExtended.setUserId(groupId);
    return this.entregaElementoCatalogoService.getAllFiltered(deliveryExtended);
  }

  @PostMapping("/entrega/getAllFiltered")
  public Collection<DeliveryExtended> getAllFilteredDeliveries(
      final @RequestBody @NotEmpty DeliveryExtended deliveryExtended) {
    return this.entregaElementoCatalogoService.getAllFiltered(deliveryExtended);
  }

  @GetMapping("/entrega/getById/{id}")
  public EntregaElementoCatalogo getDeliveryById(final @PathVariable @NotNull @NotEmpty Integer id) {
    return this.entregaElementoCatalogoService.getById(id);
  }

  @GetMapping("/entrega/getByCappCode/{cappCode}")
  public Collection<EntregaElementoCatalogo> getDeliverysByCappCode(final @PathVariable @NotNull @NotEmpty
                                                                      String cappCode) {
    ElementoCatalogo element = this.elementoCatalogoService.getByCappCode(cappCode);
    return this.entregaElementoCatalogoService.getByIdOfElement(element.getId());
  }

  @GetMapping("/entrega/getByName/{name}")
  public EntregaElementoCatalogo getEntregaElementCatalogueByName(final @PathVariable @NotNull @NotEmpty String name) {
    return this.entregaElementoCatalogoService.getByName(name);
  }

  @GetMapping("/entrega/getAllByIdOfElement/{idOfElement}")
  public Collection<EntregaElementoCatalogo> getByIdOfElement(final @PathVariable @NotNull @NotEmpty
                                                                Integer idOfElement) {
    return this.entregaElementoCatalogoService.getByIdOfElement(idOfElement);
  }

  @GetMapping("/elementocatalogo/getHierarchyById/{idOfElement}")
  public List<CatalogueNode> getHierarchyById(final @PathVariable @NotNull @NotEmpty
                                                              Integer idOfElement) {
    return this.elementoCatalogoService.getHierarchyById(idOfElement);

  }


}
