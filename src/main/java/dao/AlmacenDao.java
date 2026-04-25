package dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import modelo.Almacen;
import java.sql.SQLException;
import java.util.List;

/**
 * Acceso a datos para la entidad Almacen.
 */
public class AlmacenDao {
    private final Dao<Almacen, Integer> almacenDao;

    /**
     * Inicializa el DAO.
     * @param source Conexión ORMLite
     * @throws SQLException si ocurre un error.
     */
    public AlmacenDao(ConnectionSource source) throws SQLException {
        almacenDao = DaoManager.createDao(source, Almacen.class);
    }

    /**
     * Agrega un nuevo almacén.
     * @param a almacén a agregar.
     * @throws SQLException si ocurre un error.
     */
    public void agregar(Almacen a) throws SQLException { almacenDao.create(a); }

    /**
     * Devuelve todos los almacenes.
     */
    public List<Almacen> obtenerTodos() throws SQLException { return almacenDao.queryForAll(); }

    /**
     * Actualiza un almacén.
     */
    public void actualizar(Almacen a) throws SQLException { almacenDao.update(a); }

    /**
     * Elimina un almacén.
     */
    public void eliminar(Almacen a) throws SQLException { almacenDao.delete(a); }

    /**
     * Busca un almacén por ID.
     */
    public Almacen obtenerPorId(int id) throws SQLException { return almacenDao.queryForId(id); }
}