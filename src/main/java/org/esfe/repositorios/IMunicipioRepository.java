package org.esfe.repositorios;

import org.esfe.modelos.Municipio;
import org.esfe.modelos.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMunicipioRepository extends JpaRepository<Municipio, Integer> {
}
