package org.esfe.modelos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El usuario es requerido")
    private Integer idUsuario;

    @NotBlank(message = "El proveedor es requerido")
    private Integer idProveedor;

    @NotBlank(message = "El correlativo es requerido")
    private BigInteger correlativo;

    @NotBlank(message = "La fecha es requerida")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El Total es requerido")
    private Double total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public BigInteger getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(BigInteger correlativo) {
        this.correlativo = correlativo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
