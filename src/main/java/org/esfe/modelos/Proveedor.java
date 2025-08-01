package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "proveedores")
public class Proveedor {
   @Id
   @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

   @NotBlank (message = "El municipio es requerido")
    private Integer idMunicipio;

    @NotBlank (message = "El nombre es requerido")
    private String nombre;

    @NotBlank (message = "El telefono es requerido")
    private String telefono;

    @NotBlank (message = "El email es requerido")
    private String email;

    @NotBlank (message = "El detalle es requerido")
    private String detalle;

    @NotBlank (message = "La direccion es requerido")
    private String direccion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
