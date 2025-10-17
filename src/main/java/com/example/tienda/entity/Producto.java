package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ENTIDAD PRODUCTO
 * Tiene relación N:M con Categoria (un producto puede tener varias categorías)
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto - debe ser único
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    /**
     * Precio del producto - usando BigDecimal para precisión monetaria
     */
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    /**
     * Stock disponible del producto
     */
    @Column(nullable = false)
    @NotNull
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    /**
     * RELACIÓN N:M CON CATEGORIAS (Many-to-Many "pura")
     * Un producto puede tener múltiples categorías
     * Una categoría puede tener múltiples productos
     * @JoinTable crea una tabla intermedia producto_categoria
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "producto_categoria", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "producto_id"), // FK hacia producto
        inverseJoinColumns = @JoinColumn(name = "categoria_id") // FK hacia categoria
    )
    private Set<Categoria> categorias = new HashSet<>();

    // ===== CONSTRUCTORES =====

    public Producto() {}

    public Producto(String nombre, BigDecimal precio, Integer stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // ===== MÉTODOS HELPER PARA CATEGORÍAS =====

    /**
     * Agregar una categoría al producto
     * Mantiene la relación bidireccional
     */
    public void addCategoria(Categoria categoria) {
        categorias.add(categoria);
        categoria.getProductos().add(this);
    }

    /**
     * Remover una categoría del producto
     */
    public void removeCategoria(Categoria categoria) {
        categorias.remove(categoria);
        categoria.getProductos().remove(this);
    }

    /**
     * Verificar si hay stock suficiente
     */
    public boolean tieneStockSuficiente(Integer cantidadSolicitada) {
        return stock >= cantidadSolicitada;
    }

    /**
     * Reducir el stock del producto
     */
    public void reducirStock(Integer cantidad) {
        if (!tieneStockSuficiente(cantidad)) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + stock + ", solicitado: " + cantidad);
        }
        this.stock -= cantidad;
    }

    /**
     * Aumentar el stock del producto (por ejemplo, al cancelar un pedido)
     */
    public void aumentarStock(Integer cantidad) {
        this.stock += cantidad;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }
}
