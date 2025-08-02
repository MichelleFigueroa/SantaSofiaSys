package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Empleado {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank(message = "El nombre del empleado es obligatorio")
        private String nombre;

        @NotBlank(message = "El apellido del empleado es obligatorio")
        private String apellido;

        @NotBlank(message = "El teléfono del cliente es requerido")
        private String telefono;

        @Email(message = "Debe ser un formato de correo electrónico válido")
        @NotBlank(message = "El correo electrónico del cliente es obligatorio")
        private String email;

        @NotBlank(message = "El DUI del empleado es obligatorio")
        private String dui;


        // Getters y Setters

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

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDui() {
            return dui;
        }

        public void setDui(String dui) {
            this.dui = dui;
        }


        }


