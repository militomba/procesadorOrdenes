package ar.edu.um.programacion2.procesadorordenes.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class AnalizarOrdenesTest {

    @InjectMocks
    AnalizarOrdenes analizarOrdenes;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void clienteValidoTest() {
        AnalizarOrdenes clienteMock = mock(AnalizarOrdenes.class);
        when(clienteMock.obtenerClientesServicioProfe(1149)).thenReturn(true);
        assertTrue(clienteMock.obtenerClientesServicioProfe(1149));
        verify(clienteMock).obtenerClientesServicioProfe(1149);
    }

    @Test
    public void clienteInvalidoTest() {
        AnalizarOrdenes clienteMock = mock(AnalizarOrdenes.class);
        when(clienteMock.obtenerClientesServicioProfe(2)).thenReturn(false);
        boolean resultado = clienteMock.obtenerClientesServicioProfe(2);
        verify(clienteMock).obtenerClientesServicioProfe(2);
        assertFalse(resultado);
    }

    @Test
    public void codigoIdValidoTest() {
        AnalizarOrdenes codigoMock = mock(AnalizarOrdenes.class);
        when(codigoMock.obtenerAccionesServicioProfe(13)).thenReturn(true);
        assertTrue(codigoMock.obtenerAccionesServicioProfe(13));
        verify(codigoMock).obtenerAccionesServicioProfe(13);
    }

    @Test
    public void codigoIdInvalidoTest() {
        AnalizarOrdenes codigoMock = mock(AnalizarOrdenes.class);
        when(codigoMock.obtenerAccionesServicioProfe(555)).thenReturn(false);
        boolean resultado = codigoMock.obtenerAccionesServicioProfe(555);
        verify(codigoMock).obtenerAccionesServicioProfe(555);
        assertFalse(resultado);
    }
}
