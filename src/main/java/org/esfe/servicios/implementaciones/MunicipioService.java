package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Municipio;
import org.esfe.repositorios.IMunicipioRepository;
import org.esfe.servicios.interfaces.IMunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService implements IMunicipioService {
    private IMunicipioRepository municipioRepository;

    @Override
    public Page<Municipio> buscarTodosPaginados(Pageable pageable) {
        return municipioRepository.findAll(pageable);
    }

    @Override
    public List<Municipio> obtenerTodos() {
        return municipioRepository.findAll();
    }

    @Override
    public Optional<Municipio> buscarPorId(Integer id) {
        return municipioRepository.findById(id);
    }

    @Override
    public Municipio crearOEditar(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    @Override
    public void eliminarPorId(Integer id) {
        municipioRepository.deleteById(id);
    }
}