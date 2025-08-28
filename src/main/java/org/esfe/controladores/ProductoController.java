package org.esfe.controladores;

import jakarta.validation.Valid;
import org.esfe.modelos.Producto;
import org.esfe.servicios.interfaces.ICategoriaService;
import org.esfe.servicios.interfaces.IMarcaService;
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

    @Autowired
    private ICategoriaService categoriaService;

    @Autowired
    private IMarcaService marcaService;

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
    public String create(Producto producto, Model model)
    {
        model.addAttribute("categorias", categoriaService.obtenerTodos());
        model.addAttribute("marcas", marcaService.obtenerTodos());
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
        return "redirect:/productos";
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
            return "redirect:/productos";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("categorias", categoriaService.obtenerTodos()); // 👈 agregar
            model.addAttribute("marcas", marcaService.obtenerTodos());         // 👈 agregar
            return "producto/edit";
        } else {
            attributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/productos";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "producto/delete";
        } else {
            attributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/productos";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        productoService.eliminarPorId(id);
        attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
        return "redirect:/productos";
    }


    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Producto> producto = productoService.buscarPorId(id);
            model.addAttribute("producto", producto.orElse(null));
        }
        return "producto/buscarPorId";
    }
}

