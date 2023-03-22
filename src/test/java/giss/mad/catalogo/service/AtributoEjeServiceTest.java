package giss.mad.catalogo.service;

import giss.mad.catalogo.model.*;
import giss.mad.catalogo.model.auxejes.EjeReduced;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtributoEjeServiceTest {
    @Mock
    private AtributoEjeRepository atributoEjeRepository;

    @Mock
    private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;

    @Mock
    private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

    @InjectMocks
    private AtributoEjeService atributoEjeService;
    private static ValorDominio valorDominio;
    private static AtributoEje atributoEje;
    private static List<AtributoEje> atributosEje;
    private static List<ValorDominio> valoresDominio;

    private static TipoElementoCatalogo tipoElementoCatalogo;
    private static List<TipoElementoCatalogo> tiposElementoCatalogo;
    private static AtributoEjePorTipoElemento atributoEjePorTipoElemento;

    private static List<AtributoEjePorTipoElemento> atributosPorTipoElemento;
    @BeforeAll
    public static void setUp() {

        valorDominio = new ValorDominio();
        valorDominio.setId(90);
        valorDominio.setName("Momentum");
        valorDominio.setForDelivery(1);
        valorDominio.setForCatalogue(1);
        valorDominio.setDeleted(null);
        valorDominio.setAxisAttributeId(100);
        valoresDominio = new ArrayList<>();
        valoresDominio.add(valorDominio);

        atributoEje = new AtributoEje();
        atributoEje.setId(100);
        atributoEje.setAxis(1);
        atributoEje.setName("Atributo 44");
        atributoEje.setDeleted(null);
        atributoEje.setDomainValues(valoresDominio);
        atributoEje.setDefaultValue(null);
        atributoEje.setHelp("help soy");
        atributoEje.setCode("ATTR9900");
        atributoEje.setFromCapp(1);
        atributoEje.setAxisAttributeCollateralId(null);
        atributoEje.setValuesInDomain(1);
        atributoEje.setLongDescription(1);
        atributoEje.setMandatory(1);
        atributoEje.setObservations("observaciones");
        atributoEje.setRegex("[\nudueued]");
        atributoEje.setLongDescription(1);
        atributoEje.setDefaultValue(null);
        atributosEje = new ArrayList<>();
        atributosEje.add(atributoEje);

        atributoEjePorTipoElemento = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento.setId(101);
        atributoEjePorTipoElemento.setDeleted(null);
        atributoEjePorTipoElemento.setAxisAttributeId(1);
        atributoEjePorTipoElemento.setCatalogElementTypeId(1);
        atributoEjePorTipoElemento.setForCatalogue(1);
        atributoEjePorTipoElemento.setForDelivery(2);
        atributosPorTipoElemento = new ArrayList<>();
        atributosPorTipoElemento.add(atributoEjePorTipoElemento);

        tipoElementoCatalogo = new TipoElementoCatalogo();
        tipoElementoCatalogo.setId(200);
        tipoElementoCatalogo.setName("tipo catalogo");
        tipoElementoCatalogo.setAtributosAsociados(atributosPorTipoElemento);
        tipoElementoCatalogo.setDeleted(null);
        tiposElementoCatalogo = new ArrayList<>();
        tiposElementoCatalogo.add(tipoElementoCatalogo);
    }

    @Test
    public void testGetAtributoById() {
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);

        AtributoEje result = atributoEjeService.get(100);
        assertEquals("Atributo 44", result.getName());
        verify(atributoEjeRepository, times(1)).findByIdAndDeletedIsNull(100);
    }

    @Test
    public void testCreateAtributo() {
        when(atributoEjeRepository.save(atributoEje)).thenReturn(atributoEje);

        AtributoEje result = atributoEjeService.insertar(atributoEje);
        assertEquals("Atributo 44", result.getName());
        assertNotNull(result.getCreationDate());
        verify(atributoEjeRepository, times(1)).save(atributoEje);
    }
    @Test
    public void testUpdateAtributo() {

        atributoEje.setDeleted(null);
        atributoEje.setName("Atributo 44");

        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);
        when(atributoEjeRepository.save(atributoEje)).thenReturn(atributoEje);

        AtributoEje result = atributoEjeService.actualizar(atributoEje);
        assertEquals("Atributo 44", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(atributoEjeRepository, times(2)).save(atributoEje);
    }
    @Test
    public void testBajaLogicaAtributo() {
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);
        when(atributoEjeRepository.save(atributoEje)).thenReturn(atributoEje);
        when(atributoEjePorTipoElementoRepository.findAllByDeletedIsNullAndAxisAttributeId(atributoEje.getId())).
                thenReturn(new ArrayList<>());

        AtributoEje result = atributoEjeService.borradoLogico(100);
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(atributoEjeRepository, times(1)).save(atributoEje);
    }

    @Test
    public void testGetAllAtributos() {
        when(tipoElementoCatalogoRepository.findAllByDeletedIsNull()).thenReturn(tiposElementoCatalogo);
        Sort sortById = Sort.by(Sort.Order.asc("id"));
        when(atributoEjeRepository.findAllByDeletedIsNull(sortById)).thenReturn(atributosEje);

        Collection<AtributoEje> result = atributoEjeService.getAll();
        assertEquals(1, result.size());
        verify(atributoEjeRepository, times(1)).findAllByDeletedIsNull(Sort.by(
                Sort.Order.asc("id")));
    }

    @Test
    public void testGetAllEjes() {
        when(atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_1,
                Sort.by(Sort.Order.asc("id")))).thenReturn(atributosEje);

        Collection<AtributoEje> result = atributoEjeService.getEjes();
        assertEquals(1, result.size());
        verify(atributoEjeRepository, times(1)).findByAxisAndDeletedIsNull(1,
                Sort.by(Sort.Order.asc("id")));
    }

    @Test
    public void testGetEjesReduced() {
        when(atributoEjeRepository.findByAxisAndDeletedIsNull(1, Sort.by(Sort.Order.asc("id")))).
                thenReturn(atributosEje);

        Collection<EjeReduced> result = atributoEjeService.getEjesReduced();
        assertEquals(1, result.size());
        verify(atributoEjeRepository, times(1)).findByAxisAndDeletedIsNull(1,
                Sort.by(Sort.Order.asc("id")));
    }

    @Test
    public void testGetSoloAtributos() {
        when(atributoEjeRepository.findByAxisAndDeletedIsNull(Constantes.NUMBER_0,
                Sort.by(Sort.Order.asc("id")))).thenReturn(atributosEje);

        Collection<AtributoEje> result = atributoEjeService.getSoloAtributos();
        assertEquals(1, result.size());
        verify(atributoEjeRepository, times(1)).findByAxisAndDeletedIsNull(0,
                Sort.by(Sort.Order.asc("id")));
    }




}
