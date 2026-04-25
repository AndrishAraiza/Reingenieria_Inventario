package dao;

import modelo.Producto;
import database.DatabaseManager;

import org.junit.jupiter.api.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ProductoDaoTest {

    private static JdbcConnectionSource connectionSource;
    private ProductoDao dao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Conexión en memoria para test: no afecta tu inventario real
        connectionSource = new JdbcConnectionSource("jdbc:sqlite::memory:");
        TableUtils.createTableIfNotExists(connectionSource, Producto.class);
    }

    @AfterAll
    static void tearDown() throws Exception {
        connectionSource.close();
    }

    @BeforeEach
    void prepararDAO() throws SQLException {
        dao = new ProductoDao(connectionSource);
        TableUtils.clearTable(connectionSource, Producto.class); // Limpieza entre pruebas
    }

    @Test
    void testAgregarYObtener() throws Exception {
        Producto p = new Producto("Monitor", 2, "24 pulgadas");
        dao.agregar(p);

        List<Producto> productos = dao.obtenerTodos();
        assertEquals(1, productos.size());
        assertEquals("Monitor", productos.get(0).getNombre());
    }

    @Test
    void testActualizar() throws Exception {
        Producto p = new Producto("CPU", 1, "Intel");
        dao.agregar(p);

        List<Producto> productos = dao.obtenerTodos();
        Producto guardado = productos.get(0);

        guardado.setCantidad(5);
        dao.actualizar(guardado);

        Producto actualizado = dao.obtenerTodos().get(0);
        assertEquals(5, actualizado.getCantidad());
    }

    @Test
    void testEliminar() throws Exception {
        Producto p = new Producto("RAM", 4, "8GB DDR4");
        dao.agregar(p);

        List<Producto> productos = dao.obtenerTodos();
        Producto guardado = productos.get(0);

        dao.eliminar(guardado);
        assertEquals(0, dao.obtenerTodos().size());
    }
}