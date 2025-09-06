# User Management

Demo project for **Globalogic Test**  
Aplicación construida con **Spring Boot 2.5.14**, **Java 11** y **Gradle 7.4**.  
Usa **H2 Database** embebida, **Spring Security**, **JWT** (con Nimbus y JJWT), **MapStruct** y **Lombok**.

---

## Prerrequisitos

- **Java 11** instalado y configurado en tu `PATH`.
- **Gradle Wrapper** incluido en el proyecto (`./gradlew` o `gradlew.bat`).
- Conexión a **Maven Central** para descargar dependencias.

---

## Notas

### Colección Postman

- Para poder consumir las APIs del servicio se deja una colección postman:
[user_management_postman_collection.json](collections/user_management_postman_collection.json)

Ruta: /collections/user_management_postman_collection.json

### Base de datos H2

-  proyecto usa H2 en modo archivo (persistencia local).
El archivo de la base de datos se guarda en la carpeta del proyecto:

```yaml
./data/testdb.mv.db
```

- Para poder ingresar a la base de datos local tines que acceder al siguiente 
link (con el servicio en ejecución):
  http://localhost:8080/h2-console

### Generación y visualización del reporte de cobertura Jacoco

El proyecto está configurado con **Jacoco** para generar reportes de cobertura de pruebas unitarias.

### Generar el reporte

Ejecuta el siguiente comando desde la raíz del proyecto:

```bash
./gradlew jacocoTestReport
```

---

### APIs

Se exponen 2 Apis principales:

- **http://localhost:8080/users/sing-up** </br>

Este endpoint permite registrar un usuario devolviento un token JWT valido

- **http://localhost:8080/users/login** </br>

Este endpoint permite realizar la autenticación del usuario mediante el token JWT que retorna **_sing-up_**

---

## Construcción del proyecto

Ejecuta:

```bash
./gradlew clean build
```

## Ejecución del proyecto

- Debe dirigirse a la clase **_MainApplication_** y ejecutar _"Run File"_
