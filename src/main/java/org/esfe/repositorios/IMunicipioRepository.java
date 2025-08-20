package org.esfe.repositorios;

import org.esfe.modelos.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List; // <<<<<<<< ¡AQUÍ ESTÁ LA IMPORTACIÓN QUE FALTA!

public interface IMunicipioRepository extends JpaRepository<Municipio, Integer> {

    // Método para buscar todos los municipios y cargar eagermente su departamento
    // Si realmente lo necesitas aparte de la paginación, déjalo.
    @Query("SELECT m FROM Municipio m JOIN FETCH m.departamento")
    List<Municipio> findAllWithDepartamento();

    // Modifica el método existente para paginación para incluir el FETCH
    // Esta sobrescribe el findAll(Pageable) predeterminado de JpaRepository
    @Query(value = "SELECT m FROM Municipio m JOIN FETCH m.departamento",
            countQuery = "SELECT count(m) FROM Municipio m") // Es importante tener un countQuery si usas JOIN FETCH
    Page<Municipio> findAll(Pageable pageable);
}