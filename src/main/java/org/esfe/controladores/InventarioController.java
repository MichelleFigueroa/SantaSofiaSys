package org.esfe.controladores;

import org.esfe.modelos.Inventario;
import org.esfe.servicios.interfaces.IInventarioService;
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
@RequestMapping("/inventarios")
public class InventarioController {
    @Autowired
    private IInventarioService inventarioService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Inventario> inventarios = inventarioService.buscarTodosPaginados(pageable);
        model.addAttribute("inventarios", inventarios);

        int totalPages = inventarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "inventario/index";
    }

    @GetMapping("/create")
    public String create (Inventario inventario){
        return "inventario/create";
    }

    @PostMapping("/save")
    public String save(Inventario inventario, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(inventario);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "inventario/create";
        }

        inventarioService.crearOEditar(inventario);
        attributes.addFlashAttribute("msg", "Inventario creado con exito.");
        return "redirect:/inventarios";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model){
        Inventario inventario = inventarioService.buscarPorId(id).get();
        model.addAttribute("inventario", inventario);
        return "inventario/details";
    }
}
