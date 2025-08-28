package org.esfe.servicios.interfaces;

import org.esfe.modelos.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
    Page<Venta> buscarTodosPaginados(Pageable pageable);

    List<Venta> obtenerTodos();

    Optional<Venta> buscarPorId(Long id);

    Venta crearOEditar(Venta venta);

    void eliminarPorId(Long id);
}
