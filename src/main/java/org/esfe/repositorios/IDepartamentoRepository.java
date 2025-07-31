package org.esfe.repositorios;

import org.esfe.modelos.Departamento;
import org.esfe.modelos.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartamentoRepository extends JpaRepository<Departamento, Integer> {
}
