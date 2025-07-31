package org.esfe.servicios.interfaces;

import org.esfe.modelos.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMarcaService {
    Page<Marca> buscarTodosPaginados(Pageable pageable);

    List<Marca> obtenerTodos();

    Optional<Marca> buscarPorId(Integer id);

    Marca crearOEditar(Marca marca);

    void eliminarPorId(Integer id);
}
