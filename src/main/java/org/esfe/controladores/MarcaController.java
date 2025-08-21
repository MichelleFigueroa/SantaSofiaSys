package org.esfe.controladores;

import org.esfe.modelos.Marca;
import org.esfe.modelos.Producto;
import org.esfe.servicios.interfaces.IMarcaService;
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
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
    private IMarcaService marcaService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Marca> marcas = marcaService.buscarTodosPaginados(pageable);

        model.addAttribute("marcas", marcas);

        int totalPages = marcas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "marca/index";
    }
    @GetMapping("/create")
    public String create (Marca marca){
        return "marca/create";
    }

    @PostMapping("/save")
    public String save (Marca marca, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(marca);
            attributes.addFlashAttribute("error", "No se pudo guardar debido aun error.");
            return "marca/create";
        }
        marcaService.crearOEditar(marca);
        attributes.addFlashAttribute("msg", "marca  creada correctamente");
        return "redirect:/marca";
    }
    @GetMapping("/details/{id}")
    public String details (@PathVariable("id") Integer id, Model model){
        Marca marca = marcaService.buscarPorId(id).get();
        model.addAttribute("marca", marca);
        return "marca/details";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id,
                       Model model,
                       RedirectAttributes attributes) {
        Optional<Marca> marca = marcaService.buscarPorId(id);
        if (marca.isPresent()) {
            model.addAttribute("marca", marca.get());
            return "marca/edit";
        } else {
            attributes.addFlashAttribute("error", "La marca no existe");
            return "redirect:/marca";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id,
                         Model model,
                         RedirectAttributes attributes) {
        Optional<Marca> marca = marcaService.buscarPorId(id);
        if (marca.isPresent()) {
            model.addAttribute("marca", marca.get());
            return "marca/delete"; // Vista de confirmación
        } else {
            attributes.addFlashAttribute("error", "La Marca no existe");
            return "redirect:/marca";
        }
    }
    @PostMapping("/delete")
    public String delete(Marca marca, RedirectAttributes attributes) {
        marcaService.eliminarPorId(marca.getId());
        attributes.addFlashAttribute("msg", "Marca eliminada correctamente");
        return "redirect:/marca";
    }
    @GetMapping("/buscar")
    public String buscarPorId(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Marca> marca = marcaService.buscarPorId(id);
            model.addAttribute("marca", marca.orElse(null));
        }
        return "marcas/buscarPorId"; // la misma vista para formulario y resultado
    }
}