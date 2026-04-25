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

## Autor

Andrish Araiza

