package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Distrito;
import org.esfe.repositorios.IDistritoRepository;
import org.esfe.servicios.interfaces.IDistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistritoService implements IDistritoService {

    @Autowired
    private IDistritoRepository distritoRepository;

    @Override
    public Page<Distrito> buscarTodosPaginados(Pageable pageable) {
        return distritoRepository.findAll(pageable);
    }

    @Override
    public List<Distrito> obtenerTodos() {
        return distritoRepository.findAll();
    }

    @Override
    public Optional<Distrito> buscarPorId(Integer id) {
        return distritoRepository.findById(id);
    }

    @Override
    public Distrito crearOEditar(Distrito distrito) {
        return distritoRepository.save(distrito);
    }

    @Override
    public void eliminarPorId(Integer id) {
        distritoRepository.deleteById(id);
    }
}
