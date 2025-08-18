package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;

@Entity
@Table(name = "detalleVentas")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El producto es requerido")
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @NotBlank(message = "La venta es requerida")
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @NotBlank(message = "La cantidad es requerida")
    private short cantidad;

    @NotBlank(message = "El precio es requerido")
    private double precio;

    @NotBlank(message = "El subtotal es requerido")
    private double subTotal;

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

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public short getCantidad() {
        return cantidad;
    }

    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
