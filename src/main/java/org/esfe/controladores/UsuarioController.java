package org.esfe.controladores;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.Rol;
import org.esfe.modelos.Empleado;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.esfe.servicios.interfaces.IRolService;
import org.esfe.servicios.interfaces.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl; // Necesario para crear Page a partir de List
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Map;
import java.util.HashMap;

// DTO para la vista de índice, para evitar el problema N+1
class UsuarioDisplayDTO {
    private Integer id;
    private String nombreUsuario;
    private String rolNombre;
    private String empleadoNombreCompleto;
    private Integer status;
    // La clave no se necesita en el DTO de display por seguridad

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
    public String getEmpleadoNombreCompleto() { return empleadoNombreCompleto; }
    public void setEmpleadoNombreCompleto(String empleadoNombreCompleto) { this.empleadoNombreCompleto = empleadoNombreCompleto; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;
    @Autowired
    private IEmpleadoService empleadoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // INDEX con paginación
    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuariosPage = usuarioService.buscarTodosPaginados(pageable);

        // Pre-cargar todos los roles y empleados para evitar N+1 queries en el bucle
        Map<Integer, String> rolesMap = rolService.obtenerTodos().stream()
                .collect(Collectors.toMap(Rol::getId, Rol::getNombre));
        Map<Integer, String> empleadosMap = empleadoService.obtenerTodos().stream()
                .collect(Collectors.toMap(Empleado::getId, emp -> emp.getNombre() + " " + emp.getApellido()));


        List<UsuarioDisplayDTO> usuariosParaVista = usuariosPage.getContent().stream()
                .map(usuario -> {
                    UsuarioDisplayDTO dto = new UsuarioDisplayDTO();
                    dto.setId(usuario.getId());
                    dto.setNombreUsuario(usuario.getNombreUsuario());
                    dto.setStatus(usuario.getStatus());

                    // Obtener el nombre del rol del mapa
                    dto.setRolNombre(rolesMap.getOrDefault(usuario.getIdRol(), "N/A"));

                    // Obtener el nombre completo del empleado del mapa
                    dto.setEmpleadoNombreCompleto(empleadosMap.getOrDefault(usuario.getIdEmpleado(), "N/A"));

                    return dto;
                })
                .collect(Collectors.toList());

        // Volver a envolver en Page para mantener la estructura de paginación
        Page<UsuarioDisplayDTO> usuarios = new PageImpl<>(usuariosParaVista, pageable, usuariosPage.getTotalElements());

        model.addAttribute("usuarios", usuarios);

        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "usuario/index";
    }

    // CREATE (GET para mostrar el formulario)
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("usuario", new Usuario());
        List<Rol> roles = rolService.obtenerTodos();
        List<Empleado> empleados = empleadoService.obtenerTodos();
        model.addAttribute("roles", roles);
        model.addAttribute("empleados", empleados);
        return "usuario/create";
    }

    // SAVE (POST para procesar el formulario de creación)
    @PostMapping("/save")
    public String save(@RequestParam Integer idRol,
                       @RequestParam Integer idEmpleado,
                       @RequestParam String nombreUsuario,
                       @RequestParam String clave,
                       RedirectAttributes attributes) {

        Usuario usuario = new Usuario();
        usuario.setIdRol(idRol);
        usuario.setIdEmpleado(idEmpleado);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClave(passwordEncoder.encode(clave));

        // Establecer el status a 1 (activo) por defecto al crear un usuario
        usuario.setStatus(1);

        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario creado con éxito.");

        return "redirect:/usuarios";
    }

    // DETAILS
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);

        if (usuario != null) {
            // Cargar el rol y empleado para mostrarlos por su ID
            rolService.buscarPorId(usuario.getIdRol())
                    .ifPresent(rol -> model.addAttribute("rol", rol));
            empleadoService.buscarPorId(usuario.getIdEmpleado())
                    .ifPresent(empleado -> model.addAttribute("empleado", empleado));
        }
        return "usuario/details";
    }

    // EDIT (GET para mostrar el formulario de edición)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        return "usuario/edit";
    }

    // UPDATE (POST para procesar el formulario de edición)
    @PostMapping("/update")
    public String update(@RequestParam Integer id,
                         @RequestParam Integer idRol,
                         @RequestParam Integer idEmpleado,
                         @RequestParam String nombreUsuario,
                         @RequestParam(required = false) String clave, // La clave puede ser opcional al editar
                         @RequestParam Integer status, // Añadimos el status para poder editarlo
                         RedirectAttributes attributes) {

        Usuario usuarioExistente = usuarioService.buscarPorId(id).orElse(new Usuario());
        usuarioExistente.setIdRol(idRol);
        usuarioExistente.setIdEmpleado(idEmpleado);
        usuarioExistente.setNombreUsuario(nombreUsuario);
        usuarioExistente.setStatus(status); // Actualizamos el status aquí

        if (clave != null && !clave.isEmpty()) {
            usuarioExistente.setClave(passwordEncoder.encode(clave));
        }

        usuarioService.crearOEditar(usuarioExistente);
        attributes.addFlashAttribute("msg", "Usuario modificado correctamente.");

        return "redirect:/usuarios";
    }

    // DELETE (GET para confirmar eliminación)
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "usuario/delete";
    }

    // DELETE (POST para ejecutar eliminación)
    @PostMapping("/delete")
    public String delete(Usuario usuario, RedirectAttributes attributes) {
        usuarioService.eliminarPorId(usuario.getId());
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
}