# Guía: Conexión a PostgreSQL (NeonTech) con Hibernate/JPA

Esta guía documenta el proceso completo para conectar la aplicación BibliotecaDUOC a una base de datos PostgreSQL en NeonTech usando Hibernate/JPA.

---

## 📋 Tabla de Contenidos

1. [Prerequisitos](#prerequisitos)
2. [Configuración de NeonTech](#configuración-de-neontech)
3. [Configuración del Proyecto](#configuración-del-proyecto)
4. [Migración del Repository](#migración-del-repository)
5. [Verificación](#verificación)
6. [Troubleshooting](#troubleshooting)

---

## 1. Prerequisitos

### Dependencias Maven

Ya agregadas en `pom.xml`:

```xml
<!-- Spring Data JPA + Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Software Necesario

- ✅ Java 21+
- ✅ Maven 3.9+
- ✅ Cuenta en NeonTech (https://neon.tech)

---

## 2. Configuración de NeonTech

### Paso 1: Crear Proyecto en Neon

1. Ingresa a https://console.neon.tech
2. Click en **"New Project"**
3. Configura:
   - **Project name**: `bibliotecaduoc`
   - **Region**: Selecciona el más cercano (ej: `US East`)
   - **PostgreSQL version**: `16` (recomendado)
4. Click en **"Create Project"**

### Paso 2: Obtener Credenciales

Neon te mostrará una pantalla con la cadena de conexión:

```
postgresql://username:password@ep-cool-silence-123456.us-east-2.aws.neon.tech/neondb?sslmode=require
```

**Descompón la URL**:
- **Endpoint**: `ep-cool-silence-123456.us-east-2.aws.neon.tech`
- **Database**: `neondb` (puedes crear una nueva base de datos si prefieres)
- **Username**: `username` (mostrado en la consola)
- **Password**: Copia el password (¡guárdalo!, no se muestra de nuevo)

### Paso 3: (Opcional) Crear Base de Datos Específica

En la consola de Neon:

1. Ve a **SQL Editor**
2. Ejecuta:
   ```sql
   CREATE DATABASE bibliotecaduoc;
   ```
3. Usa `bibliotecaduoc` como nombre de base de datos en la configuración

---

## 3. Configuración del Proyecto

### Paso 1: Configurar `application.properties`

Edita `src/main/resources/application.properties`:

**Reemplaza** los valores entre `<>` con tus credenciales reales:

```properties
# ===================================
# PostgreSQL + Hibernate (JPA) - NeonTech
# ===================================

spring.datasource.url=jdbc:postgresql://ep-cool-silence-123456.us-east-2.aws.neon.tech/neondb?sslmode=require
spring.datasource.username=tu_username_real
spring.datasource.password=tu_password_real
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### ⚠️ Explicación de `ddl-auto`

| Valor | Comportamiento |
|-------|----------------|
| `update` | **Recomendado desarrollo**: Actualiza schema sin borrar datos |
| `create` | Borra y recrea tabla en cada inicio (⛔ pierde datos) |
| `create-drop` | Borra tabla al cerrar aplicación |
| `validate` | Solo valida que schema coincida (producción) |
| `none` | No hace nada automáticamente |

### Paso 2: Verificar Entidad `Libro`

La clase `Libro` ya está configurada como entidad JPA:

```java
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    // ... otros campos
}
```

**Anotaciones clave**:
- `@Entity`: Marca la clase como entidad JPA
- `@Table(name = "libros")`: Nombre de la tabla en la BD
- `@Id`: Clave primaria
- `@GeneratedValue`: PostgreSQL genera el ID automáticamente
- `@Column`: Configuración de cada columna

---

## 4. Migración del Repository

### Opción A: JPA Repository (Recomendado 2026)

**Elimina** la implementación manual y crea una **interface**:

```java
package com.example.bibliotecaduoc.repository;

import com.example.bibliotecaduoc.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Métodos automáticos heredados de JpaRepository:
    // - findAll()
    // - findById(int id)
    // - save(Libro libro)
    // - deleteById(int id)
    // - count()

    // Métodos custom (Spring Data JPA los implementa automáticamente)
    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByAutorContainingIgnoreCase(String autor);
}
```

**⚠️ IMPORTANTE**: Al hacer esto, necesitas actualizar `LibroService` para usar los métodos JPA:

```java
// Antes (con ArrayList)
libroRepository.obtenerLibros();

// Después (con JPA)
libroRepository.findAll();
```

### Opción B: Mantener Implementación Manual (No recomendado)

Si prefieres mantener el código actual, deberás inyectar `EntityManager` manualmente. **No lo recomiendo** para este proyecto.

---

## 5. Verificación

### Paso 1: Compilar

```bash
./mvnw clean compile
```

Debe completar sin errores.

### Paso 2: Ejecutar Aplicación

```bash
./mvnw spring-boot:run
```

**Busca en los logs**:

```
Hibernate: create table if not exists libros (
    id int4 generated by default as identity,
    isbn varchar(20) not null,
    titulo varchar(200) not null,
    editorial varchar(100) not null,
    fecha_publicacion int4 not null,
    autor varchar(150) not null,
    primary key (id)
)
```

Si ves esto, **¡la tabla se creó correctamente!** ✅

### Paso 3: Verificar en Neon SQL Editor

1. Ve a https://console.neon.tech
2. Click en **SQL Editor**
3. Ejecuta:
   ```sql
   SELECT * FROM libros;
   ```

Debería mostrar la tabla vacía pero existente.

### Paso 4: Probar API con Swagger

1. Abre http://localhost:8080/swagger-ui.html
2. Prueba **POST /api/v1/libros**:
   ```json
   {
     "isbn": "978-0-13-468599-1",
     "titulo": "Clean Code",
     "editorial": "Prentice Hall",
     "fechaPublicacion": 2008,
     "autor": "Robert C. Martin"
   }
   ```
3. Verifica con **GET /api/v1/libros**

---

## 6. Troubleshooting

### Error: "Connection refused"

**Causa**: No puede conectar a Neon.

**Solución**:
1. Verifica que la URL en `application.properties` tenga `?sslmode=require`
2. Confirma que el endpoint es correcto (copia-pega desde Neon Console)
3. Revisa firewall/VPN

### Error: "Password authentication failed"

**Causa**: Credenciales incorrectas.

**Solución**:
1. Regenera el password en Neon Console → Settings → Reset Password
2. Actualiza `application.properties`

### Error: "relation 'libros' does not exist"

**Causa**: Hibernate no creó la tabla.

**Solución**:
1. Verifica `spring.jpa.hibernate.ddl-auto=update`
2. Revisa que `@Entity` esté en la clase `Libro`
3. Compila con `./mvnw clean compile`

### Hibernate no muestra SQL en logs

**Solución**:
```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Tabla se crea pero datos no persisten

**Causa**: Tal vez estás usando memoria en lugar de la BD.

**Solución**:
- Asegúrate que `LibroRepository` extienda `JpaRepository`
- Verifica que el `@Autowired` o constructor injection esté correcto en `LibroService`

---

## 📚 Recursos Adicionales

- **Neon Docs**: https://neon.tech/docs/introduction
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Hibernate Guide**: https://hibernate.org/orm/documentation/6.4/

---

## ✅ Checklist Final

- [ ] Dependencias agregadas en `pom.xml`
- [ ] Credenciales configuradas en `application.properties`
- [ ] Entidad `Libro` con anotaciones JPA
- [ ] `LibroRepository` migrado a JPA interface
- [ ] `LibroService` actualizado
- [ ] Aplicación inicia sin errores
- [ ] Tabla `libros` creada en Neon
- [ ] API probada con Swagger

---

**¿Problemas?** Revisa los logs completos con:
```bash
./mvnw spring-boot:run > app.log 2>&1
```

Luego busca errores en `app.log`.
