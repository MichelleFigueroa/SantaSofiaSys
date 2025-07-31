package org.esfe.servicios.interfaces;

import org.esfe.modelos.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    Page<Cliente> buscarTodosPaginados(Pageable pageable);

    List<Cliente> obtenerTodos();

    Optional<Cliente> buscarPorId(Integer id);

    Cliente crearOEditar(Cliente cliente);

    void eliminarPorId(Integer id);
}