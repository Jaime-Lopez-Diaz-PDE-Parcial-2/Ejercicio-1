# Ejercicio 1

Jaime López Díaz

Links:
- Ejercicio 1: https://github.com/Jaime-Lopez-Diaz-PDE-Parcial-2/Ejercicio-1.git
- Ejercicio 2: https://github.com/Jaime-Lopez-Diaz-PDE-Parcial-2/Ejercicio_2.git
- Ejercicio 3: https://github.com/Jaime-Lopez-Diaz-PDE-Parcial-2/Ejercicio_3_Map.git


## Descripción

Este ejercicio en Android Studio Java implementa un sistema de calendario interactivo y visualizacion asigaturas diarias, además de un control de autentificación de usuarios mediante firebase (fireauth).
Permite a los usuarios visualizar un calendario, agregar actividades, ver las actividades programadas para un día específico, y la gestión roles de usuario como "Administrador" y "Usuario".

El proyecto está basado en una arquitectura modular utilizando Android Studio, Firebase Firestore para almacenamiento en la nube, y Room Database para almacenamiento local.

## Funcionalidades Principales

1. **Registro e inicio de sesión**:
   - Registro de usuarios con diferentes roles (administrador o usuario).
   - Inicio de sesión validado mediante Firebase Authentication y credenciales en Firestore.

2. **Calendario interactivo**:
   - Visualización de días del mes con actividades resaltadas.
   - Navegación entre meses con botones de "Anterior" y "Siguiente".
   - Detalle de actividades al seleccionar un día.

3. **Gestión de actividades**:
   - Los administradores pueden agregar actividades con fecha, hora y asignatura.
   - Visualización de actividades programadas para la fecha actual.

4. **Gestión de usuarios y ubicación**:
   - Los usuarios pueden actualizar su ubicación, la cual se guarda en Firebase Firestore.

## Clases Principales

### 1. PantallaPrincipalActivity

Gestiona el menú principal y la navegación entre actividades. Incluye:
- Configuración de un `DrawerLayout` para el menú lateral.
- Autenticación y cierre de sesión mediante FirebaseAuth.
- Permisos para ubicación y actualización de la ubicación del usuario.

### 2. CalendarActivity

Implementa el calendario interactivo:
- Utiliza un `GridLayout` para mostrar los días del mes.
- Mapea actividades a días mediante consultas en la base de datos de Room.
- Botones para cambiar entre meses.
- Celdas del calendario personalizadas, resaltadas si hay actividades.

### 3. AddActivity

Formulario para agregar nuevas actividades:
- Valida la entrada de datos en formato `YYYY-MM-DD` para fechas y `HH:MM` para horas.
- Guarda las actividades en la base de datos local de Room.

### 4. CurrentTasksActivity

Muestra las tareas programadas para el día actual:
- Consulta la base de datos de Room para filtrar actividades por la fecha actual.
- Actualiza dinámicamente la interfaz de usuario.

### 5. LoginActivity y RegisterActivity

Gestionan el flujo de autenticación:
- `LoginActivity` valida credenciales almacenadas en Firestore.
- `RegisterActivity` crea nuevos usuarios y los almacena en Firestore.

## Bases de Datos Utilizadas

### Room Database
- Local para almacenamiento de actividades.
- Entidades:
  - `Actividad`: Contiene campos `id`, `fecha`, `hora`, `asignatura`.

### Firebase Firestore
- Almacenamiento en la nube para sincronización de datos.
- Colecciones:
  - `usuarios`: Contiene información de los usuarios (ID, nombre, rol, ubicación).
  - `credenciales`: Almacena contraseñas cifradas.
  - `ubicaciones`: Ubicaciones actuales de los usuarios.

## Estructura del Calendario

### Diseño en XML

El diseño del calendario utiliza un `LinearLayout` con tres secciones principales:

1. **Encabezado**:
   - Botones para navegar entre meses (`btnPrevMonth`, `btnNextMonth`).
   - Texto para mostrar el mes y año actual (`tvCurrentMonth`).

2. **Días de la semana**:
   - Un `GridLayout` con 7 columnas para los días de la semana.

3. **Cuerpo del calendario**:
   - Otro `GridLayout` dinámico (`gridCalendar`) que muestra los días del mes.
   - Cada celda se genera en tiempo de ejecución y se resalta si tiene actividades.

### Lógica en CalendarActivity

1. **Carga de actividades**:
   - Se consultan todas las actividades de Room Database.
   - Las actividades se mapean a días específicos.

2. **Renderizado del calendario**:
   - Se generan celdas vacías para ajustar la primera semana del mes.
   - Cada día se representa como una celda interactiva.
   - Si hay actividades, la celda se resalta con un color específico.

3. **Interactividad**:
   - Al tocar una celda con actividad, se muestra un mensaje emergente (`Toast`) con los detalles de la actividad.
