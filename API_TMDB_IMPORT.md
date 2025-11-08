# Documentación: Importación de películas desde TMDB

Este documento describe cómo usar los endpoints del backend para importar películas desde The Movie Database (TMDB) hacia la base de datos del proyecto MovieStats.

## Resumen rápido
- Base URL del backend: http://localhost:8080
- Prefix API: `/api/v1/tmdb`
- Endpoints principales: importar por populares, top-rated, búsqueda, por id y sincronizaciones masivas (async)
- Rate limiting interno: el servicio respeta ~250ms entre llamadas a TMDB y batching para no golpear la API ni agotar conexiones DB.

---

## Endpoints (detalle)

### 1) Importar películas populares
- Método: POST
- URL: /api/v1/tmdb/importar-populares
- Query params:
  - `cantidad` (opcional): número de películas a solicitar (ej. ?cantidad=10). Si no se especifica puede existir un valor por defecto.
- Descripción: Trae las listas de películas populares desde TMDB y las intenta insertar en la base de datos. Previene duplicados comprobando por título.
- Respuesta (ejemplo):
  ```json
  {
    "peliculasSolicitadas": 10,
    "peliculasImportadas": 7,
    "success": true,
    "mensaje": "Películas populares importadas exitosamente"
  }
  ```

Ejemplo curl:
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/importar-populares?cantidad=10'
```

---

### 2) Importar películas top-rated (más valoradas)
- Método: POST
- URL: /api/v1/tmdb/importar-top-rated
- Query params:
  - `cantidad` (opcional)
- Descripción: Importa películas según la lista de las mejor valoradas en TMDB.

Ejemplo curl:
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/importar-top-rated?cantidad=20'
```

---

### 3) Buscar e importar por texto
- Método: POST
- URL: /api/v1/tmdb/buscar-importar
- Query params:
  - `query` (requerido): texto de búsqueda
  - `cantidad` (opcional)
- Descripción: Realiza una búsqueda en TMDB y trae e importa los primeros resultados.

Ejemplo curl:
```bash
curl -X POST "http://localhost:8080/api/v1/tmdb/buscar-importar?query=Matrix&cantidad=5"
```

---

### 4) Importar una película por TMDB ID (individual)
- Método: POST
- URL: /api/v1/tmdb/importar-id/{tmdbId}
- Descripción: Importa una sola película por su id en TMDB (útil para pruebas y correcciones manuales).

Ejemplo curl:
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/importar-id/550'
```

Respuesta esperada:
```json
{ "tmdbId": 550, "success": true, "mensaje": "Película importada exitosamente" }
```

---

### 5) Endpoints de sincronización masiva (async)
Estos endpoints ejecutan procesos asíncronos. Responden rápido con HTTP 202 y realizan el trabajo en background.

- POST /api/v1/tmdb/sync/quick
  - Descripción: Quick sync (ej. 200 películas) — balance entre velocidad y seguridad.
- POST /api/v1/tmdb/sync/full
  - Descripción: Full sync (ej. 1000 películas) — puede tardar y consumir muchas conexiones; usar con precaución.
- POST /api/v1/tmdb/sync/custom?cantidad={n}
  - Descripción: Ejecuta una sincronización con cantidad personalizada.
- POST /api/v1/tmdb/sync/categories?cantidad={n}
  - Descripción: Importa películas por categorías/géneros.
- GET /api/v1/tmdb/sync/status
  - Descripción: Devuelve el estado actual del proceso de sincronización (inProgress, porcentaje, importadas).

Ejemplo de consulta de estado:
```bash
curl 'http://localhost:8080/api/v1/tmdb/sync/status'
```

Respuesta (ejemplo):
```json
{ "inProgress": true, "totalRequested": 200, "imported": 56, "message": "En progreso" }
```

---

## Consideraciones de seguridad y permisos
- El proyecto usa Spring Security con JWT. Algunos endpoints públicos (películas, géneros y TMDB public controller) están permitidos sin token; otros requieren autenticación para acciones administrativas.
- Si tu endpoint devuelve 401, incluye el header `Authorization: Bearer <token>` con un JWT válido.

---

## Notas operativas importantes
1. Pool de conexiones (Supabase): La base de datos en Supabase puede imponer límites (Session mode). Para evitar errores `MaxClientsInSessionMode`:
   - Mantén `spring.datasource.hikari.maximum-pool-size` en un valor pequeño (por ejemplo 1–3) si usas Session Mode.
   - Alternativa: usar Transaction Mode (puerto 6543 en Supabase) que soporta más clientes.
   - Evita correr múltiples sincronizaciones masivas en paralelo.

2. IP Allow List: Si recibes `Address not in tenant allow_list`, agrega tu IP al listado de la consola de Supabase.

3. Rate limits y delay: El servicio aplica delays (≈250ms entre llamadas) y batching para no exceder TMDB y para mantener las conexiones DB bajo control.

4. Clasificación (rating): TMDB no siempre provee rating por rango de edad en el endpoint básico. El servicio usa el campo `adult` como proxy:
   - `adult = true` → `clasificacion = "R"`
   - `adult = false/null` → `clasificacion = "PG-13"` (valor por defecto)

   Nota: las películas ya importadas antes de corregir el bug pueden tener la columna `clasificacion` con el `tagline` o `status`. Para corregir el histórico, puedes:
   - Opción A: Ejecutar un `UPDATE` SQL que normalice la columna (por ejemplo reemplazar valores largos o vacíos por `PG-13` ó `R`).
   - Opción B: Borrar esas filas y reimportarlas por ID o mediante sincronización.

Ejemplo de SQL para fijar clasificaciones simples:
```sql
UPDATE pelicula
SET clasificacion = CASE WHEN clasificacion IS NULL OR length(clasificacion) > 10 THEN 'PG-13' ELSE clasificacion END
WHERE id IS NOT NULL;
```

---

## Cómo ejecutar el servidor localmente
Desde la raíz del proyecto:

```bash
# compilar y ejecutar (modo desarrollo)
./mvnw spring-boot:run

# construir jar y ejecutar
./mvnw clean package -DskipTests
java -jar target/moviestats-0.0.1-SNAPSHOT.jar
```

Si usas Supabase y necesitas Transaction Mode, ajusta la `spring.datasource.url` en `src/main/resources/application.properties` a algo como:
```
spring.datasource.url=jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:6543/postgres?sslmode=require
```

---

## Ejemplos de flujo de trabajo comunes
1. Probar importación rápida (3 películas):
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/importar-populares?cantidad=3'
```
2. Importar una película concreta por TMDB ID (por ejemplo para corregir datos):
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/importar-id/550'
```
3. Iniciar sincronización en background de 200 películas:
```bash
curl -X POST 'http://localhost:8080/api/v1/tmdb/sync/quick'
curl 'http://localhost:8080/api/v1/tmdb/sync/status'
```

---

## Troubleshooting rápido
- Error `MaxClientsInSessionMode`: reducir `maximum-pool-size` o usar Transaction Mode (6543).
- Error `Address not in tenant allow_list`: agregar IP a Supabase dashboard.
- Importaciones duplicadas: el servicio evita duplicados por título; si hay falsos positivos, revisar normalización de títulos (acentos/espacios).
- Si una importación falla por un movieId en particular, reintentar con `/importar-id/{id}` para ver el error específico.

---

## Contacto y próximos pasos
- Si quieres, puedo:
  - Añadir ejemplos de respuesta real extraídos de tu instancia (si levantas el servidor y me das permiso para acceder a endpoints locales).
  - Generar un script SQL para normalizar las 52 películas ya importadas (necesito confirmar las reglas que quieres aplicar).

---

Fecha: 2025-11-05
Proyecto: MovieStats Backend
