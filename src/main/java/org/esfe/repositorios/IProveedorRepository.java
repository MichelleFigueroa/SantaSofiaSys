package org.esfe.repositorios;

import org.esfe.modelos.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProveedorRepository extends JpaRepository <Proveedor, Integer> {
}
