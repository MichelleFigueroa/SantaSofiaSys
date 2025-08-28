package org.esfe.controladores;


import jakarta.persistence.Id;
import org.esfe.modelos.Distrito;
import org.esfe.modelos.Proveedor;
import org.esfe.servicios.interfaces.IDistritoService;
import org.esfe.servicios.interfaces.IProveedorService;
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
@RequestMapping("/proveedores")
public class ProveedorController {
    @Autowired
    private IProveedorService proveedorService;
    @Autowired
    private IDistritoService distritoService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Proveedor> proveedores = proveedorService.buscarTodosPaginados(pageable);
        model.addAttribute("proveedores", proveedores);

        int totalPages = proveedores.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "proveedor/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("distritos", distritoService.obtenerTodos());
        return "proveedor/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Integer id,   // 👈 recibimos también el id
                       @RequestParam Integer distritoId,
                       @RequestParam String nombre,
                       @RequestParam String telefono,
                       @RequestParam String email,
                       @RequestParam String detalle,
                       @RequestParam String direccion,
                       RedirectAttributes attributes) {

        Distrito distrito = distritoService.buscarPorId(distritoId).orElse(null);

        if (distrito != null) {
            Proveedor proveedor;

            if (id != null) {
                // si viene id, cargamos el proveedor de la BD (modo editar)
                proveedor = proveedorService.buscarPorId(id).orElse(new Proveedor());
            } else {
                //  si no hay id, es uno nuevo
                proveedor = new Proveedor();
            }

            // seteamos datos
            proveedor.setDistrito(distrito);
            proveedor.setNombre(nombre);
            proveedor.setTelefono(telefono);
            proveedor.setEmail(email);
            proveedor.setDetalle(detalle);
            proveedor.setDireccion(direccion);

            // guardar
            proveedorService.createOrEditone(proveedor);

            // mensaje
            String msg = (id != null) ? "Proveedor actualizado correctamente"
                    : "Proveedor creado correctamente";
            attributes.addFlashAttribute("msg", msg);
        }

        return "redirect:/proveedores";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id).get();
        model.addAttribute("proveedor", proveedor);
        return "proveedor/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id).get();
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("distritos", distritoService.obtenerTodos());
        return "proveedor/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        proveedorService.eliminarPorld(id);
        attributes.addFlashAttribute("msg", "Proveedor eliminado correctamente");
        return "redirect:/proveedores";
    }

    @PostMapping("/delete")
    public String delete(Proveedor proveedor, RedirectAttributes attributes) {
        proveedorService.eliminarPorld(proveedor.getId());
        attributes.addFlashAttribute("msg", "Proveedor eliminado correctamente");
        return "redirect:/proveedores";
    }
}
