package org.esfe.servicios.interfaces;

import org.esfe.modelos.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEmpresaService {
    Page<Empresa> buscarTodosPaginados(Pageable pageable);

    List<Empresa> obtenerTodos();

    Optional<Empresa> buscarPorId(Integer id);

    Empresa crearOEditar(Empresa empresa);

    void eliminarPorId(Integer id);
}
