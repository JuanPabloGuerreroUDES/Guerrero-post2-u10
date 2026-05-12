package com.guerrero.tareas.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas E2E con Selenium WebDriver — Patrón Page Object Model.
 * Checkpoint 1
 *
 * Requiere: Google Chrome instalado.
 * Ejecutar con: mvn test -Dtest=TareasE2ETest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TareasE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private TareasPage tareasPage;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless");
        opts.addArguments("--no-sandbox");
        opts.addArguments("--disable-dev-shm-usage");
        opts.addArguments("--disable-gpu");
        opts.addArguments("--window-size=1280,800");
        driver = new ChromeDriver(opts);

        tareasPage = new TareasPage(driver);
        tareasPage.abrir("http://localhost:" + port);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    // --- Test 1: la página carga con el título correcto ---

    @Test
    @Order(1)
    void paginaTareas_cargaCorrectamente_tituloCorrecto() {
        assertThat(driver.getTitle()).contains("Tareas");
        assertThat(tareasPage.obtenerTituloPagina()).contains("Gestión de Tareas");
    }

    // --- Test 2: el formulario de nueva tarea está visible ---

    @Test
    @Order(2)
    void paginaTareas_formularioNuevaTarea_estaVisible() {
        NuevaTareaPage nuevaTareaPage = new NuevaTareaPage(driver);
        assertThat(nuevaTareaPage.formularioVisible()).isTrue();
        assertThat(nuevaTareaPage.campTituloHabilitado()).isTrue();
    }

    // --- Test 3: crear tarea incrementa la lista ---

    @Test
    @Order(3)
    void crearTarea_conTituloValido_aparecEEnLista() {
        int antes = tareasPage.contarTareas();
        tareasPage.crearTarea("Tarea E2E de prueba", "Creada por Selenium");
        int despues = tareasPage.contarTareas();
        assertThat(despues).isGreaterThan(antes);
    }

    // --- Test 4: la nueva tarea aparece como pendiente ---

    @Test
    @Order(4)
    void crearTarea_nuevaTarea_apareceComoPendiente() {
        tareasPage.crearTarea("Tarea pendiente Selenium", null);
        boolean hayPendiente = tareasPage.obtenerBadgesEstado().stream()
                .anyMatch(badge -> badge.getText().equalsIgnoreCase("Pendiente"));
        assertThat(hayPendiente).isTrue();
    }

    // --- Test 5: completar tarea cambia su estado ---

    @Test
    @Order(5)
    void completarTarea_tareaExistente_cambiaEstadoACompletada() {
        // Crear primero una tarea pendiente
        tareasPage.crearTarea("Tarea para completar", null);

        long pendientesAntes = tareasPage.obtenerBadgesEstado().stream()
                .filter(b -> b.getText().equalsIgnoreCase("Pendiente")).count();

        tareasPage.completarPrimeraTareaPendiente();

        long completadasDespues = tareasPage.obtenerBadgesEstado().stream()
                .filter(b -> b.getText().equalsIgnoreCase("Completada")).count();

        assertThat(completadasDespues).isGreaterThanOrEqualTo(1);
    }
}
