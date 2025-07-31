package org.esfe.modelos;

import jakarta.persistence.*;

@Entity
    @Table(name = "marcas")
    public class Marca {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String nombre;
    }
