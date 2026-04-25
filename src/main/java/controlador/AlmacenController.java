package controlador;

import dao.AlmacenDao;
import dao.ProductoDao;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Almacen;
import modelo.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la gestión de almacenes.
 * Permite agregar, editar, eliminar y mostrar almacenes, así como mostrar el número de productos en cada uno.
 * Este controlador está asociado con la vista AlmacenView.fxml.
 */
public class AlmacenController {

    /** Tabla para mostrar los almacenes en la interfaz. */
    @FXML
    private TableView<AlmacenDTO> tablaAlmacenes;

    /** Columna para ID del almacén. */
    @FXML
    private TableColumn<AlmacenDTO, Integer> colId;

    /** Columna para nombre del almacén. */
    @FXML
    private TableColumn<AlmacenDTO, String> colNombre;

    /** Columna para cantidad de productos en el almacén. */
    @FXML
    private TableColumn<AlmacenDTO, Integer> colCantidad;

    /** Campo de texto para ingresar o editar el nombre del almacén. */
    @FXML
    private TextField txtNombre;

    /** Botones de la interfaz para agregar, editar y eliminar almacenes. */
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;

    /** Data Access Object (DAO) para acceso a almacenes. */
    private AlmacenDao almacenDao;

    /** Data Access Object (DAO) para acceso a productos. */
    private ProductoDao productoDao;

    /** Lista observable utilizada por la tabla de almacenes. */
    private ObservableList<AlmacenDTO> listaAlmacenes;

    /**
     * Clase auxiliar (DTO) para mostrar información de cada almacén junto a la cantidad de productos que tiene.
     */
    public static class AlmacenDTO {
        private final int id;
        private final String nombre;
        private final int cantidadProductos;

        /**
         * Constructor del DTO de almacén.
         * @param id ID del almacén.
         * @param nombre Nombre del almacén.
         * @param cantidadProductos Total de productos asociados a ese almacén.
         */
        public AlmacenDTO(int id, String nombre, int cantidadProductos) {
            this.id = id;
            this.nombre = nombre;
            this.cantidadProductos = cantidadProductos;
        }

        /**
         * @return ID del almacén.
         */
        public int getId() { return id; }

        /**
         * @return Nombre del almacén.
         */
        public String getNombre() { return nombre; }

        /**
         * @return Total de productos en este almacén.
         */
        public int getCantidadProductos() { return cantidadProductos; }
    }

    /**
     * Inicializa el controlador y configura la tabla, DAOs y listeners.
     * Se ejecuta automáticamente al cargar la vista.
     */
    @FXML
    public void initialize() {
        try {
            almacenDao = new AlmacenDao(DatabaseManager.getConnectionSource());
            productoDao = new ProductoDao(DatabaseManager.getConnectionSource());

            // Configura cómo la tabla obtiene los datos de cada columna
            colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
            colCantidad.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getCantidadProductos()).asObject());

            // Llena la tabla de almacenes
            cargarAlmacenes();

            // Acciones de los botones
            btnAgregar.setOnAction(e -> agregarAlmacen());
            btnEditar.setOnAction(e -> editarAlmacen());
            btnEliminar.setOnAction(e -> eliminarAlmacen());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Llena o actualiza la tabla de almacenes con su respectivo número de productos.
     * Recorre cada almacén, cuenta los productos asociados y los muestra.
     * @throws SQLException si ocurre un error con la base de datos.
     */
    private void cargarAlmacenes() throws SQLException {
        List<Almacen> almacenes = almacenDao.obtenerTodos();
        listaAlmacenes = FXCollections.observableArrayList(
                almacenes.stream().map(a -> {
                    try {
                        // Cuenta productos asociados a este almacén
                        int count = (int) productoDao.obtenerTodos().stream()
                                .filter(p -> p.getAlmacen() != null && p.getAlmacen().getId() == a.getId())
                                .count();
                        return new AlmacenDTO(a.getId(), a.getNombre(), count);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return new AlmacenDTO(a.getId(), a.getNombre(), 0);
                    }
                }).collect(Collectors.toList())
        );
        tablaAlmacenes.setItems(listaAlmacenes);
    }

    /**
     * Agrega un nuevo almacén con el nombre ingresado.
     * Llama a la base de datos y actualiza la tabla.
     * Muestra alerta si el campo está vacío o ante error de BD.
     */
    private void agregarAlmacen() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }
        try {
            almacenDao.agregar(new Almacen(nombre));
            cargarAlmacenes();
            txtNombre.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar almacén.");
        }
    }

    /**
     * Edita el nombre del almacén seleccionado en la tabla.
     * Busca el almacén original por ID, actualiza el nombre y guarda.
     * Valida que haya selección y campo de texto válido.
     */
    private void editarAlmacen() {
        AlmacenDTO seleccionado = tablaAlmacenes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un almacén para editar.");
            return;
        }
        String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            mostrarAlerta("El nombre no puede estar vacío.");
            return;
        }
        try {
            Almacen almacen = almacenDao.obtenerPorId(seleccionado.getId());
            almacen.setNombre(nuevoNombre);
            almacenDao.actualizar(almacen);
            cargarAlmacenes();
            txtNombre.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al editar almacén.");
        }
    }

    /**
     * Elimina el almacén seleccionado de la base de datos.
     * Valida selección previa y actualiza la tabla.
     */
    private void eliminarAlmacen() {
        AlmacenDTO seleccionado = tablaAlmacenes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un almacén para eliminar.");
            return;
        }
        try {
            Almacen almacen = almacenDao.obtenerPorId(seleccionado.getId());
            almacenDao.eliminar(almacen);
            cargarAlmacenes();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al eliminar almacén.");
        }
    }

    /**
     * Muestra una alerta/diálogo básico con el mensaje dado.
     * @param mensaje Mensaje a mostrar al usuario.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}