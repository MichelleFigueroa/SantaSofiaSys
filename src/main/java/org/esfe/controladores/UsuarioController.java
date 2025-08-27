package org.esfe.controladores;

import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    // INDEX con paginación
    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuarios = usuarioService.buscarTodosPaginados(pageable);
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

    // CREATE
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/create";
    }

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
        usuario.setClave(clave);

        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario creado con éxito.");

        return "redirect:/usuarios";
    }

    // DETAILS
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "usuario/details";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "usuario/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer id,
                         @RequestParam Integer idRol,
                         @RequestParam Integer idEmpleado,
                         @RequestParam String nombreUsuario,
                         @RequestParam String clave,
                         RedirectAttributes attributes) {

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setIdRol(idRol);
        usuario.setIdEmpleado(idEmpleado);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClave(clave);

        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario modificado correctamente.");

        return "redirect:/usuarios";
    }

    // DELETE
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "usuario/delete";
    }

    @PostMapping("/delete")
    public String delete(Usuario usuario, RedirectAttributes attributes) {
        usuarioService.eliminarPorId(usuario.getId());
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
}
