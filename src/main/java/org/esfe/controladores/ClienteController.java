package org.esfe.controladores;

import jakarta.validation.Valid;
import org.esfe.modelos.Cliente;
import org.esfe.modelos.Distrito; // Importar Distrito
import org.esfe.servicios.interfaces.IClienteService;
import org.esfe.servicios.interfaces.IDistritoService; // Importar IDistritoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder; // Importar WebDataBinder
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport; // Importar PropertyEditorSupport
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IDistritoService distritoService; // Inyectar el servicio de Distrito

    // --- BLOQUE InitBinder para Distrito ---
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Distrito.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String distritoId) throws IllegalArgumentException {
                System.out.println("DEBUG (PropertyEditor - Distrito): distritoId recibido: '" + distritoId + "'");
                if (distritoId != null && !distritoId.isEmpty()) {
                    try {
                        Integer id = Integer.parseInt(distritoId);
                        Optional<Distrito> distrito = distritoService.buscarPorId(id);
                        if (distrito.isPresent()) {
                            setValue(distrito.get());
                            System.out.println("DEBUG (PropertyEditor - Distrito): Distrito encontrado y seteado: " + distrito.get().getNombre() + " (ID: " + id + ")");
                        } else {
                            setValue(null); // Esto hará que @NotNull en Cliente falle si no se encuentra
                            System.out.println("DEBUG (PropertyEditor - Distrito): Distrito NO encontrado para ID: " + id + ". Setenado a null.");
                        }
                    } catch (NumberFormatException e) {
                        setValue(null); // Si no es un número válido
                        System.err.println("DEBUG (PropertyEditor - Distrito): Error de formato de número para ID de distrito: '" + distritoId + "'. Setenado a null.");
                    }
                } else {
                    setValue(null); // Si el ID es nulo o vacío
                    System.out.println("DEBUG (PropertyEditor - Distrito): distritoId es nulo o vacío. Setenado a null.");
                }
            }
        });
    }
    // --- FIN BLOQUE InitBinder ---

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page, // Cambiado a defaultValue "0" para PageRequest
                        @RequestParam(value = "size", defaultValue = "10") int size) { // Cambiado a defaultValue "10"

        if (page < 0) page = 0; // Asegurarse que la página no sea negativa

        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteService.buscarTodosPaginados(pageable);

        model.addAttribute("clientes", clientes);

        int totalPages = clientes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "cliente/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("distritos", distritoService.obtenerTodos()); // Cargar lista de distritos
        return "cliente/create";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("cliente") Cliente cliente, // Usar @ModelAttribute explícitamente
                       BindingResult result,
                       RedirectAttributes redirectAttributes, // Usar redirectAttributes para éxito
                       Model model) { // Usar model para errores

        if (result.hasErrors()) {
            System.err.println("ERROR DE VALIDACIÓN (Cliente - save): Errores en el formulario:");
            result.getAllErrors().forEach(error -> {
                System.err.println("  - Campo: " + (error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "N/A") + ", Mensaje: " + error.getDefaultMessage());
            });

            model.addAttribute("error", "Error de validación al guardar el cliente. Verifique los campos marcados.");
            model.addAttribute("distritos", distritoService.obtenerTodos()); // Para repoblar el select
            return "cliente/create"; // Regresa a la misma vista sin redireccionar
        }

        try {
            System.out.println("DEBUG (Cliente - save): Intentando guardar cliente: " + cliente.getNombre() +
                    " en distrito: " + (cliente.getDistrito() != null ? cliente.getDistrito().getNombre() : "NULL"));
            clienteService.crearOEditar(cliente);
            redirectAttributes.addFlashAttribute("msg", "Cliente guardado exitosamente!"); // Solo aquí se usa
            System.out.println("DEBUG (Cliente - save): Cliente guardado con ID: " + cliente.getId());
        } catch (Exception e) {
            System.err.println("ERROR DE PERSISTENCIA (Cliente - save): " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para más detalles
            model.addAttribute("error", "Ocurrió un error al intentar guardar el cliente: " + e.getMessage());
            model.addAttribute("distritos", distritoService.obtenerTodos()); // Para repoblar el select
            return "cliente/create"; // Regresa a la misma vista
        }

        return "redirect:/clientes";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) { // Cambiado a redirectAttributes
        Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);
        if (clienteOptional.isPresent()) {
            model.addAttribute("cliente", clienteOptional.get());
            return "cliente/details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/clientes";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) { // Cambiado a redirectAttributes
        Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);
        if (clienteOptional.isPresent()) {
            model.addAttribute("cliente", clienteOptional.get());
            model.addAttribute("distritos", distritoService.obtenerTodos()); // Cargar lista de distritos para edición
            return "cliente/edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/clientes";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("cliente") Cliente cliente, // Usar @ModelAttribute explícitamente
                         BindingResult result,
                         RedirectAttributes redirectAttributes, // Usar redirectAttributes para éxito
                         Model model) { // Usar model para errores

        if (result.hasErrors()) {
            System.err.println("ERROR DE VALIDACIÓN (Cliente - update): Errores en el formulario:");
            result.getAllErrors().forEach(error -> {
                System.err.println("  - Campo: " + (error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "N/A") + ", Mensaje: " + error.getDefaultMessage());
            });

            model.addAttribute("error", "Error de validación al actualizar el cliente. Verifique los campos marcados.");
            model.addAttribute("distritos", distritoService.obtenerTodos()); // Para repoblar el select
            return "cliente/edit"; // Regresa a la misma vista sin redireccionar
        }

        // Asegurarse de que el ID del cliente del formulario coincida con el ID de la URL
        cliente.setId(id);
        try {
            System.out.println("DEBUG (Cliente - update): Intentando actualizar cliente: " + cliente.getNombre() +
                    " en distrito: " + (cliente.getDistrito() != null ? cliente.getDistrito().getNombre() : "NULL"));
            clienteService.crearOEditar(cliente);
            redirectAttributes.addFlashAttribute("msg", "Cliente actualizado exitosamente!"); // Solo aquí se usa
            System.out.println("DEBUG (Cliente - update): Cliente actualizado con ID: " + cliente.getId());
        } catch (Exception e) {
            System.err.println("ERROR DE PERSISTENCIA (Cliente - update): " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo para más detalles
            model.addAttribute("error", "Ocurrió un error al intentar actualizar el cliente: " + e.getMessage());
            model.addAttribute("distritos", distritoService.obtenerTodos()); // Para repoblar el select
            return "cliente/edit"; // Regresa a la misma vista
        }

        return "redirect:/clientes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) { // Cambiado a redirectAttributes
        try {
            // Verificar si existe antes de eliminar (opcional, pero buena práctica)
            Optional<Cliente> clienteOptional = clienteService.buscarPorId(id);
            if (clienteOptional.isPresent()) {
                clienteService.eliminarPorId(id);
                redirectAttributes.addFlashAttribute("msg", "Cliente eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo eliminar: Cliente no encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
            System.err.println("ERROR AL ELIMINAR (Cliente - remove): " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }
}