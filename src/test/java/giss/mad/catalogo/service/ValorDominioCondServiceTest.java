package giss.mad.catalogo.service;

import giss.mad.catalogo.model.ValorDominioCondicionadoPor;
import giss.mad.catalogo.repository.ValorDominioCondicionadoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValorDominioCondServiceTest {
    @Mock
    private ValorDominioCondicionadoRepository valorDominioCondicionadoRepository;

    @InjectMocks
    private ValorDominioCondicionadoService valorDominioCondicionadoService;
    private static ValorDominioCondicionadoPor valorDominioCondicionadoPor;
    private static List<ValorDominioCondicionadoPor> valoresDominioCondicionados;

    @BeforeAll
    public static void setUp() {
        valorDominioCondicionadoPor = new ValorDominioCondicionadoPor();
        valorDominioCondicionadoPor.setId(90);
        valorDominioCondicionadoPor.setDomainValueId(25);
        valorDominioCondicionadoPor.setDomainValueCollateralId(50);
        valorDominioCondicionadoPor.setDeleted(null);
        valoresDominioCondicionados = new ArrayList<>();
        valoresDominioCondicionados.add(valorDominioCondicionadoPor);
    }

    @Test
    public void testGetAllValoresDominio() {

        when(valorDominioCondicionadoRepository.findAllByDeletedIsNull()).thenReturn(valoresDominioCondicionados);

        Collection<ValorDominioCondicionadoPor> result = valorDominioCondicionadoService.getAll();
        assertEquals(1, result.size());
        verify(valorDominioCondicionadoRepository, times(1)).findAllByDeletedIsNull();
    }

    @Test
    public void testGetValorDominioById() {
        when(valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominioCondicionadoPor);

        ValorDominioCondicionadoPor result = valorDominioCondicionadoService.get(90);
        assertEquals("25", String.valueOf(result.getDomainValueId()));
        verify(valorDominioCondicionadoRepository, times(1)).findByIdAndDeletedIsNull(90);
    }

    @Test
    public void testCreateValorDominio() {
        when(valorDominioCondicionadoRepository.save(valorDominioCondicionadoPor)).thenReturn(valorDominioCondicionadoPor);

        ValorDominioCondicionadoPor result = valorDominioCondicionadoService.insertar(valorDominioCondicionadoPor);
        assertEquals("25", String.valueOf(result.getDomainValueId()));
        assertNotNull(result.getCreationDate());
        verify(valorDominioCondicionadoRepository, times(1)).save(valorDominioCondicionadoPor);
    }

    @Test
    public void testUpdateValorDominio() {

        valorDominioCondicionadoPor.setDeleted(null);

        when(valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominioCondicionadoPor);
        when(valorDominioCondicionadoRepository.save(valorDominioCondicionadoPor)).thenReturn(valorDominioCondicionadoPor);

        ValorDominioCondicionadoPor result = valorDominioCondicionadoService.update(valorDominioCondicionadoPor);
        assertEquals("25", String.valueOf(result.getDomainValueId()));
        assertNotNull(result.getUpdateDate());
        verify(valorDominioCondicionadoRepository, times(1)).save(valorDominioCondicionadoPor);
    }

    @Test
    public void testBajaLogicaValorDominio() {
        when(valorDominioCondicionadoRepository.findByIdAndDeletedIsNull(90)).thenReturn(valorDominioCondicionadoPor);
        when(valorDominioCondicionadoRepository.save(valorDominioCondicionadoPor)).thenReturn(valorDominioCondicionadoPor);

        ValorDominioCondicionadoPor result = valorDominioCondicionadoService.remove(90);
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(valorDominioCondicionadoRepository, times(1)).save(valorDominioCondicionadoPor);
    }


}
