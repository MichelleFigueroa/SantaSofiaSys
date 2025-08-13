package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Distrito {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

         @NotBlank(message = "El nombre del distrito es obligatorio")
          private String nombre;
        @NotNull(message = "El municipio es obligatorio")

        private Integer idMunicipio;
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

    }

