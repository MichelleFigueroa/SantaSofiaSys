package org.esfe.servicios.implementaciones;

import org.esfe.modelos.DetalleCompra;
import org.esfe.repositorios.IDetalleCompraRespository;
import org.esfe.servicios.interfaces.IDetalleCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCompraService implements IDetalleCompraService {
    @Autowired
    private IDetalleCompraRespository detalleCompraRespository;

    @Override
    public Page<DetalleCompra> buscarTodosPaginados(Pageable pageable) {
        return detalleCompraRespository.findAll(pageable);
    }

    @Override
    public List<DetalleCompra> obtenerTodos() {
        return detalleCompraRespository.findAll();
    }

    @Override
    public Optional<DetalleCompra> buscarPorId(Integer id) {
        return detalleCompraRespository.findById(id);
    }

    @Override
    public DetalleCompra crearOEditar(DetalleCompra detalleCompra) {
        return detalleCompraRespository.save(detalleCompra);
    }

    @Override
    public void eliminarPorId(Integer id) {
        detalleCompraRespository.deleteById(id);
    }
}
