package giss.mad.catalogo.service;

import giss.mad.catalogo.Application;
import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.model.auxejes.CatalogueNode;
import giss.mad.catalogo.model.filters.CatalogueElementFilter;
import giss.mad.catalogo.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class},
        properties = {"spring.datasource.url:jdbc:h2:mem:testdb;INIT=create schema if not exists MACA_CATALOGO"})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElementoCatalogoTest {
    //Objetos de prueba
    private static TipoElementoCatalogo tipoElementoPromocionable, tipoAgrupacionFuncional, tipoProyecto;
    private static Grupo grupo;
   // private static ArrayList<UsuarioGrupo> usuariosGrupo;
    private static Usuario usuario, usuario2;
    private static AtributoEje preffixAtributoEje, nameAtributoEje, codeAtributoEje, responsibleAtributoEje, serviceNameAtributoEje, functionalAreaNameAtributoEje, computerProcessingAtributoEje, groupAtributoEje, situationAtributoEje;
    private static ValorDominio domainValue;
    private static ArrayList<ValorDominioDeAttrElemCat> domainValues;
    private static ElementoCatalogo parentElement, elementoCatalogo;
    private static Rol rol, rol2;
    @Autowired
    private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;
    @Autowired
    private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;
    @Autowired
    private EntregaElementoCatalogoRepository entregaElementoCatalogoRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ElementoCatalogoRepository elemenCatalogoRepository;
    @Autowired
    private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepository;
    @Autowired
    private GrupoRepository grupoRepository;
    @Autowired
    private AtributoEjeRepository atributoEjeRepository;
    @Autowired
    private ValorDominioRepository valorDominioRepository;
    @Autowired
    private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;
    @Autowired
    private ElementoCatalogoService elementoCatalogoService;
    @Autowired
    private AtributoEjeService atributoEjeService;
    @Autowired
    private ValorDominioService valorDominioService;

    @BeforeAll
    public void setUp() {
        grupo = new Grupo();
        grupo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        grupo.setName("Group name");
        grupo.setCatalogueElements(new ArrayList<>());
        grupo.setDeliveries(new ArrayList<>());
        this.grupoRepository.save(grupo);

        rol = new Rol();
        rol.setName("roleName");
        rol.setDescription("roleDescription");
        rol.setDeleted(null);
        rol.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.rolRepository.save(rol);

        rol2 = new Rol();
        rol2.setName("roleName__");
        rol2.setDescription("roleDescription__");
        rol2.setDeleted(null);
        rol2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.rolRepository.save(rol2);

        usuario = new Usuario();
        usuario.setName("Nombre");
        usuario.setEmail("Correo");
        usuario.setPassword("Pass");
        usuario.setSilconCode("silconCode");
        usuario.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        usuario.setDeleted(null);
        usuario.setRoleId(rol.getId());
        this.usuarioRepository.save(usuario);

        usuario2 = new Usuario();
        usuario2.setName("Nombre__");
        usuario2.setEmail("Correo__");
        usuario2.setPassword("Pass__");
        usuario2.setSilconCode("silconCode__");
        usuario2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        usuario2.setDeleted(null);
        usuario2.setRoleId(rol2.getId());
        this.usuarioRepository.save(usuario2);

        List<UsuarioGrupo> usuariosGrupo = new ArrayList<>();
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo();
        usuarioGrupo.setUserId(usuario.getId());
        usuarioGrupo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        usuarioGrupo.setGroupId(grupo.getId());
        usuariosGrupo.add(usuarioGrupo);
        usuario.setGroups(usuariosGrupo);
        this.usuarioRepository.save(usuario);

        ArrayList<UsuarioGrupo> usuariosGrupo2 = new ArrayList<>();
        UsuarioGrupo usuarioGrupo2 = new UsuarioGrupo();
        usuarioGrupo2.setUserId(usuario2.getId());
        usuarioGrupo2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        usuarioGrupo2.setGroupId(grupo.getId());
        usuariosGrupo2.add(usuarioGrupo2);
        usuario2.setGroups(usuariosGrupo2);
        this.usuarioRepository.save(usuario2);

        tipoElementoPromocionable = new TipoElementoCatalogo();
        tipoElementoPromocionable.setName("Elemento Promocionable");
        tipoElementoPromocionable.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        tipoElementoPromocionable.setDeleted(null);
        this.tipoElementoCatalogoRepository.save(tipoElementoPromocionable);

        tipoAgrupacionFuncional = new TipoElementoCatalogo();
        tipoAgrupacionFuncional.setName("Agrupación funcional");
        tipoAgrupacionFuncional.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        tipoAgrupacionFuncional.setDeleted(null);
        this.tipoElementoCatalogoRepository.save(tipoAgrupacionFuncional);

        tipoProyecto = new TipoElementoCatalogo();
        tipoProyecto.setName("Proyecto");
        tipoProyecto.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        tipoProyecto.setDeleted(null);
        this.tipoElementoCatalogoRepository.save(tipoProyecto);

        parentElement = new ElementoCatalogo();
        parentElement.setName("ParentElementoCatalogo");
        parentElement.setCappCode("EC0");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        parentElement.setDeleted(null);
        this.elemenCatalogoRepository.save(parentElement);

        elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setParentElement(parentElement);
        elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        preffixAtributoEje = new AtributoEje();
        preffixAtributoEje.setName("Preffix");
        preffixAtributoEje.setCode("ATTR1");
        preffixAtributoEje.setMultiple(0);
        preffixAtributoEje.setFromCapp(0);
        preffixAtributoEje.setFromCapp(1);
        preffixAtributoEje.setValuesInDomain(0);
        preffixAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        preffixAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(preffixAtributoEje);

        nameAtributoEje = new AtributoEje();
        nameAtributoEje.setName("Name");
        nameAtributoEje.setCode("ATTR2");
        nameAtributoEje.setMultiple(0);
        nameAtributoEje.setFromCapp(0);
        nameAtributoEje.setFromCapp(1);
        nameAtributoEje.setValuesInDomain(0);
        nameAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        nameAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(nameAtributoEje);

        codeAtributoEje = new AtributoEje();
        codeAtributoEje.setName("Code");
        codeAtributoEje.setCode("ATTR3");
        codeAtributoEje.setMultiple(0);
        codeAtributoEje.setFromCapp(0);
        codeAtributoEje.setFromCapp(1);
        codeAtributoEje.setValuesInDomain(0);
        codeAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        codeAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(codeAtributoEje);

        responsibleAtributoEje = new AtributoEje();
        responsibleAtributoEje.setName("Responsible");
        responsibleAtributoEje.setCode("ATTR6");
        responsibleAtributoEje.setMultiple(0);
        responsibleAtributoEje.setFromCapp(0);
        responsibleAtributoEje.setFromCapp(1);
        responsibleAtributoEje.setValuesInDomain(0);
        responsibleAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        responsibleAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(responsibleAtributoEje);

        serviceNameAtributoEje = new AtributoEje();
        serviceNameAtributoEje.setName("ServiceName");
        serviceNameAtributoEje.setCode("ATTR11");
        serviceNameAtributoEje.setMultiple(0);
        serviceNameAtributoEje.setFromCapp(0);
        serviceNameAtributoEje.setFromCapp(1);
        serviceNameAtributoEje.setValuesInDomain(0);
        serviceNameAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        serviceNameAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(serviceNameAtributoEje);

        functionalAreaNameAtributoEje = new AtributoEje();
        functionalAreaNameAtributoEje.setName("FunctionalAreaName");
        functionalAreaNameAtributoEje.setCode("ATTR12");
        functionalAreaNameAtributoEje.setMultiple(0);
        functionalAreaNameAtributoEje.setFromCapp(0);
        functionalAreaNameAtributoEje.setFromCapp(1);
        functionalAreaNameAtributoEje.setValuesInDomain(0);
        functionalAreaNameAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        functionalAreaNameAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(functionalAreaNameAtributoEje);

        computerProcessingAtributoEje = new AtributoEje();
        computerProcessingAtributoEje.setName("ComputerProcessing");
        computerProcessingAtributoEje.setCode("ATTR18");
        computerProcessingAtributoEje.setMultiple(0);
        computerProcessingAtributoEje.setFromCapp(0);
        computerProcessingAtributoEje.setFromCapp(1);
        computerProcessingAtributoEje.setValuesInDomain(0);
        computerProcessingAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        computerProcessingAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(computerProcessingAtributoEje);

        groupAtributoEje = new AtributoEje();
        groupAtributoEje.setName("Group");
        groupAtributoEje.setCode("ATTR19");
        groupAtributoEje.setMultiple(0);
        groupAtributoEje.setFromCapp(0);
        groupAtributoEje.setFromCapp(1);
        groupAtributoEje.setValuesInDomain(0);
        groupAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        groupAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(groupAtributoEje);

        situationAtributoEje = new AtributoEje();
        situationAtributoEje.setName("Situation");
        situationAtributoEje.setCode("ATTR33");
        situationAtributoEje.setMultiple(0);
        situationAtributoEje.setFromCapp(0);
        situationAtributoEje.setFromCapp(1);
        situationAtributoEje.setValuesInDomain(0);
        situationAtributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        situationAtributoEje.setDeleted(null);
        this.atributoEjeRepository.save(situationAtributoEje);



    }

    @Test
    @Transactional
    public void testElementoCatalogo_borradoLogico() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        elementoCatalogo.setAttributeValuesCollection(new ArrayList<>());

        this.elemenCatalogoRepository.save(elementoCatalogo);

        try {
            ElementoCatalogo removedElement = this.elementoCatalogoService.borradoLogico(elementoCatalogo.getId());
            assertNotNull(removedElement);
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void testElementoCatalogo_borradoLogico_exceptionThrown() {
        ElementoCatalogo subElement = new ElementoCatalogo();
        subElement.setName("SubElement");
        subElement.setCappCode("EC2");
        subElement.setGroupId(grupo.getId());
        subElement.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        subElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ArrayList<ElementoCatalogo> subElements = new ArrayList<>();
        subElements.add(subElement);
        elementoCatalogo.setSubElements(subElements);

        this.elemenCatalogoRepository.save(elementoCatalogo);

        try {
            this.elementoCatalogoService.borradoLogico(elementoCatalogo.getId());
        } catch (ValidationRulesException e) {
            assertNotNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    public void testElementoCatalogo_evaluateBusinessRules_exceptionThrown_1() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        try {
            ElementoCatalogo createdElement = this.elementoCatalogoService.insertar(elementoCatalogo);
        } catch (ValidationRulesException e) {
            assertNotNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    public void testElementoCatalogo_evaluateBusinessRules_exceptionThrown_2() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());

        try {
            ElementoCatalogo createdElement = this.elementoCatalogoService.insertar(elementoCatalogo);
        } catch (ValidationRulesException e) {
            assertNotNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    public void testElementoCatalogo_evaluateBusinessRules_exceptionThrown_3() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCatalogElementCollateralId(1);

        try {
            ElementoCatalogo createdElement = this.elementoCatalogoService.insertar(elementoCatalogo);
        } catch (ValidationRulesException e) {
            assertNotNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_evaluateBusinessRules_exceptionThrown_4() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ArrayList<ValoresEjesDeElemenCatalogoUsuario> attributeValues = new ArrayList<>();
        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();

        ValorDominioDeAttrElemCat domainValue = new ValorDominioDeAttrElemCat();
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDomainValueId(1);
        domainValue.setDeleted(null);
        domainValues.add(domainValue);

        ValoresEjesDeElemenCatalogoUsuario attributeValue = new ValoresEjesDeElemenCatalogoUsuario();
        attributeValue.setCatalogElementId(1);
        attributeValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        attributeValue.setDomainValues(domainValues);
        attributeValue.setAxisAttributeId(1);
        attributeValues.add(attributeValue);
        elementoCatalogo.setAttributeValuesCollection(attributeValues);

        try {
            ElementoCatalogo createdElement = this.elementoCatalogoService.insertar(elementoCatalogo);
        } catch (ValidationRulesException e) {
            assertNotNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getByCollateralId() {
        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("Parent Element");
        parentElement.setCappCode("EC1");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.elemenCatalogoRepository.save(parentElement);

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC2");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.elemenCatalogoRepository.save(elementoCatalogo);

        List<ElementoCatalogo> catalogueElements = this.elementoCatalogoService.getByCollateralId(parentElement.getId());
        assertFalse(catalogueElements.isEmpty());

        this.elemenCatalogoRepository.delete(parentElement);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getById() {
        ElementoCatalogo returnedElement = this.elementoCatalogoService.getById(elementoCatalogo.getId());
        assertNotNull(returnedElement);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getByName() {
        ElementoCatalogo returnedElement = this.elementoCatalogoService.getByName("ElementoCatalogo");
        assertNotNull(returnedElement);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getByCappCode() {
        ElementoCatalogo returnedElement = this.elementoCatalogoService.getByCappCode("EC1");
        assertNotNull(returnedElement);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getAllByGroup() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo_");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        Collection<ElementoCatalogo> elementos = this.elementoCatalogoService.getAllByGroup(usuario.getId());
        assertFalse(elementos.isEmpty());

        this.elemenCatalogoRepository.delete(elementoCatalogo);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_searchByFilterForList() {
        ValorDominio domainValue = new ValorDominio();
        domainValue.setName("Domain value name");
        domainValue.setAxisAttributeId(preffixAtributoEje.getId());
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDeleted(null);
        this.valorDominioRepository.save(domainValue);

        List<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr = new ValorDominioDeAttrElemCat();
        domainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr.setDeleted(null);
        domainValueAttr.setDomainValueId(domainValue.getId());
        domainValue.setElementDomainValues(domainValues);
        domainValues.add(domainValueAttr);
        this.valorDominioRepository.save(domainValue);

        ValoresEjesDeElemenCatalogoUsuario preffixValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        preffixValoresEjes.setAxisAttributeId(preffixAtributoEje.getId());
        preffixValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        preffixValoresEjes.setDeleted(null);
        preffixValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        preffixValoresEjes.setDomainValues(domainValues);
        preffixValoresEjes.setUserValue(preffixAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(preffixValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario nameValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        nameValoresEjes.setAxisAttributeId(nameAtributoEje.getId());
        nameValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        nameValoresEjes.setDeleted(null);
        nameValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        nameValoresEjes.setDomainValues(domainValues);
        nameValoresEjes.setUserValue(nameAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(nameValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario codeValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        codeValoresEjes.setAxisAttributeId(codeAtributoEje.getId());
        codeValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        codeValoresEjes.setDeleted(null);
        codeValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        codeValoresEjes.setDomainValues(domainValues);
        codeValoresEjes.setUserValue(codeAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(codeValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario responsibleValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        responsibleValoresEjes.setAxisAttributeId(responsibleAtributoEje.getId());
        responsibleValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        responsibleValoresEjes.setDeleted(null);
        responsibleValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        responsibleValoresEjes.setDomainValues(domainValues);
        responsibleValoresEjes.setUserValue(responsibleAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(responsibleValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario serviceNameValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        serviceNameValoresEjes.setAxisAttributeId(serviceNameAtributoEje.getId());
        serviceNameValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        serviceNameValoresEjes.setDeleted(null);
        serviceNameValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        serviceNameValoresEjes.setDomainValues(domainValues);
        serviceNameValoresEjes.setUserValue(serviceNameAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(serviceNameValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario functionalAreaNameValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        functionalAreaNameValoresEjes.setAxisAttributeId(functionalAreaNameAtributoEje.getId());
        functionalAreaNameValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        functionalAreaNameValoresEjes.setDeleted(null);
        functionalAreaNameValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        functionalAreaNameValoresEjes.setDomainValues(domainValues);
        functionalAreaNameValoresEjes.setUserValue(functionalAreaNameAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(functionalAreaNameValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario computerProcessingValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        computerProcessingValoresEjes.setAxisAttributeId(computerProcessingAtributoEje.getId());
        computerProcessingValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        computerProcessingValoresEjes.setDeleted(null);
        computerProcessingValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        computerProcessingValoresEjes.setDomainValues(domainValues);
        computerProcessingValoresEjes.setUserValue(computerProcessingAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(computerProcessingValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario groupValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        groupValoresEjes.setAxisAttributeId(groupAtributoEje.getId());
        groupValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        groupValoresEjes.setDeleted(null);
        groupValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        groupValoresEjes.setDomainValues(domainValues);
        groupValoresEjes.setUserValue(groupAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(groupValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario situationValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        situationValoresEjes.setAxisAttributeId(situationAtributoEje.getId());
        situationValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        situationValoresEjes.setDeleted(null);
        situationValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        situationValoresEjes.setDomainValues(domainValues);
        situationValoresEjes.setUserValue(situationAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(situationValoresEjes);



        CatalogueElementFilter filtro = new CatalogueElementFilter();
        filtro.setUserId(usuario.getId());
        filtro.setPreffix(preffixAtributoEje.getCode());
        filtro.setName(nameAtributoEje.getCode());
        filtro.setCode(codeAtributoEje.getCode());
        filtro.setResponsible(responsibleAtributoEje.getCode());
        filtro.setServiceName(serviceNameAtributoEje.getCode());
        filtro.setFunctionalAreaName(functionalAreaNameAtributoEje.getCode());
        filtro.setComputerProcessing(computerProcessingAtributoEje.getCode());
        filtro.setGroup(groupAtributoEje.getCode());
        filtro.setSituation(situationAtributoEje.getCode());
        filtro.setIdOfCatalogueElementType(tipoElementoPromocionable.getId());

        List<ValoresEjesDeElemenCatalogoUsuario> v = this.valoresEjesDeElemenCatalogoUsuarioRepository.findAllByAxisAttributeIdAndDeletedIsNullAndUserValueContaining(nameAtributoEje.getId(), filtro.getName());

        Collection<CatalogueElementFilter> elementFilters = this.elementoCatalogoService.searchByFilterForList(filtro);
        assertFalse(elementFilters.isEmpty());

    }

    @Test
    @Transactional
    public void testElementoCatalogo_searchElementsByFilter_1() {
        CatalogueElementFilter filtro = new CatalogueElementFilter();
        filtro.setUserId(usuario.getId());

        Collection<ElementoCatalogo> elementos = this.elementoCatalogoService.searchElementsByFilter(filtro);
        assertFalse(elementos.isEmpty());
    }

    @Test
    @Transactional
    public void testElementoCatalogo_searchElementsByFilter_2() {
        CatalogueElementFilter filtro = new CatalogueElementFilter();
        filtro.setUserId(usuario.getId());
        filtro.setIdOfCatalogueElementType(tipoElementoPromocionable.getId());

        Collection<ElementoCatalogo> elementos = this.elementoCatalogoService.searchElementsByFilter(filtro);
        assertNotNull(elementos);
        assertFalse(elementos.isEmpty());
    }

    @Test
    @Transactional
    public void testElementoCatalogo_searchElementsByFilter_3() {
        ValorDominio domainValue = new ValorDominio();
        domainValue.setName("Domain value name");
        domainValue.setAxisAttributeId(computerProcessingAtributoEje.getId());
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDeleted(null);
        this.valorDominioRepository.save(domainValue);

        List<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr = new ValorDominioDeAttrElemCat();
        domainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr.setDeleted(null);
        domainValueAttr.setDomainValueId(domainValue.getId());
        domainValue.setElementDomainValues(domainValues);
        domainValues.add(domainValueAttr);
        this.valorDominioRepository.save(domainValue);

        ValoresEjesDeElemenCatalogoUsuario computerProcessingValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        computerProcessingValoresEjes.setAxisAttributeId(computerProcessingAtributoEje.getId());
        computerProcessingValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        computerProcessingValoresEjes.setDeleted(null);
        computerProcessingValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        computerProcessingValoresEjes.setDomainValues(domainValues);
        computerProcessingValoresEjes.setUserValue(computerProcessingAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(computerProcessingValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario groupValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        groupValoresEjes.setAxisAttributeId(groupAtributoEje.getId());
        groupValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        groupValoresEjes.setDeleted(null);
        groupValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        groupValoresEjes.setDomainValues(domainValues);
        groupValoresEjes.setUserValue(groupAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(groupValoresEjes);

        ValoresEjesDeElemenCatalogoUsuario situationValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        situationValoresEjes.setAxisAttributeId(situationAtributoEje.getId());
        situationValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        situationValoresEjes.setDeleted(null);
        situationValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        situationValoresEjes.setDomainValues(domainValues);
        situationValoresEjes.setUserValue(situationAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(situationValoresEjes);

        CatalogueElementFilter filtro = new CatalogueElementFilter();
        filtro.setUserId(usuario.getId());
        filtro.setComputerProcessingId(computerProcessingAtributoEje.getId());
        filtro.setGroupId(groupAtributoEje.getId());
        filtro.setSituationId(situationAtributoEje.getId());

        Collection<ElementoCatalogo> elementos = this.elementoCatalogoService.searchElementsByFilter(filtro);
        assertNotNull(elementos);
        assertFalse(elementos.isEmpty());
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId() {
        AtributoEjePorTipoElemento atributoEjePorTipoElemento = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento.setAxisAttributeId(preffixAtributoEje.getId());
        atributoEjePorTipoElemento.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        atributoEjePorTipoElemento.setForCatalogue(1);
        atributoEjePorTipoElemento.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento);

        AtributoEjePorTipoElemento atributoEjePorTipoElemento2 = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento2.setAxisAttributeId(nameAtributoEje.getId());
        atributoEjePorTipoElemento2.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        atributoEjePorTipoElemento2.setForCatalogue(1);
        atributoEjePorTipoElemento2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento2);

        AtributoEjePorTipoElemento atributoEjePorTipoElemento3 = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento3.setAxisAttributeId(responsibleAtributoEje.getId());
        atributoEjePorTipoElemento3.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        atributoEjePorTipoElemento3.setForCatalogue(1);
        atributoEjePorTipoElemento3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento3);

        ValoresEjesDeElemenCatalogoUsuario codeValoresEjes = new ValoresEjesDeElemenCatalogoUsuario();
        codeValoresEjes.setAxisAttributeId(codeAtributoEje.getId());
        codeValoresEjes.setCatalogElementId(elementoCatalogo.getId());
        codeValoresEjes.setDeleted(null);
        codeValoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        codeValoresEjes.setDomainValues(domainValues);
        codeValoresEjes.setUserValue(codeAtributoEje.getCode());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(codeValoresEjes);

        List<ValoresEjesDeElemenCatalogoUsuario> attrCollection = new ArrayList<>();
        attrCollection.add(codeValoresEjes);
        elementoCatalogo.setAttributeValuesCollection(attrCollection);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        List<ElementoCatalogo> elements = this.elementoCatalogoService.getFreeAndNotFreeElementsByUserGroupIdAndCatalogueTypeId(usuario.getId(), tipoElementoPromocionable.getId());
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getFreeElementsByUserGroupIdAndCatalogueTypeId() {
        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("ParentElementoCatalogo_getFree");
        parentElement.setCappCode("EC0");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        parentElement.setDeleted(null);

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo_getFree");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setParentElement(parentElement);
        //elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        List<ElementoCatalogo> elements = this.elementoCatalogoService.getFreeElementsByUserGroupIdAndCatalogueTypeId(usuario.getId(), tipoElementoPromocionable.getId());
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
    }

    @Test
    @Transactional
    public void testElementoCatalogo_getHierarchyById() {
        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("Parent element");
        parentElement.setCappCode("EC1");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoProyecto.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.elemenCatalogoRepository.save(parentElement);

        ElementoCatalogo subElement = new ElementoCatalogo();
        subElement.setName("SubElement");
        subElement.setCappCode("EC2");
        subElement.setGroupId(grupo.getId());
        subElement.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        subElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("elementoCatalogo");
        elementoCatalogo.setCappCode("EC3");
        elementoCatalogo.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);

        elementoCatalogo.setParentElement(parentElement);
        ArrayList<ElementoCatalogo> subElements = new ArrayList<>();
        subElements.add(subElement);
        elementoCatalogo.setSubElements(subElements);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        List<CatalogueNode> catalogueNodes = this.elementoCatalogoService.getHierarchyById(elementoCatalogo.getId());
        assertNotNull(catalogueNodes);
        assertFalse(catalogueNodes.isEmpty());

        this.elemenCatalogoRepository.delete(parentElement);
    }

    @Test
    @Transactional
    public void testElementoCatalogo_onlyUpdate() {
        ElementoCatalogo updatedElement = this.elementoCatalogoService.onlyUpdate(elementoCatalogo);
        assertNotNull(updatedElement.getUpdateDate());
    }

    @Test
    @Transactional
    public void testCreate1ElementPromo_whenNoRuleExceptionThrown() {
        Grupo grupo = new Grupo();
        //grupo.setId(1);
        grupo.setName("grupo A");
        grupo.setDeleted(null);
        grupo.setDeliveries(null);
        grupo.setCatalogueElements(null);
        grupo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.grupoRepository.save(grupo);

        ElementoCatalogo elemenPromo = new ElementoCatalogo();
        elemenPromo.setGroupId(grupo.getId());
        elemenPromo.setCatalogElementTypeId(1);
        elemenPromo.setName("MS Backend Catalogo de MACA");
        elemenPromo.setCappCode("MAC0");
        elemenPromo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elemenPromo.setAttributeValuesCollection(new ArrayList<>());
        this.elemenCatalogoRepository.save(elemenPromo);

        ElementoCatalogo elemenPromoSaved = null;
        try {
            //metemos el grupo 1 si no está ya metido desde otro método
            if (grupoRepository.findByIdAndDeletedIsNull(grupo.getId()) == null) {
                grupo = grupoRepository.save(grupo);
                assertNotNull(grupo);
                assertNotNull(grupo.getId());
                assertNotNull(grupo.getCreationDate());
            }
            if (elemenPromo.getId() != null && elementoCatalogoService.getById(elemenPromo.getId()) != null) {
                elemenPromo = elementoCatalogoService.borradoLogico(elemenPromo.getId());
                assertNotNull(elemenPromo.getDeleted().intValue() == 1);

                //elemenPromo.setId(null);
                elemenPromo.setDeleted(null);
                elemenPromo.setUpdateDate(null);
                elemenPromo.setCappCode("MAC0");
                elemenPromoSaved = elementoCatalogoService.insertar(elemenPromo);
                assertNotNull(elemenPromoSaved.getCreationDate());
            }
        } catch (ValidationRulesException e) {
            assertNotNull(null, "Update op.: Error business rule: " + e.getMessage());
        }

        this.elemenCatalogoRepository.deleteAll();
        this.grupoRepository.deleteAll();

    }

    @Test
    @Transactional
    public void testUpdateAxisAttribute_whenCorrectDomainValue() {
        ArrayList<ValorDominio> domainValues = new ArrayList<>();
        AtributoEje axisAttribute = new AtributoEje();
        ValorDominio domainValue = new ValorDominio();
        AtributoEje createdAxis;
        AtributoEje updatedAxisAttribute;
        ValorDominio createdDomainValue;

        domainValue.setName("Valor dominio prueba");
        domainValue.setAxisAttributeId(1);
        domainValues.add(domainValue);
        axisAttribute.setName("Placeholder");
        axisAttribute.setFromCapp(0);
        axisAttribute.setMultiple(0);
        axisAttribute.setValuesInDomain(1);
        axisAttribute.setCode("ATTR19");
        createdAxis = atributoEjeService.insertar(axisAttribute);
        assertNotNull(createdAxis.getCreationDate());
        createdDomainValue = valorDominioService.insertar(domainValue);
        assertNotNull(createdDomainValue.getCreationDate());
        axisAttribute.setDomainValues(domainValues);
        updatedAxisAttribute = atributoEjeService.actualizar(axisAttribute);
        assertEquals(Integer.valueOf(1), updatedAxisAttribute.getDomainValues().get(0).getAxisAttributeId());
    }

    @Test
    @Transactional
    public void testDeleteCatalogueElement() {
        ElementoCatalogo elemenPromo = new ElementoCatalogo();
        elemenPromo.setGroupId(grupo.getId());
        elemenPromo.setCatalogElementTypeId(1);
        elemenPromo.setName("MS Backend Catalogo de MACA");
        elemenPromo.setCappCode("MAC0");
        elemenPromo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elemenPromo.setAttributeValuesCollection(new ArrayList<>());
        this.elemenCatalogoRepository.save(elemenPromo);

        ElementoCatalogo deletedCatalogueElement;
        try {
            deletedCatalogueElement = elementoCatalogoService.borradoLogico(elemenPromo.getId());
            assertNotNull(deletedCatalogueElement);
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void test_ElementoCatalogo_insertar() {
        AtributoEje atributoEje = new AtributoEje();
        atributoEje.setName("NombreEje");
        atributoEje.setCode("ATTRX");
        atributoEje.setMultiple(0);
        atributoEje.setFromCapp(0);
        atributoEje.setFromCapp(1);
        atributoEje.setValuesInDomain(1);
        atributoEje.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        atributoEje.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje);
        atributoEje.setAxisAttributeCollateralId(atributoEje.getId());
        this.atributoEjeRepository.save(atributoEje);

        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("Parent Element");
        parentElement.setCappCode("EC1");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.elemenCatalogoRepository.save(parentElement);

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("app_elementoCatalogo");
        elementoCatalogo.setCappCode("EC2");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ValorDominio valorDominio = new ValorDominio();
        valorDominio.setName("domainValueName");
        valorDominio.setAxisAttributeId(atributoEje.getId());
        valorDominio.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominio.setDeleted(null);
        this.valorDominioRepository.save(valorDominio);

        ArrayList<ValoresEjesDeElemenCatalogoUsuario> attributeValues = new ArrayList<>();
        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();

        ValorDominioDeAttrElemCat domainValue = new ValorDominioDeAttrElemCat();
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDomainValueId(valorDominio.getId());
        //domainValue.setId(1);
        domainValue.setDeleted(null);
        domainValues.add(domainValue);

        ValoresEjesDeElemenCatalogoUsuario attributeValue = new ValoresEjesDeElemenCatalogoUsuario();
        attributeValue.setCatalogElementId(1);
        attributeValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        attributeValue.setDomainValues(domainValues);
        attributeValue.setAxisAttributeId(atributoEje.getId());
        attributeValues.add(attributeValue);
        attributeValue.setDomainValues(domainValues);
        elementoCatalogo.setAttributeValuesCollection(attributeValues);

        ValorDominioCondicionadoPor valorDominioCondicionadoPor = new ValorDominioCondicionadoPor();
        valorDominioCondicionadoPor.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominioCondicionadoPor.setDeleted(null);
        valorDominioCondicionadoPor.setDomainValueId(domainValue.getId());
        valorDominioCondicionadoPor.setDomainValueCollateralId(domainValue.getDomainValueId());
        this.valorDominioCondicionadoRepository.save(valorDominioCondicionadoPor);

        try {
            ElementoCatalogo createdElement = this.elementoCatalogoService.insertar(elementoCatalogo);
            assertNotNull(createdElement);
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void test_ElementoCatalogo_actualizar_1() {
        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("elementoCatalogo");
        parentElement.setCappCode("EC1");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(3);
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("app_elementoCatalogo");
        elementoCatalogo.setCappCode("EC2");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(2);
        elementoCatalogo.setCatalogElementCollateralId(parentElement.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        //elementoCatalogo.setId(1);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        ValorDominio valorDominio = new ValorDominio();
        valorDominio.setName("domainValueName");
        valorDominio.setAxisAttributeId(preffixAtributoEje.getId());
        valorDominio.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominio.setDeleted(null);
        this.valorDominioRepository.save(valorDominio);

        ArrayList<ValoresEjesDeElemenCatalogoUsuario> attributeValues = new ArrayList<>();
        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();

        ValorDominioDeAttrElemCat domainValue = new ValorDominioDeAttrElemCat();
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDomainValueId(valorDominio.getId());
        domainValue.setDeleted(null);
        domainValues.add(domainValue);

        ValoresEjesDeElemenCatalogoUsuario attributeValue = new ValoresEjesDeElemenCatalogoUsuario();
        attributeValue.setCatalogElementId(elementoCatalogo.getId());
        attributeValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        attributeValue.setDomainValues(domainValues);
        attributeValue.setAxisAttributeId(preffixAtributoEje.getId());
        attributeValues.add(attributeValue);
        elementoCatalogo.setAttributeValuesCollection(attributeValues);

        try {
            ElementoCatalogo updatedElement = this.elementoCatalogoService.actualizar(elementoCatalogo);
            assertNotNull(updatedElement.getUpdateDate());
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(parentElement);
    }

    @Test
    @Transactional
    public void test_ElementoCatalogo_actualizar_2() {
        ElementoCatalogo parentElement = new ElementoCatalogo();
        parentElement.setName("elementoCatalogo");
        parentElement.setCappCode("EC1");
        parentElement.setGroupId(grupo.getId());
        parentElement.setCatalogElementTypeId(tipoProyecto.getId());
        parentElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        List<ElementoCatalogo> subElements = new ArrayList<>();
        ElementoCatalogo subElement = new ElementoCatalogo();
        subElement.setName("elementoCatalogo");
        subElement.setCappCode("EC2");
        subElement.setGroupId(grupo.getId());
        subElement.setCatalogElementTypeId(tipoElementoPromocionable.getId());
        subElement.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        subElements.add(subElement);

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("app_elementoCatalogo");
        elementoCatalogo.setCappCode("EC3");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoAgrupacionFuncional.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setParentElement(parentElement);
        elementoCatalogo.setSubElements(subElements);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        ValorDominio valorDominio = new ValorDominio();
        valorDominio.setName("domainValueName");
        valorDominio.setAxisAttributeId(preffixAtributoEje.getId());
        valorDominio.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valorDominio.setDeleted(null);
        this.valorDominioRepository.save(valorDominio);

        ArrayList<ValoresEjesDeElemenCatalogoUsuario> attributeValues = new ArrayList<>();
        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();

        ValorDominioDeAttrElemCat domainValue = new ValorDominioDeAttrElemCat();
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDomainValueId(valorDominio.getId());
        //domainValue.setId(1);
        domainValue.setDeleted(null);
        domainValues.add(domainValue);

        ValoresEjesDeElemenCatalogoUsuario attributeValue = new ValoresEjesDeElemenCatalogoUsuario();
        attributeValue.setCatalogElementId(elementoCatalogo.getId());
        attributeValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        attributeValue.setDomainValues(domainValues);
        attributeValue.setAxisAttributeId(preffixAtributoEje.getId());
        attributeValues.add(attributeValue);
        elementoCatalogo.setAttributeValuesCollection(attributeValues);

        try {
            ElementoCatalogo updatedElement = this.elementoCatalogoService.actualizar(elementoCatalogo);
            assertNotNull(updatedElement.getUpdateDate());
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }

        this.elemenCatalogoRepository.delete(parentElement);
    }



}
