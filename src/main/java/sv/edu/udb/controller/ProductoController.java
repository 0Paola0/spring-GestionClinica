package sv.edu.udb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Producto;
import sv.edu.udb.service.ProductoService;

@Controller
@RequestMapping("/almacen")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping({"", "/lista"})
    public String lista(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "almacen/listaProductos";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "almacen/formProducto";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/almacen/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        productoService.obtenerPorId(id).ifPresent(prod -> model.addAttribute("producto", prod));
        return "almacen/formProducto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/almacen/lista";
    }
}

