package app;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación.
 */
public class MainApp extends Application {

    /**
     * Arranca la app y carga la vista principal.
     */
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseManager.initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/InventarioView.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/vista/estilo.css").toExternalForm());
        stage.setTitle("Inventario");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Al cerrar la aplicación, cierra la base.
     */
    @Override
    public void stop() throws Exception {
        DatabaseManager.close();
    }



    /**
     * Método main.
     */
    public static void main(String[] args) {
        launch(args);
    }
}