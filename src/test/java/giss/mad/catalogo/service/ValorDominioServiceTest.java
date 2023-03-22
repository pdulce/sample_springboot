package giss.mad.catalogo.service;

import giss.mad.catalogo.model.AtributoEje;
import giss.mad.catalogo.model.ValorDominio;
import giss.mad.catalogo.repository.AtributoEjeRepository;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import giss.mad.catalogo.repository.ValorDominioRepository;
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
public class ValorDominioServiceTest {
    @Mock
    private ValorDominioRepository valorDominioRepository;
    @Mock
    private AtributoEjeRepository atributoEjeRepository;
    @Mock
    private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

    @InjectMocks
    private ValorDominioService valorDominioService;
    private static ValorDominio valorDominio;
    private static AtributoEje atributoEje;
    private static List<AtributoEje> atributosEje;
    private static List<ValorDominio> valoresDominio;

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
        atributoEje.setName("grupo A");
        atributoEje.setAxis(1);
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

    }

    @Test
    public void testGetAllValoresDominio() {

        when(valorDominioRepository.findAllByDeletedIsNull(Sort.by("id"))).thenReturn(valoresDominio);
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);

        Collection<ValorDominio> result = valorDominioService.getAll();
        assertEquals(1, result.size());
        verify(valorDominioRepository, times(1)).findAllByDeletedIsNull(Sort.by("id"));
    }

    @Test
    public void testGetValorDominioById() {
        when(valorDominioRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominio);

        ValorDominio result = valorDominioService.get(90);
        assertEquals("Momentum", result.getName());
        verify(valorDominioRepository, times(1)).findByIdAndDeletedIsNull(90);
    }

    @Test
    public void testCreateValorDominio() {
        valorDominio.setName("Momentum");
        valorDominio.setDeleted(null);
        when(valorDominioRepository.save(valorDominio)).thenReturn(valorDominio);
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);

        ValorDominio result = valorDominioService.insertar(valorDominio);
        assertEquals("Momentum", result.getName());
        assertNotNull(result.getCreationDate());
        verify(valorDominioRepository, times(1)).save(valorDominio);
    }

    @Test
    public void testUpdateValorDominio() {

        valorDominio.setDeleted(null);
        valorDominio.setName("Momentum");

        when(valorDominioRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominio);
        when(atributoEjeRepository.findByIdAndDeletedIsNull(100)).thenReturn(atributoEje);
        when(valorDominioRepository.save(valorDominio)).thenReturn(valorDominio);

        ValorDominio result = valorDominioService.actualizar(valorDominio);
        assertEquals("Momentum", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(valorDominioRepository, times(1)).save(valorDominio);
    }

    @Test
    public void testBajaLogicaValorDominio() {
        when(valorDominioRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominio);
        when(valorDominioRepository.save(valorDominio)).thenReturn(valorDominio);

        ValorDominio result = valorDominioService.borradoLogico(90);
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(valorDominioRepository, times(1)).save(valorDominio);
    }



}
