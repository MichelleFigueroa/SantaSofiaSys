package org.esfe.repositorios;

import org.esfe.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

}

