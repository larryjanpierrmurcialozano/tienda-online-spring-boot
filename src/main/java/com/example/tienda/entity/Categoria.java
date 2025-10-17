package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * ENTIDAD CATEGORIA
 * Tiene relación N:M con Producto (lado inverso de la relación)
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría - debe ser único
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;

    /**
     * RELACIÓN N:M CON PRODUCTOS (lado inverso)
     * mappedBy = "categorias" significa que la relación está definida 
     * en el lado Producto, en el atributo llamado "categorias"
     */
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private Set<Producto> productos = new HashSet<>();

    // ===== CONSTRUCTORES =====

    public Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // ===== MÉTODOS ÚTILES =====

    /**
     * Obtener la cantidad de productos en esta categoría
     */
    public int getCantidadProductos() {
        return productos.size();
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidadProductos=" + getCantidadProductos() +
                '}';
    }
}
