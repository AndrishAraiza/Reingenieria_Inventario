package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ProductoTest {

    @Test
    void testConstructorAndGetters() {
        Producto producto = new Producto("Teclado", 5, "Mecánico RGB");
        assertEquals("Teclado", producto.getNombre());
        assertEquals(5, producto.getCantidad());
        assertEquals("Mecánico RGB", producto.getDescripcion());
    }

    @Test
    void testSetters() {
        Producto producto = new Producto();
        producto.setNombre("Mouse");
        producto.setCantidad(8);
        producto.setDescripcion("Óptico inalámbrico");

        assertAll(
                () -> assertEquals("Mouse", producto.getNombre()),
                () -> assertEquals(8, producto.getCantidad()),
                () -> assertEquals("Óptico inalámbrico", producto.getDescripcion())
        );
    }
}