package giss.mad.catalogo.service;

import giss.mad.catalogo.model.AtributoEjePorTipoElemento;
import giss.mad.catalogo.model.TipoElementoCatalogo;
import giss.mad.catalogo.repository.AtributoEjePorTipoElementoRepository;
import giss.mad.catalogo.repository.TipoElementoCatalogoRepository;
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
public class TipoElementoServiceTest {
    @Mock
    private TipoElementoCatalogoRepository tipoElementoCatalogoRepository;

    @Mock
    private AtributoEjePorTipoElementoRepository atributoEjePorTipoElementoRepository;
    @InjectMocks
    private TipoElementoCatalogoService tipoElementoCatalogoService;
    private static TipoElementoCatalogo tipoElementoCatalogo;
    private static List<TipoElementoCatalogo> tiposElementoCatalogo;
    private static AtributoEjePorTipoElemento atributoEjePorTipoElemento;
    private static List<AtributoEjePorTipoElemento> atributosPorTipoElemento;

    @BeforeAll
    public static void setUp() {

        atributoEjePorTipoElemento = new AtributoEjePorTipoElemento();
        atributoEjePorTipoElemento.setId(100);
        atributoEjePorTipoElemento.setDeleted(null);
        atributoEjePorTipoElemento.setAxisAttributeId(200);
        atributoEjePorTipoElemento.setCatalogElementTypeId(1);
        atributoEjePorTipoElemento.setForCatalogue(1);
        atributoEjePorTipoElemento.setForDelivery(2);
        atributosPorTipoElemento = new ArrayList<>();
        atributosPorTipoElemento.add(atributoEjePorTipoElemento);

        tipoElementoCatalogo = new TipoElementoCatalogo();
        tipoElementoCatalogo.setId(200);
        tipoElementoCatalogo.setHierarchyLevel(5);
        tipoElementoCatalogo.setName("tipo catalogo");
        tipoElementoCatalogo.setAtributosAsociados(atributosPorTipoElemento);
        tipoElementoCatalogo.setDeleted(null);
        tiposElementoCatalogo = new ArrayList<>();
        tiposElementoCatalogo.add(tipoElementoCatalogo);
    }

    @Test
    public void testGetAllTiposElementos() {

        when(tipoElementoCatalogoRepository.findAllByDeletedIsNull(Sort.by("hierarchyLevel"))).
                thenReturn(tiposElementoCatalogo);

        Collection<TipoElementoCatalogo> result = tipoElementoCatalogoService.getAll();
        assertEquals(1, result.size());
        verify(tipoElementoCatalogoRepository, times(1)).findAllByDeletedIsNull(
                Sort.by("hierarchyLevel"));
    }

    @Test
    public void testGetTiposElementosById() {
        when(tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(100)).thenReturn(tipoElementoCatalogo);

        TipoElementoCatalogo result = tipoElementoCatalogoService.get(100);
        assertEquals("tipo catalogo", result.getName());
        assertEquals(5, result.getHierarchyLevel().intValue());
        verify(tipoElementoCatalogoRepository, times(1)).findByIdAndDeletedIsNull(100);
    }

    @Test
    public void testCreateTipoElemento() {
        tipoElementoCatalogo.setName("tipo catalogo");
        tipoElementoCatalogo.setAtributosAsociados(atributosPorTipoElemento);
        tipoElementoCatalogo.setDeleted(null);
        when(tipoElementoCatalogoRepository.save(tipoElementoCatalogo)).thenReturn(tipoElementoCatalogo);

        TipoElementoCatalogo result = tipoElementoCatalogoService.insertar(tipoElementoCatalogo);
        assertEquals("tipo catalogo", result.getName());
        assertNotNull(result.getCreationDate());
        verify(tipoElementoCatalogoRepository, times(2)).save(tipoElementoCatalogo);
    }

    @Test
    public void testUpdateTipoElemento() {
        tipoElementoCatalogo.setDeleted(null);
        tipoElementoCatalogo.setName("tipo catalogo");
        when(tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(200)).thenReturn(tipoElementoCatalogo);
        //atributoEjePorTipoElementoRepository.delete(
        when(tipoElementoCatalogoRepository.save(tipoElementoCatalogo)).thenReturn(tipoElementoCatalogo);

        TipoElementoCatalogo result = tipoElementoCatalogoService.actualizar(tipoElementoCatalogo);
        assertEquals("tipo catalogo", result.getName());
        assertNotNull(result.getUpdateDate());
        verify(tipoElementoCatalogoRepository, times(1)).save(tipoElementoCatalogo);
    }

    @Test
    public void testBajaLogicaTipoElemento()  {
        when(tipoElementoCatalogoRepository.findByIdAndDeletedIsNull(100)).thenReturn(tipoElementoCatalogo);
        when(tipoElementoCatalogoRepository.save(tipoElementoCatalogo)).thenReturn(tipoElementoCatalogo);

        TipoElementoCatalogo result = tipoElementoCatalogoService.borradoLogico(100);
        assertEquals("1", String.valueOf(result.getDeleted()));
        verify(tipoElementoCatalogoRepository, times(1)).save(tipoElementoCatalogo);
    }


}
