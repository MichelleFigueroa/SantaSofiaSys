package org.esfe.servicios.interfaces;

import org.esfe.modelos.DetalleVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    Page<DetalleVenta> buscarTodosPaginados(Pageable pageable);

    List<DetalleVenta> obtenerTodos();

    Optional<DetalleVenta> buscarPorId(Integer id);

    DetalleVenta crearOEditar(DetalleVenta detalleVenta);

    void eliminarPorId(Integer id);
}
