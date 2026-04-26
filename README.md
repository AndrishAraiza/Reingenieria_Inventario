# Reingeniería de Inventario

Este proyecto es una aplicación JavaFX con ORMLite para gestión de inventarios, almacenes y productos.

## Requisitos

- Java 11 o superior
- Maven

## Estructura de Funcionalidad

- **Almacenes:** Crea, edita y elimina almacenes. Cada producto debe pertenecer a un almacén.
- **Productos:** CRUD de productos asociados a un almacén. La interfaz refleja automáticamente las asociaciones y cantidades.

## Cómo correr el proyecto

1. Clona el repositorio y entra a la carpeta:
    ```sh
    git clone https://github.com/AndrishAraiza/Reingenieria_Inventario.git
    cd Reingenieria_Inventario
    ```

2. Ejecuta este comando en la raíz del proyecto:
    ```sh
    mvn clean javafx:run
    ```

3. La aplicación abrirá una ventana JavaFX con el inventario.

#### Nota
- Si sale error sobre el archivo de base de datos, elimina `inventario.db` y reinicia.
- Agrega primero uno o varios **almacenes** desde el botón `Almacenes`, y después podrás agregar productos.
- Para poder editar `Almacenes` y `Productos` se debe de seleccionar el almacen o el producto que se desea editar y corregir en los campos correspondientes
 
## Generar documentación JavaDoc

Si quieres generar la documentación JavaDoc, ejecuta:
```sh
javadoc -d docs -sourcepath src/main/java -subpackages controlador:dao:database:modelo:app
```
El HTML generado quedará en la carpeta `docs/`.

## Estructura principal del proyecto

```
src/
  main/
    java/
      controlador/
      dao/
      database/
      modelo/
      app/
    resources/
      vista/
      estilo.css
  docs/
  inventario.db
README.md
pom.xml
```

Mejoras en la Interfaz de Usuario y la Navegación

    De Swing a JavaFX: Se reemplazó por completo la antigua y rígida interfaz gráfica de javax.swing por JavaFX.

    Separación de Diseño y Lógica: La vista ahora se diseña de manera declarativa en archivos .fxml (InventarioView.fxml, AlmacenView.fxml), lo que permite que los diseñadores modifiquen la interfaz sin tocar el código Java.

    Estilizado con CSS: Se implementó un archivo estilo.css global que aplica fondos en degradado, botones interactivos con efectos hover y sombras (drop-shadows) a las tablas, dándole un aspecto moderno y profesional.

    Navegación Intuitiva: En lugar de un CardLayout incrustado, la gestión de almacenes ahora se abre como una ventana modular limpia que, al cerrarse, refresca automáticamente los ComboBoxes de la vista principal.

Mejoras en el Manejo de Datos

    Implementación de ORMLite: Se eliminó la clase Database.java que contenía sentencias SQL crudas ("INSERT INTO...", "UPDATE..."). Ahora, la persistencia se maneja automáticamente mapeando los objetos Java a las tablas SQLite.

    Manejo de Relaciones: En la versión anterior, la relación entre Producto y Almacén era frágil (se guardaba un ID manual). Con ORMLite, se utiliza el atributo foreign = true, lo que permite traer el objeto Almacen completo de forma automática.

    Gestor de Conexiones: La clase DatabaseManager ahora centraliza la conexión (JdbcConnectionSource) e inicializa automáticamente las tablas si no existen (TableUtils.createTableIfNotExists), evitando errores de bases de datos faltantes.

Mejoras en los Modelos

    Encapsulamiento estricto: Los modelos anteriores (mx.unison.Producto) tenían atributos públicos (public String nombre;), lo cual rompe los principios de la Programación Orientada a Objetos. En la nueva versión, los atributos son privados y se accede a ellos exclusivamente a través de métodos Getters y Setters.

    Anotaciones: Se añadieron anotaciones de base de datos como @DatabaseTable(tableName = "productos") y @DatabaseField(generatedId = true) para automatizar la creación de esquemas.

Mejoras en los Controladores

    Adopción del patrón DAO: Toda la lógica de lectura/escritura hacia la base de datos se extrajo de las vistas y se colocó en clases dedicadas (ProductoDao, AlmacenDao).

    Enlace de datos (Data Binding): Gracias a JavaFX, las tablas ahora se alimentan de listas observables (ObservableList). Cuando se agrega un registro, la tabla se actualiza automáticamente.

    Clases DTO (Data Transfer Objects): En AlmacenController se implementó un AlmacenDTO que permite calcular de forma dinámica cuántos productos existen por almacén y mostrar ese número en la tabla, una función que no existía en la versión anterior.

Comparación de Seguridad

    Prevención de Inyección SQL: En la versión anterior se previno la inyección SQL utilizando PreparedStatement de JDBC. En la nueva versión, ORMLite se encarga de parametrizar todas las sentencias por debajo, manteniendo y garantizando la protección contra inyección SQL por defecto en todas las operaciones de CRUD.

    Nota de alcance: El módulo de Autenticación de Usuarios (MD5) de la versión anterior no fue portado a esta nueva versión, ya que el enfoque actual fue establecer la arquitectura MVC y ORMLite.

Comparación de Pruebas

    Bases de Datos en Memoria: La mejora más significativa en la suite de pruebas es que ProductoDaoTest ahora utiliza una base de datos SQLite en memoria (jdbc:sqlite::memory:). En la versión antigua, las pruebas escribían directamente sobre el archivo de la base de datos real (Inventario.db), lo que corrompía los datos del usuario.

    Pruebas de Unidad Puras: Se agregaron pruebas para verificar que el encapsulamiento de los Modelos (getters y setters) funcione correctamente (ProductoTest.java), mejorando la cobertura de código.
    
## Autor

Andrish Araiza

## 📖 Documentación Técnica
Puedes consultar el Javadoc interactivo aquí: 
[Ver Javadoc del Proyecto](https://AndrishAraiza.github.io/Reingenieria_Inventario/)
