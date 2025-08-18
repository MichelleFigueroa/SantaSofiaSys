package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;

@Entity
@Table(name = "detalleCompras")
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El producto es requerido")
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @NotBlank(message = "La compra es requerida")
    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @NotBlank(message = "La cantidad es requerido")
    private Short cantidad;

    @NotBlank(message = "El precio es requerido")
    private Double precio;

    @NotBlank(message = "El subtotal es requerido")
    private Double subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

}
