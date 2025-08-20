package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Departamento;
import org.esfe.repositorios.IDepartamentoRepository;
import org.esfe.servicios.interfaces.IDepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService implements IDepartamentoService {

    @Autowired
    private IDepartamentoRepository departamentoRepository;

    @Override
    public Page<Departamento> buscarTodosPaginados(Pageable pageable) {
        return departamentoRepository.findAll(pageable);
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Optional<Departamento> buscarPorId(Integer id) {
        return departamentoRepository.findById(id);
    }

    @Override
    public Departamento crearOEditar(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public void eliminarPorId(Integer id) {
        departamentoRepository.deleteById(id);
    }
}