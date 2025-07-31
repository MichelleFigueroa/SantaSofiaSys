package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventarios")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El producto es requerido")
    private Integer idProducto;

    @NotBlank(message = "El stock actual es requerido")
    private int stockActual;

    @NotBlank(message = "El stock minimo es requerido")
    private short stockMinimo;

    @NotBlank(message = "La fecha de compra es requerida")
    private LocalDateTime fechaCompra;

    @NotBlank(message = "La fecha actual es requerida")
    private LocalDateTime fechaActual;

    @NotBlank(message = "El movimiento es requerido")
    private String movimiento;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
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

