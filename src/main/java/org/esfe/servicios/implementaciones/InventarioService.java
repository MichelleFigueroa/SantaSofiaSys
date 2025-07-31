package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Inventario;
import org.esfe.repositorios.IInventarioRepository;
import org.esfe.servicios.interfaces.IInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService implements IInventarioService {
    @Autowired
    private IInventarioRepository inventarioRepository;

    @Override
    public Page<Inventario> buscarTodosPaginados(Pageable pageable) {
        return inventarioRepository.findAll(pageable);
    }

    @Override
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    @Override
    public Optional<Inventario> buscarPorId(Integer id) {
        return inventarioRepository.findById(id);
    }

    @Override
    public Inventario crearOEditar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public void eliminarPorId(Integer id) {
        inventarioRepository.deleteById(id);
    }
}
