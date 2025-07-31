package org.esfe.repositorios;

import org.esfe.modelos.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpresaRepository extends JpaRepository<Empresa, Integer> {
}
