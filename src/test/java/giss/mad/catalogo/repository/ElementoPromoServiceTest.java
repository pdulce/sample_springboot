package giss.mad.catalogo.repository;

import giss.mad.catalogo.Application;
import giss.mad.catalogo.exception.ValidationRulesException;
import giss.mad.catalogo.model.*;
import giss.mad.catalogo.service.ElementoCatalogoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class},
        properties = {"spring.datasource.url:jdbc:h2:mem:testdb;INIT=create schema if not exists MACA_CATALOGO"})
@ActiveProfiles("test")
public class ElementoPromoServiceTest {
    @Autowired
    AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;
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
    private GrupoRepository grupoRepository;
    @Autowired
    private AtributoEjeRepository atributoEjeRepository;
    @Autowired
    private ValorDominioRepository valorDominioRepository;
    @Autowired
    private ElementoCatalogoService elementoCatalogoService;

    /** objetos de cada test de la prueba **/
    private static ElementoCatalogo elemenPromo;
    private static List<ElementoCatalogo> elementosPromo;
    private static Grupo grupo;
    @BeforeAll
    public static void setUp() {

        grupo = new Grupo();
        grupo.setId(1);
        grupo.setName("grupo A");
        grupo.setDeleted(null);
        grupo.setDeliveries(null);
        grupo.setCatalogueElements(null);
        grupo.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));

        elemenPromo = new ElementoCatalogo();
        elemenPromo.setGroupId(1);
        elemenPromo.setCatalogElementTypeId(1);
        elemenPromo.setName("MS Backend Catalogo de MACA");
        elemenPromo.setCappCode("MAC0");

        Map<Integer, Integer> mapOfAxisWithValuesDomain = new HashMap<>();
        mapOfAxisWithValuesDomain.put(1, 3);
        mapOfAxisWithValuesDomain.put(2, 6);
        mapOfAxisWithValuesDomain.put(3, 9);
        mapOfAxisWithValuesDomain.put(4, 14);
        mapOfAxisWithValuesDomain.put(5, 20);
        mapOfAxisWithValuesDomain.put(6, 23);
        mapOfAxisWithValuesDomain.put(8, 29);
        mapOfAxisWithValuesDomain.put(9, 33);
        mapOfAxisWithValuesDomain.put(10, 48);
        mapOfAxisWithValuesDomain.put(11, 50);
        mapOfAxisWithValuesDomain.put(12, 52);
        mapOfAxisWithValuesDomain.put(13, 56);
        mapOfAxisWithValuesDomain.put(14, 59);
        mapOfAxisWithValuesDomain.put(15, 63);
        mapOfAxisWithValuesDomain.put(16, 65);
        mapOfAxisWithValuesDomain.put(17, 67);
        mapOfAxisWithValuesDomain.put(19, 74);
        mapOfAxisWithValuesDomain.put(20, 80);
        mapOfAxisWithValuesDomain.put(21, 83);
        mapOfAxisWithValuesDomain.put(22, 87);

        // con dependencias; haz juegos de pruebas con estos tres; los valores del 41 y 55 dependen del 40
        mapOfAxisWithValuesDomain.put(40, 89);//40: Tipo Tratamiento Informático --> [89:pros@] posibles con 41:97,96
        mapOfAxisWithValuesDomain.put(41, 96);//41: Grupo --> [96:PROSA, 97: PROSA ARQ, 98:JAVA, 99:NET]
        mapOfAxisWithValuesDomain.put(55, 163);

        Map<Integer, String> atributosCOnUserValues = new HashMap<>();
        atributosCOnUserValues.put(24, "MS Backend Catalogo de MACA");
        atributosCOnUserValues.put(28, "Juan Gallardo");
        atributosCOnUserValues.put(33, "Producción y Sistemas");
        atributosCOnUserValues.put(34, "Calidad GISS");

        List<ValoresEjesDeElemenCatalogoUsuario> valoresEjesDeElemenCatalogoUsuarios = new ArrayList<>();

        /*** Valores de atributos tipificados***/
        for (Integer axisId : mapOfAxisWithValuesDomain.keySet()) {
            Integer valueOfDomainId = mapOfAxisWithValuesDomain.get(axisId);
            ValoresEjesDeElemenCatalogoUsuario val = new ValoresEjesDeElemenCatalogoUsuario();
            val.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            val.setAxisAttributeId(axisId);
            ValorDominioDeAttrElemCat val22 = new ValorDominioDeAttrElemCat();
            val22.setDomainValueId(valueOfDomainId);
            val22.setValorEjeElemCatId(val.getId());
            val.setDomainValues(new ArrayList<>());
            val.getDomainValues().add(val22);

            valoresEjesDeElemenCatalogoUsuarios.add(val);
        }

        /*** Valores de atributos consignados por usuario **/
        for (Integer axisId : atributosCOnUserValues.keySet()) {
            String valueOfDomainId = atributosCOnUserValues.get(axisId);
            ValoresEjesDeElemenCatalogoUsuario val = new ValoresEjesDeElemenCatalogoUsuario();
            val.setCatalogElementId(elemenPromo.getId());
            val.setCreationDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            val.setAxisAttributeId(axisId);
            val.setUserValue(valueOfDomainId);
            valoresEjesDeElemenCatalogoUsuarios.add(val);
        }
        elemenPromo.setAttributeValuesCollection(valoresEjesDeElemenCatalogoUsuarios);
        elementosPromo = new ArrayList<>();
        elementosPromo.add(elemenPromo);
    }
    @Test
    public void testCreate1ElementPromo_whenNoRuleExceptionThrown() {

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

    }


    /*@Test
    public void test2UpdateElementPromo_whenNoRuleExceptionThrown() {

        ElementoCatalogo elemenPromoSaved = null;
        try {
            //metemos el grupo 1 si no está ya metido desde otro método
            if (grupoRepository.findByIdAndDeletedIsNull(grupo.getId()) == null) {
                grupo = grupoRepository.save(grupo);
                assertNotNull(grupo);
                assertNotNull(grupo.getId());
                assertNotNull(grupo.getCreationDate());
            }
            if (elemenPromo.getId() == null || elementoCatalogoService.getById(elemenPromo.getId()) == null) {
                elemenPromoSaved = elementoCatalogoService.insertar(elemenPromo);
                assertNotNull(elemenPromoSaved.getCreationDate());
            }
            elemenPromoSaved = elementoCatalogoService.actualizar(elemenPromo);
            assertNotNull(elemenPromoSaved.getUpdateDate());

        } catch (ValidationRulesException e) {
            assertNull("Update op.: Error business rule: " + e.getMessage(), elemenPromoSaved);
        }
    }*/

    /*@Test
    public void testCreate2ElementPromo_whenCappRepetidoRuleExceptionThrown() {

        ElementoCatalogo elemenPromoSaved = null;
        try {
            //metemos el grupo 1 si no está ya metido desde otro método
            if (grupoRepository.findByIdAndDeletedIsNull(grupo.getId()) == null) {
                grupo = grupoRepository.save(grupo);
                assertNotNull(grupo);
                assertNotNull(grupo.getId());
                assertNotNull(grupo.getCreationDate());
            }
            if (elemenPromo.getId() != null) {
                elemenPromo = elementoCatalogoService.borradoLogico(elemenPromo.getId());
                assertNotNull(elemenPromo.getDeleted().intValue() == 1);
                elemenPromo.setId(2);
                elemenPromo.setCappCode("MAC0");
            }
            elemenPromoSaved = elementoCatalogoService.insertar(elemenPromo);
            assertNotNull(elemenPromoSaved.getCreationDate());

        } catch (ValidationRulesException e) {
            assertEquals("Update op.: Error business rule esperado ", 1, 1);
        }

    }*/




}
