package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventarios")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El producto es requerido")
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @NotNull(message = "El stock actual es requerido")
    private int stockActual;

    @NotNull(message = "El stock minimo es requerido")
    private short stockMinimo;

//    @NotNull(message = "La fecha de compra es requerida")
    private LocalDateTime fechaCompra;

    @NotNull(message = "La fecha actual es requerida")
    private LocalDateTime fechaActual;

    @NotBlank(message = "El movimiento es requerido")
    private String movimiento;


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

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public short getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(short stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDateTime getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(LocalDateTime fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }
}

