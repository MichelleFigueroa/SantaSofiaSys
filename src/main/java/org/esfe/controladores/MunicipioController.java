package org.esfe.controladores;

import org.esfe.modelos.Departamento;
import org.esfe.modelos.Municipio;
import org.esfe.servicios.interfaces.IDepartamentoService;
import org.esfe.servicios.interfaces.IMunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/municipios")
public class MunicipioController {

    @Autowired
    private IMunicipioService municipioService;

    @Autowired
    private IDepartamentoService departamentoService;

    // --- BLOQUE InitBinder CON DEPURACIÓN ---
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Departamento.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String departamentoId) throws IllegalArgumentException {
                System.out.println("DEBUG (PropertyEditor): departamentoId recibido: '" + departamentoId + "'");
                if (departamentoId != null && !departamentoId.isEmpty()) {
                    try {
                        Integer id = Integer.parseInt(departamentoId);
                        Optional<Departamento> departamento = departamentoService.buscarPorId(id);
                        if (departamento.isPresent()) {
                            setValue(departamento.get());
                            System.out.println("DEBUG (PropertyEditor): Departamento encontrado y seteado: " + departamento.get().getNombre() + " (ID: " + id + ")");
                        } else {
                            setValue(null); // Esto hará que @NotNull en Municipio falle si no se encuentra
                            System.out.println("DEBUG (PropertyEditor): Departamento NO encontrado para ID: " + id + ". Setenado a null.");
                        }
                    } catch (NumberFormatException e) {
                        setValue(null); // Si no es un número válido
                        System.err.println("DEBUG (PropertyEditor): Error de formato de número para ID de departamento: '" + departamentoId + "'. Setenado a null.");
                    }
                } else {
                    setValue(null); // Si el ID es nulo o vacío
                    System.out.println("DEBUG (PropertyEditor): departamentoId es nulo o vacío. Setenado a null.");
                }
            }
        });
    }
    // --- FIN BLOQUE InitBinder ---

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {

        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size);
        Page<Municipio> municipios = municipioService.buscarTodosPaginados(pageable);

        model.addAttribute("municipios", municipios);

        int totalPages = municipios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "municipio/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("municipio", new Municipio());
        model.addAttribute("departamentos", departamentoService.obtenerTodos());
        return "municipio/create";
    }

    // --- MÉTODO save REVISADO ---
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("municipio") Municipio municipio,
                       BindingResult result,
                       RedirectAttributes redirectAttributes, // Usado solo para éxito
                       Model model) { // Usado para errores

        if (result.hasErrors()) {
            System.err.println("ERROR DE VALIDACIÓN: Errores en el formulario (save):");
            result.getAllErrors().forEach(error -> {
                System.err.println("  - Campo: " + (error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "N/A") + ", Mensaje: " + error.getDefaultMessage());
            });

            // Añadir el mensaje de error directamente al Model para mostrar en la misma vista
            // En vez de redirectAttributes.addFlashAttribute("error", "...");
            model.addAttribute("error", "Error de validación al guardar el municipio. Verifique los campos marcados.");
            model.addAttribute("departamentos", departamentoService.obtenerTodos()); // Para repoblar el select
            return "municipio/create"; // Regresa a la misma vista sin redireccionar
        }

        try {
            System.out.println("DEBUG (save): Intentando guardar municipio: " + municipio.getNombre() +
                    " en departamento: " + (municipio.getDepartamento() != null ? municipio.getDepartamento().getNombre() : "NULL"));
            municipioService.crearOEditar(municipio);
            redirectAttributes.addFlashAttribute("msg", "Municipio guardado exitosamente!"); // Solo aquí se usa
            System.out.println("DEBUG (save): Municipio guardado con ID: " + municipio.getId());
        } catch (Exception e) {
            System.err.println("ERROR DE PERSISTENCIA (save): " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para más detalles
            // Añadir el error al Model para mostrar en la misma vista si falla la persistencia
            model.addAttribute("error", "Ocurrió un error al intentar guardar el municipio: " + e.getMessage());
            model.addAttribute("departamentos", departamentoService.obtenerTodos()); // Para repoblar el select
            return "municipio/create"; // Regresa a la misma vista
        }

        return "redirect:/municipios"; // Solo redirecciona si todo fue exitoso
    }
    // --- FIN MÉTODO save REVISADO ---

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Municipio> municipio = municipioService.buscarPorId(id);
        if (municipio.isPresent()) {
            model.addAttribute("municipio", municipio.get());
            return "municipio/details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Municipio no encontrado.");
            return "redirect:/municipios";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Municipio> municipio = municipioService.buscarPorId(id);
        if (municipio.isPresent()) {
            model.addAttribute("municipio", municipio.get());
            model.addAttribute("departamentos", departamentoService.obtenerTodos());
            return "municipio/edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Municipio no encontrado.");
            return "redirect:/municipios";
        }
    }

    // --- MÉTODO update REVISADO ---
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("municipio") Municipio municipio,
                         BindingResult result,
                         RedirectAttributes redirectAttributes, // Usado solo para éxito
                         Model model) { // Usado para errores

        if (result.hasErrors()) {
            System.err.println("ERROR DE VALIDACIÓN (update): Errores en el formulario:");
            result.getAllErrors().forEach(error -> {
                System.err.println("  - Campo: " + (error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "N/A") + ", Mensaje: " + error.getDefaultMessage());
            });

            model.addAttribute("error", "Error de validación al actualizar el municipio. Verifique los campos marcados.");
            model.addAttribute("departamentos", departamentoService.obtenerTodos()); // Para repoblar el select
            return "municipio/edit"; // Regresa a la misma vista sin redireccionar
        }

        municipio.setId(id); // Asegura que el ID del objeto sea el mismo que el de la URL para la actualización
        try {
            System.out.println("DEBUG (update): Intentando actualizar municipio: " + municipio.getNombre() +
                    " en departamento: " + (municipio.getDepartamento() != null ? municipio.getDepartamento().getNombre() : "NULL"));
            municipioService.crearOEditar(municipio);
            redirectAttributes.addFlashAttribute("msg", "Municipio actualizado exitosamente!"); // Solo aquí se usa
            System.out.println("DEBUG (update): Municipio actualizado con ID: " + municipio.getId());
        } catch (Exception e) {
            System.err.println("ERROR DE PERSISTENCIA (update): " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para más detalles
            model.addAttribute("error", "Ocurrió un error al intentar actualizar el municipio: " + e.getMessage());
            model.addAttribute("departamentos", departamentoService.obtenerTodos()); // Para repoblar el select
            return "municipio/edit"; // Regresa a la misma vista
        }

        return "redirect:/municipios"; // Solo redirecciona si todo fue exitoso
    }
    // --- FIN MÉTODO update REVISADO ---

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            municipioService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("msg", "Municipio eliminado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el municipio: " + e.getMessage());
            System.err.println("ERROR AL ELIMINAR (remove): " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/municipios";
    }
}