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
public class AtributoEjePorTipoElementoServiceTest {

    @Mock
    private ElementoCatalogoRepository elementoCatalogoRepository;
    @Mock
    private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;
    @Mock
    private AtributoEjeRepository atributoEjeRepository;
    @Mock
    private ValorDominioRepository valorDominioRepository;
    @Mock
    private ValoresEjesDeElemenCatalogoUsuarioRepository valoresEjesDeElemenCatalogoUsuarioRepository;
    @InjectMocks
    private AtributoEjePorTipoElementoService atributoEjePorTipoElementoService;
    private static AtributoEjePorTipoElemento atributoEjePorTipoElemento;
    private static AtributoEje atributoEje;
    private static List<AtributoEjePorTipoElemento> atributosEjePorTipoElemento;
    private static ValorDominio valorDominio;
    private static List<ValorDominio> valoresDominio;


    @BeforeAll
    public static void setUp() {

        atributoEjePorTipoElemento = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento.setId(90);
        atributoEjePorTipoElemento.setCatalogElementTypeId(1);
        atributoEjePorTipoElemento.setForDelivery(1);
        atributoEjePorTipoElemento.setForCatalogue(1);
        atributoEjePorTipoElemento.setDeleted(null);
        atributoEjePorTipoElemento.setAxisAttributeId(100);
        atributosEjePorTipoElemento = new ArrayList<>();
        atributosEjePorTipoElemento.add(atributoEjePorTipoElemento);
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
    }

    @Test
    public void testGetAllAtributos() {
        when(atributoEjePorTipoElementoRepository.findAllByDeletedIsNull()).thenReturn(atributosEjePorTipoElemento);

        Collection<AtributoEjePorTipoElemento> result = atributoEjePorTipoElementoService.getAll();
        assertEquals(1, result.size());
        verify(atributoEjePorTipoElementoRepository, times(1)).findAllByDeletedIsNull();
    }

    @Test
    public void testGetAtributoById() {
        when(atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(90)).thenReturn(atributoEjePorTipoElemento);

        AtributoEjePorTipoElemento result = atributoEjePorTipoElementoService.get(90);
        assertEquals("100", String.valueOf(result.getAxisAttributeId()));
        verify(atributoEjePorTipoElementoRepository, times(1)).findByIdAndDeletedIsNull(90);
    }

    @Test
    public void testCreateAtributo() {
        when(atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento)).thenReturn(
                atributoEjePorTipoElemento);

        AtributoEjePorTipoElemento result = atributoEjePorTipoElementoService.insertar(atributoEjePorTipoElemento);
        assertEquals("100", String.valueOf(result.getAxisAttributeId()));
        assertNotNull(result.getCreationDate());
        verify(atributoEjePorTipoElementoRepository, times(1)).save(atributoEjePorTipoElemento);
    }
    @Test
    public void testUpdateAtributo() {

        atributoEjePorTipoElemento.setDeleted(null);

        when(atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(90)).thenReturn(atributoEjePorTipoElemento);
        when(atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento)).thenReturn(
                atributoEjePorTipoElemento);

        AtributoEjePorTipoElemento result = atributoEjePorTipoElementoService.update(atributoEjePorTipoElemento);
        assertEquals("100", String.valueOf(result.getAxisAttributeId()));
        assertNotNull(result.getUpdateDate());
        verify(atributoEjePorTipoElementoRepository, times(1)).save(atributoEjePorTipoElemento);
    }
    @Test
    public void testBajaLogicaAtributo() {
        when(atributoEjePorTipoElementoRepository.findByIdAndDeletedIsNull(90)).thenReturn(atributoEjePorTipoElemento);
        when(atributoEjePorTipoElementoRepository.save(atributoEjePorTipoElemento)).
                thenReturn(atributoEjePorTipoElemento);

        AtributoEjePorTipoElemento result = atributoEjePorTipoElementoService.remove(90);
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(atributoEjePorTipoElementoRepository, times(1)).save(atributoEjePorTipoElemento);
    }

    @Test
    public void testGetAllByTipoCatalogueAndNotDelivery(){
        AtributoEje attr1 = new AtributoEje();
        attr1.setId(31);
        AtributoEje attr3 = new AtributoEje();
        attr3.setId(33);
        ElementoCatalogo elem = new ElementoCatalogo();
        elem.setId(333);
        elem.setCatalogElementTypeId(1);

        when(atributoEjePorTipoElementoRepository.
                findAllByDeletedIsNullAndCatalogElementTypeIdAndForCatalogue(
                        1/*elem.getCatalogElementTypeId()*/, 1,
                        Sort.by(Sort.Order.asc("axisAttributeId")))).
                                thenReturn(atributosEjePorTipoElemento);
        when(atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1")).thenReturn(attr1);
        when(atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3")).thenReturn(attr3);
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);
        /*when(valorDominioRepository.findAllByDeletedIsNullAndAxisAttributeId(100,
                Sort.by(Sort.Order.asc("id")))).thenReturn(valoresDominio);*/

        List<AtributoEje> result = atributoEjePorTipoElementoService.
                getAllByTipoCatalogueAndNotDelivery(1);
        assertEquals("1", String.valueOf(result.size()));

        verify(atributoEjePorTipoElementoRepository, times(1)).
                findAllByDeletedIsNullAndCatalogElementTypeIdAndForCatalogue(1, 1,
                        Sort.by(Sort.Order.asc("axisAttributeId")));

    }

    @Test
    public void testGetAllByIdCatalogueForDelivery(){
        AtributoEje attr1 = new AtributoEje();
        attr1.setId(31);
        AtributoEje attr3 = new AtributoEje();
        attr3.setId(33);
        ValoresEjesDeElemenCatalogoUsuario valorEje = new ValoresEjesDeElemenCatalogoUsuario();
        ValorDominioDeAttrElemCat val = new ValorDominioDeAttrElemCat();
        val.setDomainValueId(valorDominio.getId());
        val.setValorEjeElemCatId(valorEje.getId());
        valorEje.setDomainValues(new ArrayList<>());
        valorEje.getDomainValues().add(val);

        ElementoCatalogo elem = new ElementoCatalogo();
        elem.setId(333);
        elem.setCatalogElementTypeId(1);

        when(elementoCatalogoRepository.findByIdAndDeletedIsNull(333)).thenReturn(elem);
        when(atributoEjePorTipoElementoRepository.findAllByDeletedIsNullAndCatalogElementTypeIdAndForDelivery(
                        1, 1, Sort.by(Sort.Order.asc("axisAttributeId")))).
                thenReturn(atributosEjePorTipoElemento);
        when(atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR1")).thenReturn(attr1);
        when(atributoEjeRepository.findByCodeAndDeletedIsNull("ATTR3")).thenReturn(attr3);
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);
        when(valorDominioRepository.findAllByDeletedIsNullAndAxisAttributeId(100,
                Sort.by(Sort.Order.asc("id")))).thenReturn(valoresDominio);
        when(valoresEjesDeElemenCatalogoUsuarioRepository.
                findByAxisAttributeIdAndCatalogElementIdAndDeletedIsNull(100, 333)).
                thenReturn(valorEje);

        List<AtributoEje> result = atributoEjePorTipoElementoService.
                getAllByIdCatalogueForDelivery(333);
        assertEquals("1", String.valueOf(result.size()));

        verify(atributoEjePorTipoElementoRepository, times(1)).
                findAllByDeletedIsNullAndCatalogElementTypeIdAndForDelivery(1, 1,
                        Sort.by(Sort.Order.asc("axisAttributeId")));

    }

}
