package modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Representa un almacén de inventario.
 */
@DatabaseTable(tableName = "almacenes")
public class Almacen {
    /** Identificador único (PK). */
    @DatabaseField(generatedId = true)
    private int id;

    /** Nombre del almacén. */
    @DatabaseField
    private String nombre;

    /** Constructor vacío requerido por ORMLite */
    public Almacen() {}

    /**
     * Constructor normal.
     * @param nombre Nombre del almacén.
     */
    public Almacen(String nombre) { this.nombre = nombre; }

    /** @return ID del almacén. */
    public int getId() { return id; }
    /** @return nombre del almacén. */
    public String getNombre() { return nombre; }
    /** @param nombre cambia el nombre. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Para mostrar correctamente en ComboBox.
     */
    @Override
    public String toString() { return nombre; }
}