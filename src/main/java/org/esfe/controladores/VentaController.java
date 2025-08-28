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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private IVentaService ventaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IInventarioService inventarioService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Venta> ventas = ventaService.buscarTodosPaginados(pageable);
        model.addAttribute("ventas", ventas);

        int totalPages = ventas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "venta/index";
    }

    @GetMapping("/create")
    public String create (Venta venta, Model model){
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("inventarios", inventarioService.obtenerTodos());
        return "venta/create";
    }

    @PostMapping("/save")
    public String save(Venta venta, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(venta);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "venta/create";
        }

        venta.setFechaHora(LocalDateTime.now());
        venta.setEstado(true);

//        Cliente cliente = clienteService.buscarPorId(venta.getCliente().getId()).get();
//        Usuario usuario = usuarioService.buscarPorId(venta.getUsuario().getId()).get();
//        venta.setCliente(cliente);
//        venta.setUsuario(usuario);
//
//        venta.setDetalles(new ArrayList<>());
        double totalVenta = 0;
//
        for (DetalleVenta detalle : venta.getDetalles()){
            detalle.setSubTotal(detalle.getCantidad() * detalle.getPrecio());
            totalVenta += detalle.getSubTotal();
//
            Producto producto = productoService.buscarPorId(detalle.getProducto().getId()).get();
//            producto.setCantidad((int)(producto.getCantidad() - detalle.getCantidad()));
//            productoService.crearOEditar(producto);
//
            Inventario movimiento = new Inventario();
            movimiento.setProducto(producto);
            movimiento.setStockActual(producto.getCantidad());
            movimiento.setFechaActual(LocalDateTime.now());
            movimiento.setStockMinimo((short)5);
            movimiento.setMovimiento("VENTA_ID_" + venta.getId());
            inventarioService.crearOEditar(movimiento);

            detalle.setVenta(venta);
//
//            venta.getDetalles().add(detalle);
        }
//
//        venta.setDetalles(detalles);
//
        venta.setTotal(totalVenta);

        ventaService.crearOEditar(venta);
        attributes.addFlashAttribute("msg", "Venta creada con exito.");
        return "redirect:/ventas";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model){
        Venta venta = ventaService.buscarPorId(id).get();
        model.addAttribute("venta", venta);
        return "venta/details";
    }}
