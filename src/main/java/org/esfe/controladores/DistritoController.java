package org.esfe.controladores;

import org.esfe.modelos.Distrito;
import org.esfe.modelos.Municipio;
import org.esfe.servicios.interfaces.IDistritoService;
import org.esfe.servicios.interfaces.IMunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/distritos")
public class DistritoController {

    @Autowired
    private IDistritoService distritoService;

    @Autowired
    private IMunicipioService municipioService;

    // LISTADO CON PAGINACIÓN
    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Distrito> distritos = distritoService.buscarTodosPaginados(pageable);

        model.addAttribute("distritos", distritos);

        int totalPages = distritos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "distrito/index";
    }

    // FORMULARIO CREAR
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("distrito", new Distrito());
        model.addAttribute("municipios", municipioService.obtenerTodos());
        return "distrito/crear";
    }

    // GUARDAR NUEVO DISTRITO
    @PostMapping("/save")
    public String save(@ModelAttribute Distrito distrito, Model model) {
        try {
            distritoService.crearOEditar(distrito);
            return "redirect:/distritos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el distrito");
            model.addAttribute("municipios", municipioService.obtenerTodos());
            return "distrito/crear";
        }
    }

    // FORMULARIO EDITAR
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Distrito distrito = distritoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Distrito no encontrado"));
        model.addAttribute("distrito", distrito);
        model.addAttribute("municipios", municipioService.obtenerTodos());
        return "distrito/editar";
    }

    // ACTUALIZAR DISTRITO
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute Distrito distrito, Model model) {
        try {
            distrito.setId(id);
            distritoService.crearOEditar(distrito);
            return "redirect:/distritos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el distrito");
            model.addAttribute("municipios", municipioService.obtenerTodos());
            return "distrito/editar";
        }
    }

    // VER DETALLES
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Distrito distrito = distritoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Distrito no encontrado"));
        model.addAttribute("distrito", distrito);
        return "distrito/details";
    }

    // ELIMINAR
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id) {
        distritoService.eliminarPorId(id);
        return "redirect:/distritos";
    }
}
