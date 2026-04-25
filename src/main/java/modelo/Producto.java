package modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Representa un producto en el inventario.
 */
@DatabaseTable(tableName = "productos")
public class Producto {
    /** ID único generado. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Nombre del producto. */
    @DatabaseField
    private String nombre;

    /** Cantidad. */
    @DatabaseField
    private int cantidad;

    /** Descripción del producto. */
    @DatabaseField
    private String descripcion;

    /** Almacén al que pertenece este producto. */
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "almacen_id", canBeNull = false)
    private Almacen almacen;

    /** Constructor vacío requerido por ORMLite */
    public Producto() {}

    /**
     * Constructor normal.
     */
    public Producto(String nombre, int cantidad, String descripcion, Almacen almacen) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.almacen = almacen;
    }

    /** @return ID */
    public int getId() { return id; }
    /** @return nombre */
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    /** @return cantidad */
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    /** @return descripción */
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    /** @return almacén */
    public Almacen getAlmacen() { return almacen; }
    public void setAlmacen(Almacen almacen) { this.almacen = almacen; }
}