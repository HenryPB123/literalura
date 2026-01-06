# 📚 LiterAlura

Proyecto backend desarrollado en **Java con Spring Boot** como parte del desafío **LiterAlura – Alura ONE**.

La aplicación consume datos desde una API pública de libros, los procesa y permite almacenarlos en una base de datos relacional, ofreciendo distintas funcionalidades de consulta mediante un menú por consola.

---

## 🚀 Tecnologías utilizadas

* **Java 17**
* **Spring Boot**
* **Spring Data JPA / Hibernate**
* **PostgreSQL**
* **Maven**
* **API pública de libros (Gutendex)**

---

## 🧠 Arquitectura general

El proyecto sigue una arquitectura en capas:

* **model** → Entidades JPA (`Libro`, `Autor`)
* **repository** → Interfaces `JpaRepository`
* **dto** → Clases para mapear respuestas de la API
* **service / util** → Lógica de negocio y ejecución del menú
* **principal** → Punto de entrada de la aplicación

---

## 📖 Modelo de datos

### 📘 Libro

* `id`
* `titulo`
* `idiomas` (ARRAY en PostgreSQL)
* `totalDescargas`
* Relación **ManyToMany** con `Autor`

### ✍️ Autor

* `id`
* `nombreAutor` (único)
* `fechaNacimiento`
* `fechaFallecimiento`
* Relación **ManyToMany** con `Libro`

---

## ⚙️ Funcionalidades implementadas

La aplicación se ejecuta por consola y ofrece las siguientes opciones:

1️⃣ Buscar libro por nombre y guardarlo en la base de datos (previa confirmación del usuario)

2️⃣ Listar todos los libros almacenados

3️⃣ Listar todos los autores registrados

4️⃣ Listar autores vivos en un año determinado

5️⃣ Listar libros por idioma

6️⃣ Evitar registros duplicados (libros y autores)

0️⃣ Salir

---

## 🧩 Detalles importantes de implementación

### ✔️ Control de duplicados

* No se permiten libros duplicados por título
* No se permiten autores duplicados por nombre

Antes de guardar, se valida si el registro ya existe en la base de datos.

---

### ✔️ Confirmación antes de guardar

Los datos obtenidos desde la API **no se guardan automáticamente**.

El usuario debe confirmar explícitamente si desea persistir la información:

```text
¿Deseas guardar este libro?
1) Sí
2) No
```

---

### ✔️ Consulta por idioma (Native Query)

Los idiomas se almacenan como un **ARRAY (`TEXT[]`) en PostgreSQL**.

Para mantener este diseño, se utiliza una **native query**:

```java
@Query(
  value = "SELECT * FROM libros WHERE :idioma = ANY(idiomas)",
  nativeQuery = true
)
List<Libro> buscarLibrosPorIdioma(String idioma);
```

Esto evita modificar la estructura existente y garantiza compatibilidad con PostgreSQL.

---

## 🗄️ Base de datos

Configuración típica en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ Ejecución del proyecto

1. Clonar el repositorio
2. Crear la base de datos en PostgreSQL
3. Configurar credenciales en `application.properties`
4. Ejecutar el proyecto desde el IDE o con:

```bash
mvn spring-boot:run
```

---

## 🎯 Objetivo del proyecto

Este proyecto tiene como finalidad:

* Practicar consumo de APIs externas
* Profundizar en JPA y relaciones entre entidades
* Aplicar buenas prácticas de persistencia
* Consolidar conocimientos de backend con Spring Boot

---

## 👨‍💻 Autor

**Henry Peralta Briceño**
Estudiante de Ingeniería de Sistemas – UNAD
Participante del programa **Alura ONE**

---

## 📌 Estado del proyecto

✅ Funcionalidades básicas completas
🔧 Proyecto abierto a mejoras futuras (paginación, DTOs de salida, tests, REST API)

---

⭐ ¡Gracias por revisar este proyecto!
