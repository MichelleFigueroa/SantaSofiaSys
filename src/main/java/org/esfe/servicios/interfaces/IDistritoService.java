package org.esfe.servicios.interfaces;

import org.esfe.modelos.Distrito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDistritoService {
    Page<Distrito> buscarTodosPaginados(Pageable pageable);

    List<Distrito> obtenerTodos();

    Optional<Distrito> buscarPorId(Integer id);

    Distrito crearOEditar(Distrito distrito);

    void eliminarPorId(Integer id);
}
