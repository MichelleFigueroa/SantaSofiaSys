package org.esfe.servicios.interfaces;

import org.esfe.modelos.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProveedorService {
    Page<Proveedor> buscarTodosPaginados(Pageable pageable);

    List<Proveedor> obtenerTodos();

    Optional<Proveedor> buscarPorId (Integer id);

    Proveedor createOrEditone (Proveedor proveedor);

    void eliminarPorld (Integer proveedor);
}