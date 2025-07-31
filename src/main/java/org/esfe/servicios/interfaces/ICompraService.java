package org.esfe.servicios.interfaces;

import org.esfe.modelos.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICompraService {

    Page<Compra> buscarTodosPaginados(Pageable pageable);

    List<Compra> obtenerTodos();

    Optional<Compra> buscarPorId(Integer id);

    Compra crearOEditar(Compra compra);

    void eliminarPorId(Integer id);

}
