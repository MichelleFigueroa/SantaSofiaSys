package org.esfe.controladores;


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
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Proveedor> proveedores = proveedorService.buscarTodosPaginados(pageable);
        model.addAttribute("proveedores", proveedores);

        int totalPages = proveedores.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "proveedor/index";

    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("distrito", distritoService.obtenerTodos());
        return "proveedor/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer distritoId,
                       @RequestParam String nombre, @RequestParam String telefono,
                       @RequestParam String email,
                       @RequestParam String detalle, @RequestParam String direccion,
                       RedirectAttributes attributes) {
        Distrito distrito = distritoService.buscarPorId(distritoId).get();

        if (distrito != null ) {
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombre);
            proveedor.setDistrito(distrito);
            proveedor.setTelefono(telefono);
            proveedor.setEmail(email);
            proveedor.setDetalle(detalle);
            proveedor.setDireccion(direccion);

            proveedorService.createOrEditone(proveedor);
            attributes.addFlashAttribute("msg", "Proveedor creado correctamente");

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
        model.addAttribute("proveedor", proveedorService.obtenerTodos());
        model.addAttribute("distrito", distritoService.obtenerTodos());
        return "proveedor/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Proveedor proveedor = proveedorService.buscarPorId(id).get();
        model.addAttribute("proveedor", proveedor);
        return "proveedor/delete";
    }


    @PostMapping("/delete")
    public String delete(Proveedor proveedor, RedirectAttributes attributes) {
        proveedorService.eliminarPorld(proveedor.getId());
        attributes.addFlashAttribute("msg", "Proveedor eliminado correctamente");
        return "redirect:/proveedores";

    }
}