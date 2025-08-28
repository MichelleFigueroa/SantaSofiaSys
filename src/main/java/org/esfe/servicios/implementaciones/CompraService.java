package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Compra;
import org.esfe.repositorios.ICompraRepository;
import org.esfe.servicios.interfaces.ICompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraService implements ICompraService {
    @Autowired
    private ICompraRepository compraRepository;

    @Override
    public Page<Compra> buscarTodosPaginados(Pageable pageable) {
        return compraRepository.findAll(pageable);
    }

    @Override
    public List<Compra> obtenerTodos() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public Compra crearOEditar(Compra compra) {
        return compraRepository.save(compra);
    }
    @Override
    public void eliminarPorId(Long id) {
        compraRepository.deleteById(id);
    }

}
