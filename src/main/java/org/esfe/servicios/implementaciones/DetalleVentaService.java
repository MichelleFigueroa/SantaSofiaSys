package org.esfe.servicios.implementaciones;

import org.esfe.modelos.DetalleVenta;
import org.esfe.repositorios.IDetalleVentaRepository;
import org.esfe.servicios.interfaces.IDetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService implements IDetalleVentaService {
    @Autowired
    private IDetalleVentaRepository detalleVentaRepository;

    @Override
    public Page<DetalleVenta> buscarTodosPaginados(Pageable pageable) {
        return detalleVentaRepository.findAll(pageable);
    }

    @Override
    public List<DetalleVenta> obtenerTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public DetalleVenta crearOEditar(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminarPorId(Integer id) {
        detalleVentaRepository.deleteById(id);
    }
}
