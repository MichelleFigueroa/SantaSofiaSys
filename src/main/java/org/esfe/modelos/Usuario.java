package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;

public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private BigInteger id;

        @NotBlank(message = "El rol es obligatorio")
        private Integer idRol;

        @NotBlank(message = "El empleado es obligatorio")
        private Integer idEmpleado;

        @NotBlank(message = "El nombre de usuario es obligatorio")
        private String nombre;



        public BigInteger getId() {
            return id;
        }

        public void setId(BigInteger id) {
            this.id = id;
        }

        public Integer getIdRol() {
            return idRol;
        }

        public void setIdRol(Integer idRol) {
            this.idRol = idRol;
        }

        public Integer getIdEmpleado() {
            return idEmpleado;
        }

        public void setIdEmpleado(Integer idEmpleado) {
            this.idEmpleado = idEmpleado;
        }
         public String getNombre() {
        return nombre;
         }

        public void setNombre(String nombre) {
        this.nombre = nombre;
        }

    }

