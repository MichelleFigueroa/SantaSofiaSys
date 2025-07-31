package org.esfe.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_marca")
    private int idMarca;

    @Column(name = "id_categoria")
    private int idCategoria;

    private String nombre;

    private double precio;

    @Column(name = "cantidad")
    private int cantidad;
}
