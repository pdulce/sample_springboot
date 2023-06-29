package giss.mad.catalogo.service;

import giss.mad.catalogo.Application;
import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.model.filters.CatalogueElementFilter;
import giss.mad.catalogo.model.filters.DeliveryExtended;
import giss.mad.catalogo.model.filters.DeliveryFilter;
import giss.mad.catalogo.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class},
        properties = {"spring.datasource.url:jdbc:h2:mem:testdb-${random.uuid};INIT=create schema if not exists MACA_CATALOGO"})
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntregaElementoCatalogoServiceTest {

    @Autowired
    AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;
    @Autowired
    private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;
    @Autowired
    private EntregaElementoCatalogoRepository entregaElementoCatalogoRepository;
    @Autowired
    private ValoresEjesDeEntregaUsuarioRepository valoresEjesDeEntregaUsuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ElementoCatalogoRepository elemenCatalogoRepository;
    @Autowired
    private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
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
    @Autowired
    private EntregaElementoCatalogoService entregaElementoCatalogoService;

    /*Objetos para las pruebas*/
    private static Grupo grupo;
    private static ValorDominio valorDominio;
    private static ValorDominioCondicionadoPor valorDominioCondicionado;
    private static AtributoEje atributoEje, atributoEje2, atributoEje3, atributoEje4, atributoEje5, atributoEje6, atributoEje7, atributoEje8, atributoEje9;
    private static TipoElementoCatalogo tipoElementoCatalogo;
    private static ElementoCatalogo elementoCatalogo;
    private static EntregaElementoCatalogo entregaElementoCatalogo;
    private static Usuario usuario, usuario2;
    private static Rol rol, rol2;
    private static Timestamp timestamp;

    @BeforeAll
    public void setUp() {
        timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

        grupo = new Grupo();
        grupo.setCreationDate(timestamp);
        grupo.setName("Group_name");
        grupo.setCatalogueElements(new ArrayList<>());
        grupo.setDeliveries(new ArrayList<>());
        this.grupoRepository.save(grupo);

        rol = new Rol();
        rol.setName("roleName_");
        rol.setDescription("roleDescription_");
        rol.setDeleted(null);
        rol.setCreationDate(timestamp);
        this.rolRepository.save(rol);

        rol2 = new Rol();
        rol2.setName("roleName__");
        rol2.setDescription("roleDescription__");
        rol2.setDeleted(null);
        rol2.setCreationDate(timestamp);
        this.rolRepository.save(rol2);

        usuario = new Usuario();
        usuario.setName("Nombre_");
        usuario.setEmail("Correo_");
        usuario.setPassword("Pass_");
        usuario.setSilconCode("silconCode_");
        usuario.setCreationDate(timestamp);
        usuario.setDeleted(null);
        usuario.setRoleId(rol.getId());
        this.usuarioRepository.save(usuario);

        usuario2 = new Usuario();
        usuario2.setName("Nombre__");
        usuario2.setEmail("Correo__");
        usuario2.setPassword("Pass__");
        usuario2.setSilconCode("silconCode__");
        usuario2.setCreationDate(timestamp);
        usuario2.setDeleted(null);
        usuario2.setRoleId(rol2.getId());
        this.usuarioRepository.save(usuario2);

        ArrayList<UsuarioGrupo> usuariosGrupo = new ArrayList<>();
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo();
        usuarioGrupo.setUserId(usuario.getId());
        usuarioGrupo.setCreationDate(timestamp);
        usuarioGrupo.setGroupId(grupo.getId());
        usuariosGrupo.add(usuarioGrupo);
        usuario.setGroups(usuariosGrupo);
        this.usuarioRepository.save(usuario);

        ArrayList<UsuarioGrupo> usuariosGrupo2 = new ArrayList<>();
        UsuarioGrupo usuarioGrupo2 = new UsuarioGrupo();
        usuarioGrupo2.setUserId(usuario2.getId());
        usuarioGrupo2.setCreationDate(timestamp);
        usuarioGrupo2.setGroupId(grupo.getId());
        usuariosGrupo2.add(usuarioGrupo2);
        usuario2.setGroups(usuariosGrupo2);
        this.usuarioRepository.save(usuario2);

        //REVISAR: estan presentes en BBDD los atributosEje de ElementoCatalogoTest
        atributoEje = new AtributoEje();
        atributoEje.setName("NombreEje");
        atributoEje.setCode("ATTR1");
        atributoEje.setMultiple(0);
        atributoEje.setFromCapp(0);
        atributoEje.setFromCapp(1);
        atributoEje.setValuesInDomain(0);
        atributoEje.setCreationDate(timestamp);
        atributoEje.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje);
        atributoEje.setAxisAttributeCollateralId(atributoEje.getId()); //DESCOMENTAR Y REVISAR
        this.atributoEjeRepository.save(atributoEje);

        atributoEje2 = new AtributoEje();
        atributoEje2.setName("NombreEje2");
        atributoEje2.setCode("ATTR2");
        atributoEje2.setMultiple(0);
        atributoEje2.setFromCapp(0);
        atributoEje2.setFromCapp(1);
        atributoEje2.setValuesInDomain(0);
        atributoEje2.setCreationDate(timestamp);
        atributoEje2.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje2);

        atributoEje3 = new AtributoEje();
        atributoEje3.setName("NombreEje3");
        atributoEje3.setCode("ATTR3");
        atributoEje3.setMultiple(0);
        atributoEje3.setFromCapp(0);
        atributoEje3.setFromCapp(1);
        atributoEje3.setValuesInDomain(0);
        atributoEje3.setCreationDate(timestamp);
        atributoEje3.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje3);

        atributoEje4 = new AtributoEje();
        atributoEje4.setName("NombreEje4");
        atributoEje4.setCode("ATTR6");
        atributoEje4.setMultiple(0);
        atributoEje4.setFromCapp(0);
        atributoEje4.setFromCapp(1);
        atributoEje4.setValuesInDomain(0);
        atributoEje4.setCreationDate(timestamp);
        atributoEje4.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje4);

        atributoEje5 = new AtributoEje();
        atributoEje5.setName("NombreEje5");
        atributoEje5.setCode("ATTR11");
        atributoEje5.setMultiple(0);
        atributoEje5.setFromCapp(0);
        atributoEje5.setFromCapp(1);
        atributoEje5.setValuesInDomain(0);
        atributoEje5.setCreationDate(timestamp);
        atributoEje5.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje5);

        atributoEje6 = new AtributoEje();
        atributoEje6.setName("NombreEje6");
        atributoEje6.setCode("ATTR12");
        atributoEje6.setMultiple(0);
        atributoEje6.setFromCapp(0);
        atributoEje6.setFromCapp(1);
        atributoEje6.setValuesInDomain(0);
        atributoEje6.setCreationDate(timestamp);
        atributoEje6.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje6);

        atributoEje7 = new AtributoEje();
        atributoEje7.setName("NombreEje7");
        atributoEje7.setCode("ATTR18");
        atributoEje7.setMultiple(0);
        atributoEje7.setFromCapp(0);
        atributoEje7.setFromCapp(1);
        atributoEje7.setValuesInDomain(0);
        atributoEje7.setCreationDate(timestamp);
        atributoEje7.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje7);

        atributoEje8 = new AtributoEje();
        atributoEje8.setName("NombreEje8");
        atributoEje8.setCode("ATTR19");
        atributoEje8.setMultiple(0);
        atributoEje8.setFromCapp(0);
        atributoEje8.setFromCapp(1);
        atributoEje8.setValuesInDomain(0);
        atributoEje8.setCreationDate(timestamp);
        atributoEje8.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje8);

        atributoEje9 = new AtributoEje();
        atributoEje9.setName("NombreEje9");
        atributoEje9.setCode("ATTR33");
        atributoEje9.setMultiple(0);
        atributoEje9.setFromCapp(0);
        atributoEje9.setFromCapp(1);
        atributoEje9.setValuesInDomain(0);
        atributoEje9.setCreationDate(timestamp);
        atributoEje9.setDeleted(null);
        this.atributoEjeRepository.save(atributoEje9);

        tipoElementoCatalogo = new TipoElementoCatalogo();
        tipoElementoCatalogo.setName("TipoNombre_");
        tipoElementoCatalogo.setCreationDate(timestamp);
        tipoElementoCatalogo.setDeleted(null);
        this.tipoElementoCatalogoRepository.save(tipoElementoCatalogo);

        elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo__");
        elementoCatalogo.setCappCode("EC1");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoCatalogo.getId());
        elementoCatalogo.setCreationDate(timestamp);
        elementoCatalogo.setDeleted(null);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        entregaElementoCatalogo = new EntregaElementoCatalogo();
        entregaElementoCatalogo.setName("EntregaElementoCatalogo__");
        entregaElementoCatalogo.setGroupId(grupo.getId());
        entregaElementoCatalogo.setCreationDate(timestamp);
        entregaElementoCatalogo.setDeleted(null);
        entregaElementoCatalogo.setCatalogElementId(elementoCatalogo.getId());
        this.entregaElementoCatalogoRepository.save(entregaElementoCatalogo);

    }
    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_searchByFilterForList() {
        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr.setDeleted(null);
        deliveryDomainValues.add(deliveryDomainValueAttr);

        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr = new ValorDominioDeAttrElemCat();
        domainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr.setDeleted(null);
        domainValues.add(domainValueAttr);

        ValorDominio domainValue = new ValorDominio();
        domainValue.setName("Domain value name");
        domainValue.setAxisAttributeId(atributoEje.getId());
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDeleted(null);
        domainValue.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue);

        domainValueAttr.setDomainValueId(domainValue.getId());
        deliveryDomainValueAttr.setDomainValueId(domainValue.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue.setDeliveryDomainValues(deliveryDomainValues);
        this.valorDominioRepository.save(domainValue);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues2 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr2 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr2.setDeleted(null);
        deliveryDomainValues2.add(deliveryDomainValueAttr2);

        ArrayList<ValorDominioDeAttrElemCat> domainValues2 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr2 = new ValorDominioDeAttrElemCat();
        domainValueAttr2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr2.setDeleted(null);
        domainValues2.add(domainValueAttr2);

        ValorDominio domainValue2 = new ValorDominio();
        domainValue2.setName("Domain value name");
        domainValue2.setAxisAttributeId(atributoEje3.getId());
        domainValue2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue2.setDeleted(null);
        domainValue2.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue2);

        domainValueAttr2.setDomainValueId(domainValue2.getId());
        deliveryDomainValueAttr2.setDomainValueId(domainValue2.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue2.setDeliveryDomainValues(deliveryDomainValues2);
        this.valorDominioRepository.save(domainValue2);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues3 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr3 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr3.setDeleted(null);
        deliveryDomainValues3.add(deliveryDomainValueAttr3);

        ArrayList<ValorDominioDeAttrElemCat> domainValues3 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr3 = new ValorDominioDeAttrElemCat();
        domainValueAttr3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr3.setDeleted(null);
        domainValues3.add(domainValueAttr3);

        ValorDominio domainValue3 = new ValorDominio();
        domainValue3.setName("Domain value name");
        domainValue3.setAxisAttributeId(atributoEje2.getId());
        domainValue3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue3.setDeleted(null);
        domainValue3.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue3);

        domainValueAttr3.setDomainValueId(domainValue3.getId());
        deliveryDomainValueAttr3.setDomainValueId(domainValue3.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue3.setDeliveryDomainValues(deliveryDomainValues3);
        this.valorDominioRepository.save(domainValue3);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues4 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr4 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr4.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr4.setDeleted(null);
        deliveryDomainValues4.add(deliveryDomainValueAttr4);

        ArrayList<ValorDominioDeAttrElemCat> domainValues4 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr4 = new ValorDominioDeAttrElemCat();
        domainValueAttr4.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr4.setDeleted(null);
        domainValues4.add(domainValueAttr4);

        ValorDominio domainValue4 = new ValorDominio();
        domainValue4.setName("Domain value name");
        domainValue4.setAxisAttributeId(atributoEje4.getId());
        domainValue4.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue4.setDeleted(null);
        domainValue4.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue4);

        domainValueAttr4.setDomainValueId(domainValue4.getId());
        deliveryDomainValueAttr4.setDomainValueId(domainValue4.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue4.setDeliveryDomainValues(deliveryDomainValues4);
        this.valorDominioRepository.save(domainValue4);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues5 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr5 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr5.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr5.setDeleted(null);
        deliveryDomainValues5.add(deliveryDomainValueAttr5);

        ArrayList<ValorDominioDeAttrElemCat> domainValues5 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr5 = new ValorDominioDeAttrElemCat();
        domainValueAttr5.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr5.setDeleted(null);
        domainValues5.add(domainValueAttr5);

        ValorDominio domainValue5 = new ValorDominio();
        domainValue5.setName("Domain value name");
        domainValue5.setAxisAttributeId(atributoEje5.getId());
        domainValue5.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue5.setDeleted(null);
        domainValue5.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue5);

        domainValueAttr5.setDomainValueId(domainValue5.getId());
        deliveryDomainValueAttr5.setDomainValueId(domainValue5.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue5.setDeliveryDomainValues(deliveryDomainValues5);
        this.valorDominioRepository.save(domainValue5);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues6 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr6 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr6.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr6.setDeleted(null);
        deliveryDomainValues6.add(deliveryDomainValueAttr6);

        ArrayList<ValorDominioDeAttrElemCat> domainValues6 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr6 = new ValorDominioDeAttrElemCat();
        domainValueAttr6.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr6.setDeleted(null);
        domainValues6.add(domainValueAttr6);

        ValorDominio domainValue6 = new ValorDominio();
        domainValue6.setName("Domain value name");
        domainValue6.setAxisAttributeId(atributoEje6.getId());
        domainValue6.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue6.setDeleted(null);
        domainValue6.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue6);

        domainValueAttr6.setDomainValueId(domainValue6.getId());
        deliveryDomainValueAttr6.setDomainValueId(domainValue6.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue6.setDeliveryDomainValues(deliveryDomainValues6);
        this.valorDominioRepository.save(domainValue6);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues7 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr7 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr7.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr7.setDeleted(null);
        deliveryDomainValues7.add(deliveryDomainValueAttr7);

        ArrayList<ValorDominioDeAttrElemCat> domainValues7 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr7 = new ValorDominioDeAttrElemCat();
        domainValueAttr7.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr7.setDeleted(null);
        domainValues7.add(domainValueAttr7);

        ValorDominio domainValue7 = new ValorDominio();
        domainValue7.setName("Domain value name");
        domainValue7.setAxisAttributeId(atributoEje7.getId());
        domainValue7.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue7.setDeleted(null);
        domainValue7.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue7);

        domainValueAttr7.setDomainValueId(domainValue7.getId());
        deliveryDomainValueAttr7.setDomainValueId(domainValue7.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue7.setDeliveryDomainValues(deliveryDomainValues7);
        this.valorDominioRepository.save(domainValue7);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues8 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr8 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr8.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr8.setDeleted(null);
        deliveryDomainValues8.add(deliveryDomainValueAttr8);

        ArrayList<ValorDominioDeAttrElemCat> domainValues8 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr8 = new ValorDominioDeAttrElemCat();
        domainValueAttr8.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr8.setDeleted(null);
        domainValues8.add(domainValueAttr8);

        ValorDominio domainValue8 = new ValorDominio();
        domainValue8.setName("Domain value name");
        domainValue8.setAxisAttributeId(atributoEje8.getId());
        domainValue8.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue8.setDeleted(null);
        domainValue8.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue8);

        domainValueAttr8.setDomainValueId(domainValue8.getId());
        deliveryDomainValueAttr8.setDomainValueId(domainValue8.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue8.setDeliveryDomainValues(deliveryDomainValues8);
        this.valorDominioRepository.save(domainValue8);

        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues9 = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr9 = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr9.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr9.setDeleted(null);
        deliveryDomainValues9.add(deliveryDomainValueAttr9);

        ArrayList<ValorDominioDeAttrElemCat> domainValues9 = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr9 = new ValorDominioDeAttrElemCat();
        domainValueAttr9.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr9.setDeleted(null);
        domainValues9.add(domainValueAttr9);

        ValorDominio domainValue9 = new ValorDominio();
        domainValue9.setName("Domain value name");
        domainValue9.setAxisAttributeId(atributoEje9.getId());
        domainValue9.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue9.setDeleted(null);
        domainValue9.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue9);

        domainValueAttr9.setDomainValueId(domainValue9.getId());
        deliveryDomainValueAttr9.setDomainValueId(domainValue9.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue9.setDeliveryDomainValues(deliveryDomainValues9);
        this.valorDominioRepository.save(domainValue9);

        //Comienzo ValoresEjes necesarios, //TODO insertar en setUp
        ValoresEjesDeEntregaUsuario valoresEjes = new ValoresEjesDeEntregaUsuario();
        valoresEjes.setAxisAttributeId(atributoEje.getId());
        //valoresEjes.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes.setDeleted(null);
        valoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes.setDomainValues(deliveryDomainValues);
        valoresEjes.setUserValue(atributoEje.getCode());
        valoresEjes.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat.setAxisAttributeId(atributoEje.getId());
        valoresEjesCat.setDeleted(null);
        valoresEjesCat.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat.setDomainValues(domainValues);
        valoresEjesCat.setUserValue(atributoEje.getCode());
        valoresEjesCat.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat);

        ValoresEjesDeEntregaUsuario valoresEjes2 = new ValoresEjesDeEntregaUsuario();
        valoresEjes2.setAxisAttributeId(atributoEje3.getId());
        //valoresEjes2.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes2.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes2.setDeleted(null);
        valoresEjes2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes2.setDomainValues(deliveryDomainValues2);
        valoresEjes2.setUserValue(atributoEje3.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes2);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes2);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat2 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat2.setAxisAttributeId(atributoEje3.getId());
        valoresEjesCat2.setDeleted(null);
        valoresEjesCat2.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat2.setDomainValues(domainValues2);
        valoresEjesCat2.setUserValue(atributoEje3.getCode());
        valoresEjesCat2.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat2);

        ValoresEjesDeEntregaUsuario valoresEjes3 = new ValoresEjesDeEntregaUsuario();
        valoresEjes3.setAxisAttributeId(atributoEje2.getId());
        //valoresEjes3.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes3.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes3.setDeleted(null);
        valoresEjes3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes3.setDomainValues(deliveryDomainValues3);
        valoresEjes3.setUserValue(atributoEje2.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes3);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes3);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat3 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat3.setAxisAttributeId(atributoEje2.getId());
        valoresEjesCat3.setDeleted(null);
        valoresEjesCat3.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat3.setDomainValues(domainValues3);
        valoresEjesCat3.setUserValue(atributoEje2.getCode());
        valoresEjesCat3.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat3);

        ValoresEjesDeEntregaUsuario valoresEjes4 = new ValoresEjesDeEntregaUsuario();
        valoresEjes4.setAxisAttributeId(atributoEje4.getId());
        //valoresEjes4.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes4.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes4.setDeleted(null);
        valoresEjes4.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes4.setDomainValues(deliveryDomainValues4);
        valoresEjes4.setUserValue(atributoEje4.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes4);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes4);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat4 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat4.setAxisAttributeId(atributoEje4.getId());
        valoresEjesCat4.setDeleted(null);
        valoresEjesCat4.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat4.setDomainValues(domainValues4);
        valoresEjesCat4.setUserValue(atributoEje4.getCode());
        valoresEjesCat4.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat4);

        ValoresEjesDeEntregaUsuario valoresEjes5 = new ValoresEjesDeEntregaUsuario();
        valoresEjes5.setAxisAttributeId(atributoEje5.getId());
        //valoresEjes5.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes5.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes5.setDeleted(null);
        valoresEjes5.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes5.setDomainValues(deliveryDomainValues5);
        valoresEjes5.setUserValue(atributoEje5.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes5);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes5);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat5 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat5.setAxisAttributeId(atributoEje5.getId());
        valoresEjesCat5.setDeleted(null);
        valoresEjesCat5.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat5.setDomainValues(domainValues5);
        valoresEjesCat5.setUserValue(atributoEje5.getCode());
        valoresEjesCat5.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat5);

        ValoresEjesDeEntregaUsuario valoresEjes6 = new ValoresEjesDeEntregaUsuario();
        valoresEjes6.setAxisAttributeId(atributoEje6.getId());
        //valoresEjes6.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes6.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes6.setDeleted(null);
        valoresEjes6.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes6.setDomainValues(deliveryDomainValues6);
        valoresEjes6.setUserValue(atributoEje6.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes6);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes6);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat6 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat6.setAxisAttributeId(atributoEje6.getId());
        valoresEjesCat6.setDeleted(null);
        valoresEjesCat6.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat6.setDomainValues(domainValues6);
        valoresEjesCat6.setUserValue(atributoEje6.getCode());
        valoresEjesCat6.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat6);

        ValoresEjesDeEntregaUsuario valoresEjes7 = new ValoresEjesDeEntregaUsuario();
        valoresEjes7.setAxisAttributeId(atributoEje7.getId());
        //valoresEjes7.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes7.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes7.setDeleted(null);
        valoresEjes7.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes7.setDomainValues(deliveryDomainValues7);
        valoresEjes7.setUserValue(atributoEje7.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes7);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes7);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat7 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat7.setAxisAttributeId(atributoEje7.getId());
        valoresEjesCat7.setDeleted(null);
        valoresEjesCat7.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat7.setDomainValues(domainValues7);
        valoresEjesCat7.setUserValue(atributoEje7.getCode());
        valoresEjesCat7.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat7);

        ValoresEjesDeEntregaUsuario valoresEjes8 = new ValoresEjesDeEntregaUsuario();
        valoresEjes8.setAxisAttributeId(atributoEje8.getId());
        //valoresEjes8.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes8.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes8.setDeleted(null);
        valoresEjes8.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes8.setDomainValues(deliveryDomainValues8);
        valoresEjes8.setUserValue(atributoEje8.getCode());
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes8);
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes8);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat8 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat8.setAxisAttributeId(atributoEje8.getId());
        valoresEjesCat8.setDeleted(null);
        valoresEjesCat8.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat8.setDomainValues(domainValues8);
        valoresEjesCat8.setUserValue(atributoEje8.getCode());
        valoresEjesCat8.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat8);

        ValoresEjesDeEntregaUsuario valoresEjes9 = new ValoresEjesDeEntregaUsuario();
        valoresEjes9.setAxisAttributeId(atributoEje9.getId());
        //valoresEjes9.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes9.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjes9.setDeleted(null);
        valoresEjes9.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes9.setDomainValues(deliveryDomainValues9);
        valoresEjes9.setUserValue(atributoEje9.getCode());
        //this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjes9);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes9);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat9 = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat9.setAxisAttributeId(atributoEje9.getId());
        valoresEjesCat9.setDeleted(null);
        valoresEjesCat9.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat9.setDomainValues(domainValues9);
        valoresEjesCat9.setUserValue(atributoEje9.getCode());
        valoresEjesCat9.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat9);

        DeliveryFilter filtroEntrega = new DeliveryFilter();
        filtroEntrega.setUserId(usuario.getId());
        filtroEntrega.setName(atributoEje2.getCode());
        filtroEntrega.setResponsible(atributoEje4.getCode());
        filtroEntrega.setServiceName(atributoEje5.getCode());
        filtroEntrega.setFunctionalAreaName(atributoEje6.getCode());
        filtroEntrega.setComputerProcessingId(atributoEje7.getId()); //mm
        filtroEntrega.setGroupId(atributoEje8.getId()); //mm
        filtroEntrega.setSituationId(atributoEje9.getId()); //mm
        filtroEntrega.setIdOfCatalogueElementType(tipoElementoCatalogo.getId());

        List<ValoresEjesDeEntregaUsuario> listaValuesDeElem = this.valoresEjesDeEntregaUsuarioRepository.
                findAllByAxisAttributeIdAndDeletedIsNull(atributoEje7.getId());

        Collection<DeliveryFilter> deliveryFilters = this.entregaElementoCatalogoService.searchByFilterForList(filtroEntrega);
        assertNotNull(deliveryFilters);
        assertFalse(deliveryFilters.isEmpty());

        //this.usuarioRepository.deleteAll();
        //this.entregaElementoCatalogoRepository.deleteAll();
        //this.elemenCatalogoRepository.deleteAll();
        //this.tipoElementoCatalogoRepository.deleteAll();
        //this.atributoEjeRepository.deleteAll();
        //this.valorDominioRepository.deleteAll();
    }

    /*@Test
    @Transactional
    public void testEntregaElementoCatalogo_searchByFilterForList_2() {
        ArrayList<ValorDominioDeAttrEntrega> deliveryDomainValues = new ArrayList<>();
        ValorDominioDeAttrEntrega deliveryDomainValueAttr = new ValorDominioDeAttrEntrega();
        deliveryDomainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryDomainValueAttr.setDeleted(null);
        deliveryDomainValues.add(deliveryDomainValueAttr);

        ArrayList<ValorDominioDeAttrElemCat> domainValues = new ArrayList<>();
        ValorDominioDeAttrElemCat domainValueAttr = new ValorDominioDeAttrElemCat();
        domainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr.setDeleted(null);
        domainValues.add(domainValueAttr);

        ValorDominio domainValue = new ValorDominio();
        domainValue.setName("Domain value name");
        domainValue.setAxisAttributeId(atributoEje2.getId());
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDeleted(null);
        domainValue.setElementDomainValues(domainValues);
        this.valorDominioRepository.save(domainValue);

        domainValueAttr.setDomainValueId(domainValue.getId());
        deliveryDomainValueAttr.setDomainValueId(domainValue.getId());
        //domainValue.setElementDomainValues(domainValues);
        domainValue.setDeliveryDomainValues(deliveryDomainValues);
        this.valorDominioRepository.save(domainValue);

        //Comienzo ValoresEjes necesarios, //TODO insertar en setUp
        ValoresEjesDeEntregaUsuario valoresEjes = new ValoresEjesDeEntregaUsuario();
        valoresEjes.setAxisAttributeId(atributoEje2.getId());
        //valoresEjes.setCatalogElementId(elementoCatalogo.getId());
        valoresEjes.setDeleted(null);
        valoresEjes.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjes.setDomainValues(deliveryDomainValues);
        valoresEjes.setUserValue(atributoEje2.getCode());
        valoresEjes.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjes);

        ValoresEjesDeElemenCatalogoUsuario valoresEjesCat = new ValoresEjesDeElemenCatalogoUsuario();
        valoresEjesCat.setAxisAttributeId(atributoEje2.getId());
        valoresEjesCat.setDeleted(null);
        valoresEjesCat.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesCat.setDomainValues(domainValues);
        valoresEjesCat.setUserValue(atributoEje2.getCode());
        valoresEjesCat.setCatalogElementId(entregaElementoCatalogo.getId());
        this.valoresEjesDeElemenCatalogoUsuarioRepository.save(valoresEjesCat);

        DeliveryFilter filtroEntrega = new DeliveryFilter();
        filtroEntrega.setUserId(1);
        filtroEntrega.setName(atributoEje2.getCode());
        filtroEntrega.setIdOfCatalogueElementType(tipoElementoCatalogo.getId());

        Collection<DeliveryFilter> deliveryFilters = this.entregaElementoCatalogoService.searchByFilterForList(filtroEntrega);
        assertNotNull(deliveryFilters);
        assertFalse(deliveryFilters.isEmpty());

    }*/

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_searchElementsByFilter_1() {

        DeliveryFilter filtroEntrega = new DeliveryFilter();
        filtroEntrega.setUserId(-1);
        filtroEntrega.setIdOfCatalogueElementType(tipoElementoCatalogo.getId());
        /*filtroEntrega.setName(atributoEje2.getCode());
        filtroEntrega.setResponsible(atributoEje4.getCode());
        filtroEntrega.setServiceName(atributoEje5.getCode());
        filtroEntrega.setFunctionalAreaName(atributoEje6.getCode());
        filtroEntrega.setComputerProcessingId(atributoEje7.getId()); //mm
        filtroEntrega.setGroupId(atributoEje8.getId()); //mm
        filtroEntrega.setSituationId(atributoEje9.getId()); //mm*/

        Collection<EntregaElementoCatalogo> deliveries = this.entregaElementoCatalogoService.searchElementsByFilter(filtroEntrega);
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());

        //this.entregaElementoCatalogoRepository.deleteAll();
        //this.elemenCatalogoRepository.deleteAll();
        //this.tipoElementoCatalogoRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_searchElementsByFilter_2() {


        DeliveryFilter filtroEntrega = new DeliveryFilter();
        filtroEntrega.setUserId(usuario2.getId());
        filtroEntrega.setIdOfCatalogueElementType(tipoElementoCatalogo.getId());
        /*filtroEntrega.setName(atributoEje2.getCode());
        filtroEntrega.setResponsible(atributoEje4.getCode());
        filtroEntrega.setServiceName(atributoEje5.getCode());
        filtroEntrega.setFunctionalAreaName(atributoEje6.getCode());
        filtroEntrega.setComputerProcessingId(atributoEje7.getId()); //mm
        filtroEntrega.setGroupId(atributoEje8.getId()); //mm
        filtroEntrega.setSituationId(atributoEje9.getId()); //mm*/

        Collection<EntregaElementoCatalogo> deliveries = this.entregaElementoCatalogoService.searchElementsByFilter(filtroEntrega);
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());

        //this.usuarioRepository.deleteAll();
        //this.entregaElementoCatalogoRepository.deleteAll();
        //this.rolRepository.deleteAll();
        //this.elemenCatalogoRepository.deleteAll();
        //this.tipoElementoCatalogoRepository.deleteAll();

    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_getAllFiltered() {
        Collection<DeliveryExtended> extendedDeliveries = this.entregaElementoCatalogoService.getAllFiltered(null);
        assertNotNull(extendedDeliveries);
        assertFalse(extendedDeliveries.isEmpty());
    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_getAllFiltered_2() {
        DeliveryExtended extendedDelivery = new DeliveryExtended();
        extendedDelivery.setUserId(-1);

        Collection<DeliveryExtended> extendedDeliveries = this.entregaElementoCatalogoService.getAllFiltered(extendedDelivery);
        assertNotNull(extendedDeliveries);
        assertTrue(extendedDeliveries.isEmpty());
    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_getAllFiltered_3() {
        DeliveryExtended extendedDelivery = new DeliveryExtended();
        //extendedDelivery.setStartDate(timestamp);
        extendedDelivery.setUserId(usuario.getId());
        extendedDelivery.setName(entregaElementoCatalogo.getName());

        Collection<DeliveryExtended> extendedDeliveries = this.entregaElementoCatalogoService.getAllFiltered(extendedDelivery);
        assertNotNull(extendedDeliveries);
        assertFalse(extendedDeliveries.isEmpty());
    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_getAllFiltered_4() {
        DeliveryExtended extendedDelivery = new DeliveryExtended();
        //extendedDelivery.setStartDate(timestamp);
        extendedDelivery.setUserId(usuario2.getId());
        extendedDelivery.setName(entregaElementoCatalogo.getName());

        Collection<DeliveryExtended> extendedDeliveries = this.entregaElementoCatalogoService.getAllFiltered(extendedDelivery);
        assertNotNull(extendedDeliveries);
        assertTrue(extendedDeliveries.isEmpty());
    }

    @Test
    @Transactional
    public void testEntregaElementoCatalogo_getAllFiltered_5() {
        DeliveryExtended extendedDelivery = new DeliveryExtended();
        extendedDelivery.setStartDate(timestamp);
        extendedDelivery.setUserId(usuario.getId());
        extendedDelivery.setName(entregaElementoCatalogo.getName());

        Collection<DeliveryExtended> extendedDeliveries = this.entregaElementoCatalogoService.getAllFiltered(extendedDelivery);
        assertNotNull(extendedDeliveries);
        assertFalse(extendedDeliveries.isEmpty());
    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_getByIdOfElement() {

        Collection<EntregaElementoCatalogo> returnedDeliveryElements = this.entregaElementoCatalogoService.getByIdOfElement(entregaElementoCatalogo.getCatalogElementId());
        assertNotNull(returnedDeliveryElements);
        assertFalse(returnedDeliveryElements.isEmpty());

    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_getById() {
        EntregaElementoCatalogo returnedDeliveryElement = this.entregaElementoCatalogoService.getById(entregaElementoCatalogo.getId());
        assertNotNull(returnedDeliveryElement);
    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_getByName() {

        EntregaElementoCatalogo returnedDeliveryElement = this.entregaElementoCatalogoService.getByName(entregaElementoCatalogo.getName());
        assertNotNull(returnedDeliveryElement);

    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_insertar() {
        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo_insertar");
        elementoCatalogo.setCappCode("EC1_");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(tipoElementoCatalogo.getId());
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        this.elemenCatalogoRepository.save(elementoCatalogo);

        EntregaElementoCatalogo entregaElementoCatalogo = new EntregaElementoCatalogo();
        entregaElementoCatalogo.setName("EntregaElementoCatalogo_insertar");
        entregaElementoCatalogo.setGroupId(grupo.getId());
        entregaElementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        entregaElementoCatalogo.setDeleted(null);
        entregaElementoCatalogo.setCatalogElementId(elementoCatalogo.getId());
        entregaElementoCatalogo.setAttributeValuesCollection(new ArrayList<>());
        //this.entregaElementoCatalogoRepository.save(entregaElementoCatalogo);

        /*ValorDominio domainValue = new ValorDominio();
        domainValue.setName("DomainValue");
        domainValue.setAxisAttributeId(atributoEje.getId());
        domainValue.setDeleted(null);
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.valorDominioRepository.save(domainValue);

        List<ValoresEjesDeEntregaUsuario> deliveryAxisUserValues = new ArrayList<>();
        List<ValorDominioDeAttrEntrega> domainValues = new ArrayList<>();
        ValoresEjesDeEntregaUsuario value = new ValoresEjesDeEntregaUsuario();
        value.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        this.entregaElementoCatalogoRepository.delete(entregaElementoCatalogo);
        value.setAxisAttributeId(atributoEje.getId());
        value.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        this.valoresEjesDeEntregaUsuarioRepository.save(value);
        ValorDominioDeAttrEntrega deliveryAttrDomainValue = new ValorDominioDeAttrEntrega();
        deliveryAttrDomainValue.setValorEjeEntregaId(value.getId());
        deliveryAttrDomainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        deliveryAttrDomainValue.setDomainValueId(domainValue.getId());
        domainValues.add(deliveryAttrDomainValue);
        value.setDomainValues(domainValues);
        deliveryAxisUserValues.add(value);
        entregaElementoCatalogo.setAttributeValuesCollection(deliveryAxisUserValues);*/

        try {
            EntregaElementoCatalogo savedDeliveryCatalogueElement = this.entregaElementoCatalogoService.insertar(entregaElementoCatalogo);
            assertNotNull(savedDeliveryCatalogueElement);
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }

        this.entregaElementoCatalogoRepository.delete(entregaElementoCatalogo);
        this.elemenCatalogoRepository.delete(elementoCatalogo);

    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_actualizar() {
        List<ValoresEjesDeEntregaUsuario> deliveryAxisValues = new ArrayList<>();
        ValoresEjesDeEntregaUsuario valoresEjesDeEntregaUsuario = new ValoresEjesDeEntregaUsuario();
        valoresEjesDeEntregaUsuario.setDeliveryCatalogElementId(entregaElementoCatalogo.getId());
        valoresEjesDeEntregaUsuario.setDeleted(null);
        valoresEjesDeEntregaUsuario.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        valoresEjesDeEntregaUsuario.setAxisAttributeId(atributoEje.getId());
        deliveryAxisValues.add(valoresEjesDeEntregaUsuario);
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjesDeEntregaUsuario);

        ArrayList<ValorDominioDeAttrEntrega> attrDomainValues = new ArrayList<>();
        ValorDominioDeAttrEntrega domainValueAttr = new ValorDominioDeAttrEntrega();
        domainValueAttr.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValueAttr.setDeleted(null);
        domainValueAttr.setValorEjeEntregaId(valoresEjesDeEntregaUsuario.getId());
        attrDomainValues.add(domainValueAttr);
        valoresEjesDeEntregaUsuario.setDomainValues(attrDomainValues);
        ValorDominio domainValue = new ValorDominio();
        domainValue.setName("Domain value name");
        domainValue.setAxisAttributeId(atributoEje.getId());
        domainValue.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        domainValue.setDeleted(null);
        domainValue.setDeliveryDomainValues(attrDomainValues);
        this.valorDominioRepository.save(domainValue);
        domainValueAttr.setDomainValueId(domainValue.getId());
        this.valoresEjesDeEntregaUsuarioRepository.save(valoresEjesDeEntregaUsuario);

        entregaElementoCatalogo.setAttributeValuesCollection(deliveryAxisValues);
        this.entregaElementoCatalogoRepository.save(entregaElementoCatalogo);

        try {
            EntregaElementoCatalogo updatedDeliveryCatalogueElement = this.entregaElementoCatalogoService.actualizar(entregaElementoCatalogo);
            assertNotNull(updatedDeliveryCatalogueElement);
        } catch (ValidationRulesException e) {
            assertNull(e.getMessage());
        }


        /*this.valorDominioRepository.deleteAll();
        this.elemenCatalogoRepository.deleteAll();
        this.entregaElementoCatalogoRepository.deleteAll();
        this.valoresEjesDeEntregaUsuarioRepository.deleteAll();
        this.atributoEjeRepository.deleteAll();
        this.grupoRepository.deleteAll();*/

        //this.usuarioRepository.deleteAll();
        //this.entregaElementoCatalogoRepository.deleteAll(); //REVISAR
        //this.elemenCatalogoRepository.deleteAll();
        //this.tipoElementoCatalogoRepository.deleteAll();
        //this.atributoEjeRepository.deleteAll();
        //this.valorDominioRepository.deleteAll();
    }

    @Test
    @Transactional
    public void test_EntregaElementoCatalogo_remove() {

        ElementoCatalogo elementoCatalogo = new ElementoCatalogo();
        elementoCatalogo.setName("ElementoCatalogo_remove");
        elementoCatalogo.setCappCode("EC1_");
        elementoCatalogo.setGroupId(grupo.getId());
        elementoCatalogo.setCatalogElementTypeId(1);
        elementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        elementoCatalogo.setDeleted(null);
        //elementoCatalogo.setAttributeValuesCollection(new ArrayList<>());
        this.elemenCatalogoRepository.save(elementoCatalogo);

        EntregaElementoCatalogo entregaElementoCatalogo = new EntregaElementoCatalogo();
        entregaElementoCatalogo.setName("EntregaElementoCatalogo_remove");
        //elementoCatalogo.setCappCode("EC1");
        entregaElementoCatalogo.setGroupId(grupo.getId());
        //elementoCatalogo.setCatalogElementTypeId(1);
        entregaElementoCatalogo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
        entregaElementoCatalogo.setDeleted(null);
        entregaElementoCatalogo.setCatalogElementId(elementoCatalogo.getId());
        entregaElementoCatalogo.setAttributeValuesCollection(new ArrayList<>());
        this.entregaElementoCatalogoRepository.save(entregaElementoCatalogo);

        EntregaElementoCatalogo deletedDelivery = this.entregaElementoCatalogoService.remove(entregaElementoCatalogo.getId());
        assertNotNull(deletedDelivery);

        //this.usuarioRepository.deleteAll();
        //this.entregaElementoCatalogoRepository.deleteAll(); //REVISAR
        //this.elemenCatalogoRepository.delete(elementoCatalogo);
        //this.tipoElementoCatalogoRepository.deleteAll();
        //this.atributoEjeRepository.deleteAll();
        //this.valorDominioRepository.deleteAll();
    }

}
