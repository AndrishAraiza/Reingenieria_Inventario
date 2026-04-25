package database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import modelo.Almacen;
import modelo.Producto;
import java.sql.SQLException;

/**
 * Maneja la conexión a la base de datos y las tablas.
 */
public class DatabaseManager {
    private static ConnectionSource connectionSource;

    /**
     * Inicializa la base de datos y las tablas.
     * @throws SQLException si hay error
     */
    public static void initialize() throws SQLException {
        if (connectionSource == null) {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:inventario.db");
            TableUtils.createTableIfNotExists(connectionSource, Almacen.class);
            TableUtils.createTableIfNotExists(connectionSource, Producto.class);

        }
    }

    /**
     * Devuelve la conexión.
     */
    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Cierra la conexión.
     */
    public static void close() {
        try {
            if (connectionSource != null)
                connectionSource.close();
        } catch (Exception ignore) {}
    }
}