package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
// Ya no necesitas jakarta.validation.constraints.Size si no hay "descripcion"

@Entity
@Table(name = "departamentos")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Mantienes Integer si así lo tienes en tu DB

    @NotBlank(message = "El nombre del departamento es requerido")
    // @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.") // Si no lo tenías antes, quítalo
    private String nombre;

    // --- CONSTRUCTORES ---
    public Departamento() {
    }

    public Departamento(String nombre) { // Constructor ajustado para solo nombre
        this.nombre = nombre;
    }

    // --- GETTERS Y SETTERS ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // --- OPCIONAL: toString() para depuración ---
    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}