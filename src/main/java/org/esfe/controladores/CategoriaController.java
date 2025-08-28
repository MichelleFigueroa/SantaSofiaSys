package org.esfe.controladores;

import org.esfe.modelos.Categoria;
import org.esfe.servicios.interfaces.ICategoriaService;
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
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Categoria> categorias = categoriaService.buscarTodosPaginados(pageable);

        model.addAttribute("categorias", categorias);

        int totalPages = categorias.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "categoria/index";
    }
    @GetMapping("/create")
    public String create (Categoria categoria){
        return "categoria/create";
    }

    @PostMapping("/save")
    public String save (Categoria categoria, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(categoria);
            attributes.addFlashAttribute("error", "No se pudo guardar debido aun error.");
            return "categoria/create";
        }
        categoriaService.crearOEditar(categoria);
        attributes.addFlashAttribute("msg", "categoria creada correctamente");
        return "redirect:/categorias";
    }
    @GetMapping("/details/{id}")
    public String details (@PathVariable("id") Integer id, Model model){
        Categoria categoria = categoriaService.buscarPorId(id).get();
        model.addAttribute("categoria", categoria);
        return "categoria/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id,
                       Model model,
                       RedirectAttributes attributes) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "categoria/edit";
        } else {
            attributes.addFlashAttribute("error", "La categoria no existe");
            return "redirect:/categorias";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id,
                         Model model,
                         RedirectAttributes attributes) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "categoria/delete";
        } else {
            attributes.addFlashAttribute("error", "La categoria no existe");
            return "redirect:/categorias";
        }
    }
    @PostMapping("/delete")
    public String delete(Categoria categoria, RedirectAttributes attributes) {
        categoriaService.eliminarPorId(categoria.getId());
        attributes.addFlashAttribute("msg", "Categoria eliminada correctamente");
        return "redirect:/categorias";
    }
    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Categoria> categoria = categoriaService.buscarPorId(id); // usa tu método existente
            model.addAttribute("categoria", categoria.orElse(null));         // si no existe, pasa null
        }
        return "categoria/buscarPorId"; // la misma vista para formulario y resultado
    }
}