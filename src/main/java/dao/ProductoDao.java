package dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import modelo.Producto;
import java.sql.SQLException;
import java.util.List;

/**
 * Acceso a datos para la entidad Producto.
 */
public class ProductoDao {
    private final Dao<Producto, Integer> productoDao;

    /**
     * Inicializa el DAO.
     * @param source Conexión ORMLite
     * @throws SQLException si ocurre un error.
     */
    public ProductoDao(ConnectionSource source) throws SQLException {
        productoDao = DaoManager.createDao(source, Producto.class);
    }

    /**
     * Agrega un producto.
     */
    public void agregar(Producto p) throws SQLException { productoDao.create(p); }

    /**
     * Lista todos los productos.
     */
    public List<Producto> obtenerTodos() throws SQLException { return productoDao.queryForAll(); }

    /**
     * Actualiza un producto.
     */
    public void actualizar(Producto p) throws SQLException { productoDao.update(p); }

    /**
     * Elimina un producto.
     */
    public void eliminar(Producto p) throws SQLException { productoDao.delete(p); }
}