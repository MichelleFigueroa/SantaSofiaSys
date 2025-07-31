package org.esfe.servicios.interfaces;

import org.esfe.modelos.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMunicipioService {
    Page<Municipio> buscarTodosPaginados(Pageable pageable);

    List<Municipio> obtenerTodos();

    Optional<Municipio> buscarPorId(Integer id);

    Municipio crearOEditar(Municipio municipio);

    void eliminarPorId(Integer id);
}