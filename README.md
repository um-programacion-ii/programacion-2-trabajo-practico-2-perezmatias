[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/tc38IXJF)
# 📚 Trabajo Práctico: Sistema de Gestión de Biblioteca Digital (Java 21+)

## 📌 Objetivo General

Desarrollar un sistema de gestión de biblioteca digital que implemente los cinco principios SOLID, programación orientada a objetos, y conceptos avanzados de Java. El sistema deberá manejar diferentes tipos de recursos digitales, préstamos, reservas, y notificaciones en tiempo real.

## 👨‍🎓 Información del Alumno
- **Nombre y Apellido**: Matias Agustin Perez
- **Repositorio**: https://github.com/um-programacion-ii/programacion-2-trabajo-practico-2-perezmatias.git

## 📋 Requisitos Adicionales

### Documentación del Sistema
Como parte del trabajo práctico, deberás incluir en este README una guía de uso que explique:

1. **Cómo funciona el sistema**:
   - Descripción general de la arquitectura
   - Explicación de los componentes principales
   - Flujo de trabajo del sistema

2. **Cómo ponerlo en funcionamiento**:
   - Deberás incluir las instrucciones detalladas de puesta en marcha
   - Explicar los requisitos previos necesarios
   - Describir el proceso de compilación
   - Detallar cómo ejecutar la aplicación

3. **Cómo probar cada aspecto desarrollado**:
   - Deberás proporcionar ejemplos de uso para cada funcionalidad implementada
   - Incluir casos de prueba que demuestren el funcionamiento del sistema
   - Describir flujos de trabajo completos que muestren la interacción entre diferentes componentes

La guía debe ser clara, concisa y permitir a cualquier usuario entender y probar el sistema. Se valorará especialmente:
- La claridad de las instrucciones
- La completitud de la documentación
- La organización de la información
- La inclusión de ejemplos prácticos

### Prueba de Funcionalidades

#### 1. Gestión de Recursos
- **Agregar Libro**: 
  - Proceso para agregar un nuevo libro al sistema
  - Verificación de que el libro se agregó correctamente
  - Validación de los datos ingresados

- **Buscar Recurso**:
  - Proceso de búsqueda de recursos
  - Verificación de resultados de búsqueda
  - Manejo de casos donde no se encuentran resultados

- **Listar Recursos**:
  - Visualización de todos los recursos
  - Filtrado por diferentes criterios
  - Ordenamiento de resultados

#### 2. Gestión de Usuarios
- **Registrar Usuario**:
  - Proceso de registro de nuevos usuarios
  - Validación de datos del usuario
  - Verificación del registro exitoso

- **Buscar Usuario**:
  - Proceso de búsqueda de usuarios
  - Visualización de información del usuario
  - Manejo de usuarios no encontrados

#### 3. Préstamos
- **Realizar Préstamo**:
  - Proceso completo de préstamo
  - Verificación de disponibilidad
  - Actualización de estados

- **Devolver Recurso**:
  - Proceso de devolución
  - Actualización de estados
  - Liberación del recurso

#### 4. Reservas
- **Realizar Reserva**:
  - Proceso de reserva de recursos
  - Gestión de cola de reservas
  - Notificación de disponibilidad

#### 5. Reportes
- **Ver Reportes**:
  - Generación de diferentes tipos de reportes
  - Visualización de estadísticas
  - Exportación de datos

#### 6. Alertas
- **Verificar Alertas**:
  - Sistema de notificaciones
  - Diferentes tipos de alertas
  - Gestión de recordatorios

### Ejemplos de Prueba
1. **Flujo Completo de Préstamo**:
   - Registrar un usuario
   - Agregar un libro
   - Realizar un préstamo
   - Verificar el estado del recurso
   - Devolver el recurso
   - Verificar la actualización del estado

2. **Sistema de Reservas**:
   - Registrar dos usuarios
   - Agregar un libro
   - Realizar una reserva con cada usuario
   - Verificar la cola de reservas
   - Procesar las reservas

3. **Alertas y Notificaciones**:
   - Realizar un préstamo
   - Esperar a que se acerque la fecha de vencimiento
   - Verificar las alertas generadas
   - Probar la renovación del préstamo
   - 
## 📋 Documentacion del Sistema

Esta sección describe cómo funciona el sistema, cómo ponerlo en marcha y cómo probar sus funcionalidades.

### 1. Cómo funciona el sistema

#### Descripción general de la arquitectura

El sistema sigue una arquitectura en capas simple y aplica los principios SOLID:

* **Capa de Modelo (`com.biblioteca.modelo.*`):** Contiene las clases que representan las entidades del dominio (POJOs - Plain Old Java Objects):
    * `Usuario`: Representa a un usuario de la biblioteca.
    * `RecursoDigital` (Interfaz): Define el contrato base para todos los recursos.
    * `RecursoBase` (Clase Abstracta): Implementa lógica común para recursos (ID, título, estado, categoría, contador de préstamos).
    * `Libro`, `Revista`, `Audiolibro`: Clases concretas que extienden `RecursoBase`.
    * `Prestable`, `Renovable` (Interfaces): Definen capacidades específicas (ISP).
    * `Prestamo`: Representa un préstamo activo.
    * `Reserva`: Representa una reserva realizada (aunque la lógica principal usa colas de Usuarios).
    * `CategoriaRecurso` (Enum): Define las categorías de los recursos.
    * `EstadoRecurso` (Enum): Define los estados de un recurso.
* **Capa de Servicio/Gestión (`com.biblioteca.servicio.*`):** Contiene la lógica de negocio y la gestión de las colecciones de objetos del modelo. Cada gestor tiene una única responsabilidad (SRP).
    * `GestorUsuarios`: Maneja la colección de `Usuario` (añadir, buscar, listar).
    * `GestorRecursos`: Maneja la colección de `RecursoDigital` (añadir, buscar, listar, filtrar, ordenar). Usa `Comparator`s personalizados (`servicio.comparadores`).
    * `GestorPrestamos`: Orquesta la lógica de realizar préstamos y registrar devoluciones, interactuando con los recursos y las reservas.
    * `GestorReservas`: Gestiona las colas (`BlockingQueue`) de espera para cada recurso reservado.
    * `ServicioNotificaciones` (Interfaz): Abstracción para enviar notificaciones (DIP).
    * `ServicioNotificacionesConsola`: Implementación concreta que muestra notificaciones en consola usando un `ExecutorService` para envío asíncrono.
    * `MonitorVencimientos`: Revisa préstamos activos y genera alertas por vencimiento.
    * `GeneradorReportes`: Calcula y genera los reportes estadísticos. Usa un `ExecutorService` para ejecución asíncrona.
* **Capa de Excepciones (`com.biblioteca.excepciones`):** Contiene las excepciones personalizadas (`RecursoDuplicadoException`, `UsuarioDuplicadoException`, `OperacionNoPermitidaException`).
* **Capa de UI (`com.biblioteca.ui`):**
    * `Consola`: Implementa la interfaz de usuario basada en texto. Interactúa con el usuario, recibe entradas y llama a los métodos de los gestores. Muestra resultados y maneja excepciones para mensajes amigables.
* **Punto de Entrada (`com.biblioteca.app`):**
    * `BibliotecaApp`: Contiene el método `main`. Es responsable de crear e inyectar las dependencias (Gestores, Servicios, Executors) en la `Consola` y de iniciar/finalizar la aplicación limpiamente (incluyendo el apagado de los `ExecutorService`).

**Principios SOLID Aplicados:**

* **SRP:** Cada Gestor (`GestorUsuarios`, `GestorRecursos`, etc.), `Consola`, `MonitorVencimientos`, `GeneradorReportes` tiene una única responsabilidad. Las clases del modelo solo representan datos.
* **OCP:** La interfaz `RecursoDigital` y la clase `RecursoBase` permiten añadir nuevos tipos de recursos (ej. `MapaDigital`) sin modificar el código existente. Los gestores trabajan con la interfaz `RecursoDigital`.
* **LSP:** Las subclases `Libro`, `Revista`, `Audiolibro` pueden ser usadas donde se espere `RecursoDigital` (demostrado en `GestorRecursos` y `Consola`).
* **ISP:** Las interfaces `Prestable` y `Renovable` segregan funcionalidades específicas, aplicadas solo a `Libro` y `Audiolibro`. `Revista` no implementa estas interfaces.
* **DIP:** Los módulos de alto nivel dependen de abstracciones. Ej: `Consola` depende de interfaces (implícito para Gestores) y `ServicioNotificaciones`. `GestorPrestamos` depende de `GestorReservas` y `ServicioNotificaciones`. `BibliotecaApp` inyecta las dependencias concretas. `ServicioNotificacionesConsola` depende de `ExecutorService`.

#### Componentes Principales

* **`Consola`**: Punto de interacción principal con el usuario vía menús de texto.
* **`GestorUsuarios`**: Almacena usuarios en un `Map<String, Usuario>`. Permite agregar, buscar (ID, nombre/email), listar.
* **`GestorRecursos`**: Almacena recursos en un `Map<String, RecursoDigital>`. Permite agregar, buscar (ID, título), listar (con ordenamiento opcional por Título/ID), filtrar (por Tipo, Categoría). Usa Streams y Comparators.
* **`GestorPrestamos`**: Almacena préstamos activos en `Map<String, Prestamo>`. Contiene la lógica para `realizarPrestamo` (validaciones, marcar recurso, crear registro) y `registrarDevolucion` (validaciones, marcar recurso, procesar cola de reservas, eliminar registro).
* **`GestorReservas`**: Almacena colas de espera en `Map<String, BlockingQueue<Usuario>>`. Permite `realizarReserva` (añadir usuario a cola) y `obtenerSiguienteUsuarioEnCola` (usado por `GestorPrestamos`).
* **`MonitorVencimientos`**: Escanea préstamos activos y usa `ServicioNotificaciones` para enviar alertas de vencimiento.
* **`GeneradorReportes`**: Calcula estadísticas (Top N, por Categoría) leyendo datos de los gestores.
* **`ServicioNotificacionesConsola`**: Implementación que imprime notificaciones en consola de forma asíncrona usando un `ExecutorService`.
* **Modelos (`Usuario`, `Libro`, etc.)**: Clases POJO que contienen los datos y lógica específica (ej. implementación de `Prestable`/`Renovable`).

#### Flujo de trabajo del sistema (Ejemplo: Préstamo)

1.  Usuario elige "Gestionar Recursos" -> "Prestar Recurso" en `Consola`.
2.  `Consola` pide ID de recurso, ID de usuario y días de préstamo.
3.  `Consola` busca el `Usuario` y `RecursoDigital` usando `GestorUsuarios` y `GestorRecursos`.
4.  Si ambos existen, `Consola` llama a `gestorPrestamos.realizarPrestamo(usuario, recurso, dias)`.
5.  `GestorPrestamos.realizarPrestamo`:
    * Valida entradas.
    * Verifica si el recurso es `Prestable` y está disponible. Lanza `OperacionNoPermitidaException` si no.
    * Calcula fecha devolución.
    * Llama a `recurso.marcarComoPrestado(...)` (esto cambia el estado del recurso y guarda datos del préstamo en el recurso).
    * Crea un objeto `Prestamo`.
    * Añade el `Prestamo` a su mapa de préstamos activos.
    * Incrementa contadores en `Usuario` y `RecursoDigital`.
    * Devuelve el objeto `Prestamo`.
6.  Si `realizarPrestamo` no lanza excepción, `Consola` muestra mensaje de éxito.
7.  `Consola` llama a `servicioNotificaciones.enviarNotificacion(...)`.
8.  `ServicioNotificacionesConsola` recibe la llamada y usa su `ExecutorService` para ejecutar la impresión de la notificación en otro hilo.
9.  La `Consola` vuelve a mostrar el menú.

### 2. Cómo ponerlo en funcionamiento

#### Requisitos Previos

* **Java Development Kit (JDK):** Versión 21 o superior (LTS recomendada). Asegúrate de que esté instalado y configurado en el PATH del sistema.
* **Git (Opcional):** Necesario si clonas el repositorio desde GitHub.
* **IDE (Recomendado):** Un Entorno de Desarrollo Integrado como IntelliJ IDEA, Eclipse IDE for Java Developers, o Visual Studio Code con el "Extension Pack for Java" facilitará la compilación y ejecución.

#### Puesta en Marcha (Instrucciones Detalladas)

1.  **Clonar el Repositorio (si aplica):**
    ```bash
    git clone https://github.com/um-programacion-ii/programacion-2-trabajo-practico-2-perezmatias.git
    cd programacion-2-trabajo-practico-2-perezmatias
    ```
2.  **Abrir en IDE:**
    * Abre tu IDE preferido.
    * Selecciona la opción "Open Project" o "Import Project".
    * Navega hasta la carpeta donde clonaste o descomprimiste el proyecto y selecciónala.
    * El IDE debería reconocer la estructura del proyecto Java (`src/main/java`). Asegúrate de que el JDK configurado para el proyecto sea Java 21+. (Verifica en `File > Project Structure` en IntelliJ o `Project > Properties > Java Build Path` en Eclipse).

#### Proceso de Compilación

* **Usando IDE:** La mayoría de los IDEs compilan el código automáticamente al guardar los archivos o al ejecutar la aplicación. Si necesitas forzar una compilación/reconstrucción, busca opciones como `Build > Rebuild Project` (IntelliJ) o `Project > Clean...` (Eclipse).
* **Manualmente (Línea de Comandos):**
    1.  Navega a la carpeta raíz del proyecto en tu terminal.
    2.  Crea un directorio para las clases compiladas (ej. `bin`): `mkdir bin`
    3.  Compila todos los archivos `.java` indicando el directorio de salida (`-d`), el classpath (`-cp`, aunque aquí no hay externos) y la ruta a los fuentes. Puede ser complejo por las dependencias entre clases, un comando simplificado sería (puede necesitar ajustes):
        ```bash
        javac -d bin -cp src/main/java $(find src/main/java -name *.java)
        # O compilar específicamente la clase principal y sus dependencias:
        # javac -d bin -cp src/main/java src/main/java/com/biblioteca/app/BibliotecaApp.java
        ```
  *Nota: Es mucho más sencillo usar un IDE.*

#### Ejecución de la Aplicación

* **Usando IDE:**
    1.  Localiza el archivo `src/main/java/com/biblioteca/app/BibliotecaApp.java`.
    2.  Haz clic derecho dentro del editor de ese archivo o sobre el archivo en el explorador de proyectos.
    3.  Selecciona "Run 'BibliotecaApp.main()'" (IntelliJ) o "Run As > Java Application" (Eclipse) o haz clic en el enlace "Run" sobre el método `main` (VS Code).
    4.  La aplicación se ejecutará y verás el menú principal en la consola integrada del IDE.
* **Manualmente (Línea de Comandos):**
    1.  Asegúrate de haber compilado las clases en el directorio `bin` (ver paso anterior).
    2.  Desde la carpeta raíz del proyecto, ejecuta:
        ```bash
        java -cp bin com.biblioteca.app.BibliotecaApp
        ```
    3.  La aplicación se ejecutará en tu terminal.

### 3. Cómo probar cada aspecto desarrollado

Interactúa con la aplicación a través de las opciones numéricas de los menús que aparecen en la consola. A continuación se detallan ejemplos para cada funcionalidad principal.

*(Nota: Los IDs de Usuario y Recurso se generan automáticamente con UUID y son largos. Anótalos cuando los crees para usarlos en pasos posteriores).*

#### Gestión de Recursos

* **Agregar Recurso (Libro):**
    1.  Menú Principal -> Opción 2 (Gestionar Recursos).
    2.  Opción 1 (Agregar Libro).
    3.  Ingresa Título, Autor, ISBN cuando se soliciten.
    4.  Selecciona un número de la lista de Categorías mostrada.
    5.  Verifica el mensaje "Libro agregado con éxito (ID: ...)".
        *(Repite similarmente para Revista [Opción 2] y Audiolibro [Opción 3]).*
* **Listar Recursos (Ordenado):**
    1.  Menú Recursos -> Opción 4 (Listar Todos).
    2.  Elige opción de ordenamiento (1: Título, 2: ID, 0: Sin Ordenar).
    3.  Verifica que la lista resultante aparece ordenada según el criterio elegido (o sin orden específico si elegiste 0). Los detalles de cada recurso (incluyendo estado, categoría, info de préstamo si aplica) se muestran por su método `toString()`.
* **Buscar Recurso por ID:**
    1.  Menú Recursos -> Opción 5 (Buscar por ID).
    2.  Ingresa un ID de recurso existente.
    3.  Verifica que se muestran los detalles completos de ese recurso.
    4.  Intenta buscar con un ID inválido. Verifica el mensaje "No se encontró...".
* **Buscar Recursos por Título:**
    1.  Menú Recursos -> Opción 6 (Buscar por Título).
    2.  Ingresa una parte del título de un recurso existente (ej. "Libro").
    3.  Verifica que se listan todos los recursos cuyo título contiene ese texto (ignorando mayúsculas/minúsculas).
    4.  Intenta buscar con un texto que no exista en ningún título. Verifica el mensaje "No se encontraron...".
* **Filtrar Recursos por Tipo:**
    1.  Menú Recursos -> Opción 7 (Filtrar por Tipo).
    2.  Elige un tipo (1: Libro, 2: Revista, 3: Audiolibro).
    3.  Verifica que se listan *únicamente* los recursos del tipo seleccionado.
* **Listar Recursos por Categoría:**
    1.  Menú Recursos -> Opción 8 (Listar por Categoría).
    2.  Selecciona un número de la lista de categorías.
    3.  Verifica que se listan *únicamente* los recursos de la categoría seleccionada.
* **Mostrar Categorías:**
    1.  Menú Recursos -> Opción 9 (Mostrar Categorías).
    2.  Verifica que se lista el contenido del enum `CategoriaRecurso`.

#### Gestión de Usuarios

* **Agregar Usuario:**
    1.  Menú Principal -> Opción 1 (Gestionar Usuarios).
    2.  Opción 1 (Agregar Usuario).
    3.  Ingresa Nombre y Email.
    4.  Verifica el mensaje de éxito y anota el ID generado.
* **Listar Usuarios:**
    1.  Menú Usuarios -> Opción 2 (Listar).
    2.  Verifica que se listan todos los usuarios agregados con sus detalles.
* **Buscar por Nombre/Email:**
    1.  Menú Usuarios -> Opción 3 (Buscar por Nombre/Email).
    2.  Ingresa parte del nombre o email de un usuario existente.
    3.  Verifica que se listan los usuarios coincidentes.

#### Préstamos

* **Realizar Préstamo:**
    1.  Menú Recursos -> Opción 10 (Prestar).
    2.  Ingresa ID de un recurso *prestable* (Libro/Audiolibro) que esté *DISPONIBLE*.
    3.  Ingresa ID de un usuario existente.
    4.  Ingresa número de días (ej. 14).
    5.  Verifica mensaje de éxito y la notificación asíncrona de préstamo.
    6.  Intenta prestar un recurso NO prestable (Revista). Verifica el error "no es del tipo prestable".
    7.  Intenta prestar un recurso ya prestado. Verifica el error "no está disponible para préstamo".
* **Devolver Recurso:**
    1.  Menú Recursos -> Opción 11 (Devolver).
    2.  Ingresa el ID de un recurso que esté *PRESTADO*.
    3.  Verifica mensaje de éxito y notificación de devolución. El recurso debería pasar a DISPONIBLE o RESERVADO (ver Reservas).
    4.  Intenta devolver un recurso que *no* esté prestado. Verifica el error "No se encontró un préstamo activo...".
* **Renovar Préstamo:**
    1.  Menú Recursos -> Opción 12 (Renovar).
    2.  Ingresa el ID de un recurso *prestable* y *renovable* que esté *PRESTADO*.
    3.  Verifica mensaje de éxito, notificación de renovación y que el contador de renovaciones aumenta.
    4.  Intenta renovar un recurso no prestado o no renovable. Verifica los mensajes de error correspondientes.

#### Reservas

* **Realizar Reserva:**
    1.  Asegúrate de tener un recurso prestado (ej. Libro L1 prestado a U1).
    2.  Menú Recursos -> Opción 13 (Reservar).
    3.  Ingresa ID del recurso L1 e ID de otro usuario (U2).
    4.  Verifica mensaje de éxito y notificación de reserva.
    5.  Intenta reservar un recurso DISPONIBLE. Verifica el error "...no se puede reservar porque su estado actual es DISPONIBLE...".
    6.  Intenta que U2 reserve L1 de nuevo. Verifica el error "...ya tiene una reserva activa...".
* **Ver Estado de Reservas:**
    1.  Menú Recursos -> Opción 14 (Ver Estado).
    2.  Verifica que se lista el recurso L1 y el usuario U2 (y otros si los añadiste) en la cola de espera.
* **Procesar Cola al Devolver:**
    1.  Devuelve el recurso L1 (que tenía a U2 esperando).
    2.  Busca la notificación "RESERVA_DISPONIBLE" para U2.
    3.  Verifica el estado de L1 (Listar Recursos), debe ser RESERVADO.
    4.  Verifica el estado de reservas (Opción 14), la cola para L1 debe estar vacía ahora.

#### Alertas

* **Verificar Vencimientos:**
    1.  Asegúrate de tener algunos préstamos activos con diferentes fechas de devolución (algunas pasadas, hoy, mañana, futuro lejano - puede requerir modificar temporalmente los días de préstamo en `Consola.prestarRecurso`).
    2.  Menú Principal -> Opción 3 (Verificar Préstamos Vencidos/Por Vencer).
    3.  Observa la consola. Deberían aparecer (de forma asíncrona) las notificaciones correspondientes ("RECORDATORIO_VENCIMIENTO", "AVISO_VENCIMIENTO_HOY", "PRESTAMO_VENCIDO") para los préstamos que cumplan la condición. Verifica el mensaje "Verificación completada".

#### Reportes

* **Generar Reportes:**
    1.  Realiza varios préstamos a diferentes usuarios y de diferentes recursos/categorías.
    2.  Menú Principal -> Opción 4 (Reportes y Análisis).
    3.  Selecciona Opción 1 (Top 5 Recursos). Verifica mensaje "Generando..." y luego la lista ordenada por número de préstamos.
    4.  Selecciona Opción 2 (Top 5 Usuarios). Verifica mensaje "Generando..." y luego la lista ordenada por préstamos realizados.
    5.  Selecciona Opción 3 (Estadísticas por Categoría). Verifica mensaje "Generando..." y luego el recuento de recursos por categoría.

#### Flujos Completos de Ejemplo

* **Flujo Completo Préstamo-Devolución:**
    1.  Agregar Usuario U1.
    2.  Agregar Libro B1.
    3.  Listar recursos (ver B1 DISPONIBLE).
    4.  Prestar B1 a U1 (10 días). (Ver notificación).
    5.  Listar recursos (ver B1 PRESTADO a U1, fecha X).
    6.  Devolver B1. (Ver notificación).
    7.  Listar recursos (ver B1 DISPONIBLE).
* **Flujo Completo Reserva:**
    1.  Agregar Usuarios U1, U2.
    2.  Agregar Audiolibro A1.
    3.  Prestar A1 a U1 (5 días).
    4.  Reservar A1 por U2.
    5.  Ver estado reservas (ver A1 -> U2).
    6.  Devolver A1. (Ver notificación a U2).
    7.  Listar recursos (ver A1 RESERVADO).
    8.  Ver estado de reservas (ver cola de A1 vacía).

## 🧩 Tecnologías y Herramientas

- Java 21+ (LTS)
- Git y GitHub
- GitHub Projects
- GitHub Issues
- GitHub Pull Requests

## 📘 Etapas del Trabajo

### Etapa 1: Diseño Base y Principios SOLID
- **SRP**: 
  - Crear clase `Usuario` con atributos básicos (nombre, ID, email)
  - Crear clase `RecursoDigital` como clase base abstracta
  - Implementar clase `GestorUsuarios` separada de `GestorRecursos`
  - Cada clase debe tener una única responsabilidad clara
  - Implementar clase `Consola` para manejar la interacción con el usuario

- **OCP**: 
  - Diseñar interfaz `RecursoDigital` con métodos comunes
  - Implementar clases concretas `Libro`, `Revista`, `Audiolibro`
  - Usar herencia para extender funcionalidad sin modificar código existente
  - Ejemplo: agregar nuevo tipo de recurso sin cambiar clases existentes
  - Implementar menú de consola extensible para nuevos tipos de recursos

- **LSP**: 
  - Asegurar que todas las subclases de `RecursoDigital` puedan usarse donde se espera `RecursoDigital`
  - Implementar métodos comunes en la clase base
  - Validar que el comportamiento sea consistente en todas las subclases
  - Crear métodos de visualización en consola para todos los tipos de recursos

- **ISP**: 
  - Crear interfaz `Prestable` para recursos que se pueden prestar
  - Crear interfaz `Renovable` para recursos que permiten renovación
  - Implementar solo las interfaces necesarias en cada clase
  - Diseñar menús de consola específicos para cada tipo de operación

- **DIP**: 
  - Crear interfaz `ServicioNotificaciones`
  - Implementar `ServicioNotificacionesEmail` y `ServicioNotificacionesSMS`
  - Usar inyección de dependencias en las clases que necesitan notificaciones
  - Implementar visualización de notificaciones en consola

### Etapa 2: Gestión de Recursos y Colecciones
- Implementar colecciones:
  - Usar `ArrayList<RecursoDigital>` para almacenar recursos
  - Usar `Map<String, Usuario>` para gestionar usuarios
  - Implementar métodos de búsqueda básicos
  - Crear menú de consola para gestión de recursos

- Crear servicios de búsqueda:
  - Implementar búsqueda por título usando Streams
  - Implementar filtrado por categoría
  - Crear comparadores personalizados para ordenamiento
  - Diseñar interfaz de consola para búsquedas con filtros

- Sistema de categorización:
  - Crear enum `CategoriaRecurso`
  - Implementar método de asignación de categorías
  - Crear búsqueda por categoría
  - Mostrar categorías disponibles en consola

- Manejo de excepciones:
  - Crear `RecursoNoDisponibleException`
  - Crear `UsuarioNoEncontradoException`
  - Implementar manejo adecuado de excepciones en los servicios
  - Mostrar mensajes de error amigables en consola

### Etapa 3: Sistema de Préstamos y Reservas
- Implementar sistema de préstamos:
  - Crear clase `Prestamo` con atributos básicos
  - Implementar lógica de préstamo y devolución
  - Manejar estados de los recursos (disponible, prestado, reservado)
  - Diseñar menú de consola para préstamos

- Sistema de reservas:
  - Crear clase `Reserva` con atributos necesarios
  - Implementar cola de reservas usando `BlockingQueue`
  - Manejar prioridad de reservas
  - Mostrar estado de reservas en consola

- Notificaciones:
  - Implementar sistema básico de notificaciones
  - Crear diferentes tipos de notificaciones
  - Usar `ExecutorService` para enviar notificaciones
  - Mostrar notificaciones en consola

- Concurrencia:
  - Implementar sincronización en operaciones de préstamo
  - Usar `synchronized` donde sea necesario
  - Manejar condiciones de carrera
  - Mostrar estado de operaciones concurrentes en consola

### Etapa 4: Reportes y Análisis
- Generar reportes básicos:
  - Implementar reporte de recursos más prestados
  - Crear reporte de usuarios más activos
  - Generar estadísticas de uso por categoría
  - Diseñar visualización de reportes en consola

- Sistema de alertas:
  - Implementar alertas por vencimiento de préstamos:
    - Crear clase `AlertaVencimiento` que monitorea fechas de devolución
    - Implementar lógica de recordatorios (1 día antes, día del vencimiento)
    - Mostrar alertas en consola con formato destacado
    - Permitir renovación desde la alerta
  
  - Crear notificaciones de disponibilidad:
    - Implementar `AlertaDisponibilidad` para recursos reservados
    - Notificar cuando un recurso reservado está disponible
    - Mostrar lista de recursos disponibles en consola
    - Permitir préstamo inmediato desde la notificación
  
  - Manejar recordatorios automáticos:
    - Implementar sistema de recordatorios periódicos
    - Crear diferentes niveles de urgencia (info, warning, error)
    - Mostrar historial de alertas en consola
    - Permitir configuración de preferencias de notificación

- Concurrencia en reportes:
  - Implementar generación de reportes en segundo plano
  - Usar `ExecutorService` para tareas asíncronas
  - Manejar concurrencia en acceso a datos
  - Mostrar progreso de generación de reportes en consola

## 📋 Detalle de Implementación

### 1. Estructura Base
```java
// Interfaces principales
public interface RecursoDigital {
    String getIdentificador();
    EstadoRecurso getEstado();
    void actualizarEstado(EstadoRecurso estado);
}

public interface Prestable {
    boolean estaDisponible();
    LocalDateTime getFechaDevolucion();
    void prestar(Usuario usuario);
}

public interface Notificable {
    void enviarNotificacion(String mensaje);
    List<Notificacion> getNotificacionesPendientes();
}

// Clase base abstracta
public abstract class RecursoBase implements RecursoDigital, Prestable {
    // Implementación común
}
```

### 2. Gestión de Biblioteca
```java
public class GestorBiblioteca {
    private final Map<String, RecursoDigital> recursos;
    private final List<Prestamo> prestamos;
    private final ExecutorService notificador;
    // Implementación de gestión
}
```

### 3. Sistema de Préstamos
```java
public class SistemaPrestamos {
    private final BlockingQueue<SolicitudPrestamo> colaSolicitudes;
    private final ExecutorService procesadorPrestamos;
    // Implementación de préstamos
}
```

## ✅ Entrega y Flujo de Trabajo con GitHub

1. **Configuración del Repositorio**
   - Proteger la rama `main`
   - Crear template de Issues y Pull Requests

2. **Project Kanban**
   - `To Do`
   - `In Progress`
   - `Code Review`
   - `Done`

3. **Milestones**
   - Etapa 1: Diseño Base
   - Etapa 2: Gestión de Recursos
   - Etapa 3: Sistema de Préstamos
   - Etapa 4: Reportes

4. **Issues y Pull Requests**
   - Crear Issues detallados para cada funcionalidad
   - Asociar cada Issue a un Milestone
   - Implementar en ramas feature
   - Revisar código antes de merge

## 📝 Ejemplo de Issue

### Título
Implementar sistema de préstamos concurrente

### Descripción
Crear el sistema de préstamos que utilice hilos y el patrón productor-consumidor para procesar solicitudes de préstamo en tiempo real.

#### Requisitos
- Implementar `BlockingQueue` para solicitudes de préstamo
- Crear procesador de solicitudes usando `ExecutorService`
- Implementar sistema de notificaciones
- Asegurar thread-safety en operaciones de préstamo

#### Criterios de Aceptación
- [ ] Sistema procesa préstamos concurrentemente
- [ ] Manejo adecuado de excepciones
- [ ] Documentación de diseño

### Labels
- `enhancement`
- `concurrency`

## ✅ Requisitos para la Entrega

- ✅ Implementación completa de todas las etapas
- ✅ Código bien documentado
- ✅ Todos los Issues cerrados
- ✅ Todos los Milestones completados
- ✅ Pull Requests revisados y aprobados
- ✅ Project actualizado

> ⏰ **Fecha de vencimiento**: 23/04/2025 a las 13:00 hs

## 📚 Recursos Adicionales

- Documentación oficial de Java 21
- Guías de estilo de código
- Ejemplos de implementación concurrente
- Patrones de diseño aplicados

## 📝 Consideraciones Éticas

## Uso de Asistencia de IA (Google Gemini)

Siguiendo las pautas de integridad académica y transparencia establecidas para este trabajo práctico, se declara el uso de la herramienta de inteligencia artificial Google Gemini como asistente durante el desarrollo.

La asistencia de IA se utilizó específicamente en las siguientes áreas:

* **Resolución de Errores:** Ayuda en la identificación y corrección de errores de compilación y runtime encontrados durante la codificación.
* **Estructuración de Tareas:** Sugerencias para organizar y describir los Issues de GitHub correspondientes a cada etapa del desarrollo.
* **Estructura del Proyecto:** Recomendaciones sobre la adopción de la estructura estándar de paquetes y carpetas para proyectos Java (`src/main/java`, etc.).
* **Guía y Planificación:** Asistencia en la interpretación inicial de los requisitos y en la planificación del desarrollo de las funcionalidades solicitadas.

**Autoría del Código:**

Es importante destacar que **el codigo presentado y logica utilizada son de autoria propia**. La IA funcionó como una herramienta de apoyo para superar bloqueos (errores), organizar el trabajo y obtener guía sobre convenciones estándar.

### Uso de Inteligencia Artificial
El uso de herramientas de IA en este trabajo práctico debe seguir las siguientes pautas:

1. **Transparencia**
   - Documentar claramente qué partes del código fueron generadas con IA
   - Explicar las modificaciones realizadas al código generado
   - Mantener un registro de las herramientas utilizadas

2. **Aprendizaje**
   - La IA debe usarse como herramienta de aprendizaje, no como reemplazo
   - Comprender y ser capaz de explicar el código generado
   - Utilizar la IA para mejorar la comprensión de conceptos

3. **Integridad Académica**
   - El trabajo final debe reflejar tu aprendizaje y comprensión personal
   - No se permite la presentación de código generado sin comprensión
   - Debes poder explicar y defender cualquier parte del código

4. **Responsabilidad**
   - Verificar la corrección y seguridad del código generado
   - Asegurar que el código cumple con los requisitos del proyecto
   - Mantener la calidad y estándares de código establecidos

5. **Desarrollo Individual**
   - La IA puede usarse para facilitar tu proceso de aprendizaje
   - Documentar tu proceso de desarrollo y decisiones tomadas
   - Mantener un registro de tu progreso y aprendizaje

### Consecuencias del Uso Inadecuado
El uso inadecuado de IA puede resultar en:
- Calificación reducida o nula
- Sanciones académicas
- Pérdida de oportunidades de aprendizaje
- Impacto negativo en tu desarrollo profesional

## 📝 Licencia

Este trabajo es parte del curso de Programación Avanzada de Ingeniería en Informática. Uso educativo únicamente.