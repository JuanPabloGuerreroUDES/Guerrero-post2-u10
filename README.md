# Guerrero-post2-u10
## Juan Pablo Guerrero Hernandez - 02230132029
## Pruebas E2E con Selenium, Postman y Newman
**Programación Web — Unidad 10 | Ingeniería de Sistemas — UDES 2026**

---

## Prerrequisitos

| Herramienta | Versión mínima |
|-------------|----------------|
| Java JDK    | 17+            |
| Maven       | 3.9.x          |
| Google Chrome | Versión estable |
| Node.js     | 18+            |
| Postman Desktop | v10+       |

---

## Estructura del Proyecto

```
Guerrero-post2-u10/
├── src/
│   ├── main/java/com/guerrero/tareas/
│   │   ├── controller/
│   │   │   ├── TareaController.java       # REST API
│   │   │   └── TareaViewController.java   # Vista Thymeleaf /tareas
│   │   ├── entity/, repository/, service/, exception/
│   │   └── TareasApplication.java
│   ├── main/resources/
│   │   ├── templates/tareas.html          # Vista para Selenium
│   │   └── application.properties
│   └── test/java/com/guerrero/tareas/
│       ├── e2e/
│       │   ├── TareasPage.java            # Page Object — lista de tareas
│       │   ├── NuevaTareaPage.java        # Page Object — formulario
│       │   └── TareasE2ETest.java         # Tests Selenium (Checkpoint 1)
│       ├── service/TareaServiceTest.java
│       ├── controller/TareaControllerTest.java
│       └── repository/TareaRepositoryTest.java
├── postman/
│   ├── ColeccionToDo.json                 # Colección Postman (Checkpoint 2)
│   ├── env-local.json                     # Entorno local
│   └── env-ci.json                        # Entorno CI/CD
└── .github/workflows/
    └── api-tests.yml                      # Pipeline GitHub Actions (Checkpoint 3)
```

---

## Checkpoint 1 — Pruebas E2E con Selenium

### Ejecutar tests Selenium (requiere Chrome instalado)
```bash
mvn test -Dtest=TareasE2ETest
```

Los tests corren en modo **headless** (sin abrir ventana). `WebDriverManager` descarga ChromeDriver automáticamente.

**Tests implementados:**
- `paginaTareas_cargaCorrectamente_tituloCorrecto`
- `paginaTareas_formularioNuevaTarea_estaVisible`
- `crearTarea_conTituloValido_aparecEEnLista`
- `crearTarea_nuevaTarea_apareceComoPendiente`
- `completarTarea_tareaExistente_cambiaEstadoACompletada`

---

## Checkpoint 2 — Colección Postman

### Importar en Postman
1. **Import** → `postman/ColeccionToDo.json`
2. Importar entorno → `postman/env-local.json`
3. Seleccionar entorno **ToDoApp-Local**
4. Iniciar app: `mvn spring-boot:run`
5. **Collection Runner** → API ToDoApp → **Run**

### Ejecutar con Newman
```bash
npm install -g newman
mvn spring-boot:run          # en otra terminal
newman run postman/ColeccionToDo.json --environment postman/env-local.json
```

**5 requests:** POST crear → GET obtener → PATCH completar → GET verificar → GET 404

---

## Checkpoint 3 — GitHub Actions

El workflow `.github/workflows/api-tests.yml` se activa en cada `push`/`pull_request`.
Ver resultados en: **GitHub → pestaña Actions → API Tests con Newman**

---

## Ejecutar todos los tests
```bash
mvn clean test                          # incluye Selenium (requiere Chrome)
mvn clean test -Dexclude=**/e2e/**     # solo unitarios e integración
```
