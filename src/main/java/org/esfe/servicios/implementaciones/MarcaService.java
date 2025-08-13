package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Marca;
import org.esfe.repositorios.IMarcaRepository;
import org.esfe.servicios.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class  MarcaService implements IMarcaService {
    @Autowired
     private IMarcaRepository marcaRepository;

    @Override
    public Page<Marca> buscarTodosPaginados(Pageable pageable) {

        return marcaRepository.findAll(pageable);
    }
    @Override
    public List<Marca> obtenerTodos() {

        return marcaRepository.findAll();
    }

    @Override
    public Optional<Marca> buscarPorId(Integer id) {

        return marcaRepository.findById(id);
    }

    @Override
    public Marca crearOEditar(Marca marca) {

        return marcaRepository.save(marca);
    }

    @Override
    public void eliminarPorId(Integer id) {

        marcaRepository.deleteById(id);
    }
}
