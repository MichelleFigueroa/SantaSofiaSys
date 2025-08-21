package org.esfe.controladores;

import org.esfe.modelos.Empleado;
import org.esfe.servicios.interfaces.IEmpleadoService;
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
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService empleadoService;

    // LISTAR EMPLEADOS CON PAGINACIÓN
    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Empleado> empleados = empleadoService.buscarTodosPaginados(pageable);

        model.addAttribute("empleados", empleados);

        int totalPages = empleados.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "empleado/index";
    }

    // FORMULARIO PARA CREAR NUEVO EMPLEADO
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("empleado", new Empleado());
        return "empleado/create";
    }

    // GUARDAR EMPLEADO (CREAR O EDITAR)
    @PostMapping("/save")
    public String save(Empleado empleado, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute("empleado", empleado);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "empleado/create";
        }

        empleadoService.crearOEditar(empleado);
        attributes.addFlashAttribute("msg", "Empleado guardado correctamente");
        return "redirect:/empleados";
    }

    // FORMULARIO PARA EDITAR EMPLEADO
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<Empleado> empleadoOpt = empleadoService.buscarPorId(id);
        if (empleadoOpt.isEmpty()) {
            return "redirect:/empleados";
        }
        model.addAttribute("empleado", empleadoOpt.get());
        return "empleado/create"; // Reusa el formulario de creación
    }

    // DETALLES DE UN EMPLEADO
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Optional<Empleado> empleadoOpt = empleadoService.buscarPorId(id);
        if (empleadoOpt.isEmpty()) {
            return "redirect:/empleados";
        }
        model.addAttribute("empleado", empleadoOpt.get());
        return "empleado/details";
    }

    // ELIMINAR UN EMPLEADO
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        empleadoService.eliminarPorId(id);
        attributes.addFlashAttribute("msg", "Empleado eliminado correctamente");
        return "redirect:/empleados";
    }


}
