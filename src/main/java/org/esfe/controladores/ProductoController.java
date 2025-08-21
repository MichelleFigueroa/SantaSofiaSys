package org.esfe.controladores;

import jakarta.validation.Valid;
import org.esfe.modelos.Producto;
import org.esfe.servicios.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Página actual (0 si no está definida)
        int pageSize = size.orElse(5); // Tamaño de la página (default 5)

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Producto> productos = productoService.buscarTodosPaginados(pageable);

        model.addAttribute("productos", productos);

        int totalPages = productos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "producto/index";
    }

    @GetMapping("/create")
    public String create(Producto producto) {
        return "producto/create";
    }

    @PostMapping("/save")
    public String save(@Valid Producto producto,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("producto", producto);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a errores en el formulario.");
            return "producto/create";
        }
        productoService.crearOEditar(producto);
        attributes.addFlashAttribute("msg", "Producto creado correctamente");
        return "redirect:/producto";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id,
                          Model model,
                          RedirectAttributes attributes) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "producto/details";
        } else {
            attributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/producto";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id,
                       Model model,
                       RedirectAttributes attributes) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "producto/edit";
        } else {
            attributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/producto";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id,
                         Model model,
                         RedirectAttributes attributes) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "producto/delete"; // Vista de confirmación
        } else {
            attributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/producto";
        }
    }
    @PostMapping("/delete")
    public String delete(Producto producto, RedirectAttributes attributes) {
        productoService.eliminarPorId(producto.getId());
        attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
        return "redirect:/producto";
    }
    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Producto> producto = productoService.buscarPorId(id); // usa tu método existente
            model.addAttribute("producto", producto.orElse(null));         // si no existe, pasa null
        }
        return "productos/buscarPorId"; // la misma vista para formulario y resultado
    }
}

