package com.guerrero.tareas.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object que encapsula los selectores y acciones de la página /tareas.
 * Checkpoint 1 — Patrón Page Object Model
 */
public class TareasPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectores encapsulados como constantes privadas
    private static final By TITULO_PAGINA  = By.id("titulo-pagina");
    private static final By INPUT_TITULO   = By.id("titulo");
    private static final By INPUT_DESC     = By.id("descripcion");
    private static final By BTN_NUEVA      = By.id("btn-nueva");
    private static final By LIST_ITEMS     = By.cssSelector(".tarea-item");
    private static final By BADGES_ESTADO  = By.cssSelector(".tarea-estado");
    private static final By BTNS_COMPLETAR = By.cssSelector(".btn-completar");
    private static final By BTNS_ELIMINAR  = By.cssSelector(".btn-eliminar");

    public TareasPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /** Retorna el texto del título principal H1 de la página. */
    public String obtenerTituloPagina() {
        return driver.findElement(TITULO_PAGINA).getText();
    }

    /** Cuenta cuántas tareas están visibles en la lista. */
    public int contarTareas() {
        return driver.findElements(LIST_ITEMS).size();
    }

    /**
     * Llena el formulario y envía una nueva tarea.
     * @param titulo      Título de la tarea (requerido)
     * @param descripcion Descripción (puede ser null)
     */
    public void crearTarea(String titulo, String descripcion) {
        WebElement inputTitulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(INPUT_TITULO));
        inputTitulo.clear();
        inputTitulo.sendKeys(titulo);

        if (descripcion != null && !descripcion.isBlank()) {
            WebElement inputDesc = driver.findElement(INPUT_DESC);
            inputDesc.clear();
            inputDesc.sendKeys(descripcion);
        }

        driver.findElement(BTN_NUEVA).click();

        // Esperar a que la página recargue y aparezca la nueva tarea
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                LIST_ITEMS, contarTareas() - 1));
    }

    /** Retorna la lista de badges de estado (.tarea-estado). */
    public List<WebElement> obtenerBadgesEstado() {
        return driver.findElements(BADGES_ESTADO);
    }

    /** Hace clic en el botón "Completar" del primer ítem pendiente. */
    public void completarPrimeraTareaPendiente() {
        List<WebElement> btns = driver.findElements(BTNS_COMPLETAR);
        if (!btns.isEmpty()) {
            btns.get(0).click();
        }
    }

    /** Hace clic en el botón "Eliminar" del primer ítem. */
    public void eliminarPrimeraTarea() {
        List<WebElement> btns = driver.findElements(BTNS_ELIMINAR);
        if (!btns.isEmpty()) {
            btns.get(0).click();
        }
    }

    /** Navega a la página de tareas. */
    public void abrir(String baseUrl) {
        driver.get(baseUrl + "/tareas");
    }
}
