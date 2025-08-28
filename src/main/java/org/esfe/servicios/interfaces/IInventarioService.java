package org.esfe.servicios.interfaces;

import org.esfe.modelos.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IInventarioService {
    Page<Inventario> buscarTodosPaginados(Pageable pageable);

    List<Inventario> obtenerTodos();

    Optional<Inventario> buscarPorId(Long id);

    Inventario crearOEditar(Inventario inventario);

    void eliminarPorId(Long id);
}
