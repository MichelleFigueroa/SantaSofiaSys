package org.esfe.servicios.implementaciones;

import org.esfe.modelos.*;
import org.esfe.repositorios.IVentaRepository;
import org.esfe.servicios.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {
    @Autowired
    private IVentaRepository ventaRepository;

    @Override
    public Page<Venta> buscarTodosPaginados(Pageable pageable) {
        return ventaRepository.findAll(pageable);
    }

    @Override
    public List<Venta> obtenerTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta crearOEditar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminarPorId(Long id) {
        ventaRepository.deleteById(id);
    }
}
