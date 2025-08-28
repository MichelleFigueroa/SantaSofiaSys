package org.esfe.controladores;


import org.esfe.modelos.*;
import org.esfe.servicios.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    private ICompraService compraService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IProveedorService proveedorService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IInventarioService inventarioService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Compra> compras = compraService.buscarTodosPaginados(pageable);
        model.addAttribute("compras", compras);

        int totalPages = compras.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "compra/index";

    }
    @GetMapping("/create")
    public String create(Compra compra, Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("inventarios", inventarioService.obtenerTodos());
        return "compra/create";
    }

    @PostMapping("/save")
    public String save(Compra compra, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute(compra);
            attributes.addFlashAttribute("error", "error no se pudo guardar debido a un error.");
            return "compra/create";
        }

        compra.setFechaHora(LocalDateTime.now());

        double totalCompra = 0;

        for (DetalleCompra detalle : compra.getDetalles()){
            detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecio());
            totalCompra += detalle.getSubtotal();

            Producto producto = productoService.buscarPorId(detalle.getProducto().getId()).get();

            Inventario movimiento = new Inventario();
            movimiento.setProducto(producto);
            movimiento.setStockActual(producto.getCantidad());
            movimiento.setFechaActual(LocalDateTime.now());
            movimiento.setStockMinimo((short)5);
            movimiento.setMovimiento("COMPRA_ID_" + compra.getId());
            inventarioService.crearOEditar(movimiento);

            detalle.setCompra(compra);
        }

        compra.setTotal(totalCompra);

        compraService.crearOEditar(compra);
        attributes.addFlashAttribute("msg", "Compra creada correctamente");
        return "redirect:/compras";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Compra compra = compraService.buscarPorId(id).get();
        model.addAttribute("compra", compra);
        return "compra/details";
    }

}
