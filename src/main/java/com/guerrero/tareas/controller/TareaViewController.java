package com.guerrero.tareas.controller;

import com.guerrero.tareas.entity.Tarea;
import com.guerrero.tareas.service.TareaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador MVC para vistas Thymeleaf.
 * Expone la ruta /tareas para las pruebas E2E con Selenium.
 */
@Controller
@RequestMapping("/tareas")
public class TareaViewController {

    private final TareaService service;

    public TareaViewController(TareaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tareas", service.listarTodas());
        return "tareas";
    }

    @PostMapping
    public String crear(@RequestParam String titulo,
                        @RequestParam(required = false) String descripcion) {
        Tarea t = new Tarea();
        t.setTitulo(titulo);
        t.setDescripcion(descripcion);
        service.crear(t);
        return "redirect:/tareas";
    }

    @PostMapping("/{id}/completar")
    public String completar(@PathVariable Long id) {
        service.completar(id);
        return "redirect:/tareas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/tareas";
    }
}
