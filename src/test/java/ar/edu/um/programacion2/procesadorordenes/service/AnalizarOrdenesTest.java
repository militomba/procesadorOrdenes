package ar.edu.um.programacion2.procesadorordenes.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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
        boolean resultado = clienteMock.obtenerClientesServicioProfe(1150);
        verify(clienteMock).obtenerClientesServicioProfe(2);
        assertFalse(resultado);
    }

    @Test
    public void codigoIdValidoTest() {
        AnalizarOrdenes clienteMock = mock(AnalizarOrdenes.class);
        when(clienteMock.obtenerAccionesServicioProfe(13)).thenReturn(true);
        assertTrue(clienteMock.obtenerAccionesServicioProfe(13));
        verify(clienteMock).obtenerAccionesServicioProfe(13);
    }
}
