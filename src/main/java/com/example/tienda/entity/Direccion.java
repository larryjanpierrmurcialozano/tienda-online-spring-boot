package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * ENTIDAD DIRECCIÓN
 * Tiene relación 1:1 con Cliente
 * Esta entidad POSEE la clave foránea (cliente_id)
 */
@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "La calle no puede estar vacía")
    private String calle;

    @Column(nullable = false)
    @NotBlank(message = "La ciudad no puede estar vacía")
    private String ciudad;

    @Column(nullable = false)
    @NotBlank(message = "El país no puede estar vacío")
    private String pais;

    @Column(nullable = false)
    @NotBlank(message = "El código postal no puede estar vacío")
    private String zip;

    /**
     * RELACIÓN 1:1 CON CLIENTE
     * @JoinColumn(name="cliente_id") crea la columna cliente_id en esta tabla
     * unique = true garantiza que cada dirección pertenece a UN SOLO cliente
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", unique = true)
    private Cliente cliente;

    // ===== CONSTRUCTORES =====

    public Direccion() {}

    public Direccion(String calle, String ciudad, String pais, String zip) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.pais = pais;
        this.zip = zip;
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", calle='" + calle + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
