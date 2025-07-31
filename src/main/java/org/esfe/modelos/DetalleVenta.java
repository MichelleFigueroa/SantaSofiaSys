package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;

@Entity
@Table(name = "detalleVentas")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NotBlank(message = "El producto es requerido")
    private Integer idProducto;

    @NotBlank(message = "La venta es requerida")
    private BigInteger idVenta;

    @NotBlank(message = "La cantidad es requerida")
    private short cantidad;

    @NotBlank(message = "El precio es requerido")
    private double precio;

    @NotBlank(message = "El subtotal es requerido")
    private double subTotal;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public BigInteger getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(BigInteger idVenta) {
        this.idVenta = idVenta;
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
