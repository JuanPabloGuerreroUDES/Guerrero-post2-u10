package com.guerrero.tareas.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object para el formulario de nueva tarea (mismo formulario en /tareas).
 * Se puede usar de forma independiente para pruebas del formulario en aislamiento.
 */
public class NuevaTareaPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selectores encapsulados
    private static final By INPUT_TITULO   = By.id("titulo");
    private static final By INPUT_DESC     = By.id("descripcion");
    private static final By BTN_SUBMIT     = By.id("btn-nueva");
    private static final By FORM           = By.id("form-nueva-tarea");

    public NuevaTareaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /** Verifica que el formulario está visible en la página. */
    public boolean formularioVisible() {
        return !driver.findElements(FORM).isEmpty();
    }

    /** Verifica que el campo título está habilitado. */
    public boolean campTituloHabilitado() {
        return driver.findElement(INPUT_TITULO).isEnabled();
    }

    /**
     * Ingresa solo el título sin enviar.
     * @param titulo texto a ingresar
     */
    public void ingresarTitulo(String titulo) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_TITULO))
                .sendKeys(titulo);
    }

    /** Ingresa la descripción sin enviar. */
    public void ingresarDescripcion(String descripcion) {
        driver.findElement(INPUT_DESC).sendKeys(descripcion);
    }

    /** Envía el formulario y retorna la página de tareas actualizada. */
    public TareasPage enviarFormulario() {
        driver.findElement(BTN_SUBMIT).click();
        return new TareasPage(driver);
    }

    /** Retorna el valor actual del campo título. */
    public String obtenerValorTitulo() {
        return driver.findElement(INPUT_TITULO).getAttribute("value");
    }
}
