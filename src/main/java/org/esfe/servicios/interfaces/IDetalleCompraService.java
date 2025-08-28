package org.esfe.servicios.interfaces;

import org.esfe.modelos.DetalleCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDetalleCompraService {
    Page<DetalleCompra> buscarTodosPaginados(Pageable pageable);

    List<DetalleCompra> obtenerTodos();

    Optional<DetalleCompra> buscarPorId(Long id);

    DetalleCompra crearOEditar(DetalleCompra detalleCompra);

    void eliminarPorId(Long id);


}
