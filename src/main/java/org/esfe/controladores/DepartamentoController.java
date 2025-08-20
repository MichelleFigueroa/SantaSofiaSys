package org.esfe.controladores;

import jakarta.validation.Valid; // Asegúrate de tener la dependencia para jakarta.validation
import org.esfe.modelos.Departamento;
import org.esfe.servicios.interfaces.IDepartamentoService;
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
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private IDepartamentoService departamentoService;

    // Muestra la lista paginada de departamentos
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // page es 1-based en la URL, pero 0-based para PageRequest
        int pageSize = size.orElse(5); // Tamaño de la página por defecto

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Departamento> departamentos = departamentoService.buscarTodosPaginados(pageable);

        model.addAttribute("departamentos", departamentos);

        // Lógica para mostrar los números de página en la vista
        int totalPages = departamentos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "departamento/index"; // Vista para mostrar el listado de departamentos
    }

    // Muestra el formulario para crear un nuevo departamento
    @GetMapping("/create")
    public String create(Departamento departamento, Model model) {
        // El objeto 'departamento' se agrega automáticamente al modelo por Spring para el formulario
        return "departamento/create"; // Vista del formulario de creación
    }

    // Procesa el envío del formulario para guardar un nuevo departamento
    @PostMapping("/save")
    public String save(@Valid Departamento departamento, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario de creación
            model.addAttribute("departamento", departamento); // Mantiene los datos ingresados
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error de validación.");
            return "departamento/create";
        }

        departamentoService.crearOEditar(departamento); // Guarda el nuevo departamento
        attributes.addFlashAttribute("msg", "Departamento creado correctamente.");
        return "redirect:/departamentos"; // Redirige a la lista de departamentos
    }

    // Muestra los detalles de un departamento específico por su ID
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Optional<Departamento> departamentoOptional = departamentoService.buscarPorId(id);
        if (!departamentoOptional.isPresent()) {
            attributes.addFlashAttribute("error", "Departamento no encontrado.");
            return "redirect:/departamentos"; // Si no se encuentra, redirige a la lista
        }
        model.addAttribute("departamento", departamentoOptional.get());
        return "departamento/details"; // Vista para mostrar los detalles del departamento
    }

    // Muestra el formulario para editar un departamento existente por su ID
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        Optional<Departamento> departamentoOptional = departamentoService.buscarPorId(id);
        if (!departamentoOptional.isPresent()) {
            attributes.addFlashAttribute("error", "Departamento no encontrado.");
            return "redirect:/departamentos"; // Si no se encuentra, redirige a la lista
        }
        model.addAttribute("departamento", departamentoOptional.get());
        return "departamento/edit"; // Vista del formulario de edición
    }

    // Procesa el envío del formulario para actualizar un departamento existente
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @Valid Departamento departamento, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Si hay errores de validación, vuelve al formulario de edición
            model.addAttribute("departamento", departamento); // Mantiene los datos ingresados
            attributes.addFlashAttribute("error", "No se pudo actualizar debido a un error de validación.");
            return "departamento/edit";
        }

        // Verifica si el departamento existe antes de intentar actualizar
        Optional<Departamento> existingDepartamentoOptional = departamentoService.buscarPorId(id);
        if (!existingDepartamentoOptional.isPresent()) {
            attributes.addFlashAttribute("error", "Departamento no encontrado para actualizar.");
            return "redirect:/departamentos";
        }

        // Es crucial establecer el ID del departamento del formulario para asegurar
        // que 'crearOEditar' realice una actualización y no una inserción nueva.
        departamento.setId(id);
        departamentoService.crearOEditar(departamento); // Actualiza el departamento
        attributes.addFlashAttribute("msg", "Departamento actualizado correctamente.");
        return "redirect:/departamentos"; // Redirige a la lista de departamentos
    }

    // Elimina un departamento por su ID
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        Optional<Departamento> departamentoOptional = departamentoService.buscarPorId(id); // Verifica si existe
        if (departamentoOptional.isPresent()) {
            departamentoService.eliminarPorId(id); // Elimina el departamento
            attributes.addFlashAttribute("msg", "Departamento eliminado correctamente.");
        } else {
            attributes.addFlashAttribute("error", "No se pudo eliminar el departamento o no fue encontrado.");
        }
        return "redirect:/departamentos"; // Redirige a la lista de departamentos
    }
}