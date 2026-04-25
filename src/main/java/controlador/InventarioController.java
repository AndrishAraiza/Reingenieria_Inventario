package controlador;

import dao.ProductoDao;
import dao.AlmacenDao;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Almacen;
import modelo.Producto;

import java.sql.SQLException;

/**
 * Controlador de la vista InventarioView.fxml.
 * Gestiona las acciones para productos: agregar, editar, eliminar y
 * visualiza el inventario junto con su almacén correspondiente.
 */
public class InventarioController {

    /** Tabla principal donde se visualizan los productos */
    @FXML
    private TableView<Producto> tablaProductos;
    /** Columna ID de producto */
    @FXML
    private TableColumn<Producto, Integer> colId;
    /** Columna nombre de producto */
    @FXML
    private TableColumn<Producto, String> colNombre;
    /** Columna cantidad de producto */
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    /** Columna descripción de producto */
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    /** Columna almacén del producto */
    @FXML
    private TableColumn<Producto, String> colAlmacen;

    /** Campos de texto para los datos de producto */
    @FXML
    private TextField txtNombre, txtCantidad, txtDescripcion;
    /** ComboBox con los almacenes disponibles */
    @FXML
    private ComboBox<Almacen> comboAlmacen;
    /** Botones principales del inventario */
    @FXML
    private Button btnAgregar, btnEditar, btnEliminar, btnAlmacenes;

    /** DAO para operar sobre productos */
    private ProductoDao productoDao;
    /** DAO para operar sobre almacenes */
    private AlmacenDao almacenDao;
    /** Lista observable que respalda la tabla de productos */
    private ObservableList<Producto> listaProductos;

    /**
     * Inicialización automática del controlador (al cargarse la vista).
     * Crea DAOs, configura columnas, llena la tabla y almacenes,
     * asocia eventos a botones y actualiza campos al seleccionar producto.
     */
    @FXML
    public void initialize() {
        try {
            productoDao = new ProductoDao(DatabaseManager.getConnectionSource());
            almacenDao = new AlmacenDao(DatabaseManager.getConnectionSource());

            colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
            colNombre.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));
            colCantidad.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());
            colDescripcion.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescripcion()));
            colAlmacen.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                    cell.getValue().getAlmacen() != null ? cell.getValue().getAlmacen().getNombre() : ""
            ));

            cargarProductos();
            cargarAlmacenesCombo();

            btnAgregar.setOnAction(e -> agregarProducto());
            btnEditar.setOnAction(e -> editarProducto());
            btnEliminar.setOnAction(e -> eliminarProducto());
            btnAlmacenes.setOnAction(e -> mostrarAlmacenes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Muestra los datos del producto seleccionado en los campos de edición
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombre.setText(newSel.getNombre());
                txtCantidad.setText(String.valueOf(newSel.getCantidad()));
                txtDescripcion.setText(newSel.getDescripcion());
                comboAlmacen.setValue(newSel.getAlmacen());
            }
        });
    }

    /**
     * Carga productos desde la base y llena la tabla.
     * @throws SQLException si hay errores de base de datos.
     */
    private void cargarProductos() throws SQLException {
        listaProductos = FXCollections.observableArrayList(productoDao.obtenerTodos());
        tablaProductos.setItems(listaProductos);
    }

    /**
     * Carga almacenes desde la base y los pone en el ComboBox.
     * @throws SQLException si hay errores de base de datos.
     */
    private void cargarAlmacenesCombo() throws SQLException {
        comboAlmacen.setItems(FXCollections.observableArrayList(almacenDao.obtenerTodos()));
    }

    /**
     * Acción al agregar un producto nuevo.
     * Valida campos, inserta, actualiza la tabla y limpia los campos.
     */
    @FXML
    public void agregarProducto() {
        try {
            String nombre = txtNombre.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String descripcion = txtDescripcion.getText();
            Almacen almacen = comboAlmacen.getValue();

            if (almacen == null) {
                mostrarAlerta("Debes seleccionar un almacén.");
                return;
            }
            System.out.println("DEBUG: Almacen seleccionado id=" + almacen.getId() + ", nombre=" + almacen.getNombre());
            productoDao.agregar(new Producto(nombre, cantidad, descripcion, almacen));
            cargarProductos();
            limpiarCampos();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error al agregar producto. Revise los campos.");
        }
    }

    /**
     * Acción al editar el producto seleccionado en la tabla.
     * Valida campos, guarda cambios en BD, actualiza tabla y limpia campos.
     */
    @FXML
    public void editarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un producto para editar.");
            return;
        }
        try {
            seleccionado.setNombre(txtNombre.getText());
            seleccionado.setCantidad(Integer.parseInt(txtCantidad.getText()));
            seleccionado.setDescripcion(txtDescripcion.getText());
            seleccionado.setAlmacen(comboAlmacen.getValue());
            productoDao.actualizar(seleccionado);
            cargarProductos();
            limpiarCampos();
        } catch (Exception ex) {
            ex.printStackTrace(); // Importante para visualizar detalles del error en consola
            mostrarAlerta("Error al editar producto.");
        }
    }

    /**
     * Acción al eliminar un producto seleccionado en la tabla.
     * Actualiza la tabla y limpia los campos tras eliminar.
     */
    @FXML
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un producto para eliminar.");
            return;
        }
        try {
            productoDao.eliminar(seleccionado);
            cargarProductos();
            limpiarCampos();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error al eliminar producto.");
        }
    }

    /**
     * Limpia los campos de entrada del formulario.
     * Utilizado luego de agregar, editar o eliminar productos.
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtCantidad.clear();
        txtDescripcion.clear();
        comboAlmacen.setValue(null);
    }

    /**
     * Muestra una alerta simple de advertencia con un mensaje.
     * @param mensaje Texto que se muestra al usuario.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra la ventana de gestión de almacenes.
     * Llama a la vista AlmacenView.fxml y al cerrarla recarga los almacenes en el ComboBox.
     */
    @FXML
    public void mostrarAlmacenes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/AlmacenView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gestión de Almacenes");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarAlmacenesCombo(); // Refresca el ComboBox tras posible edición de almacenes
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("No se pudo abrir la gestión de almacenes.");
        }
    }
}