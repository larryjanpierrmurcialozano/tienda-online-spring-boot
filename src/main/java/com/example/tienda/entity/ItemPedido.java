package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * ENTIDAD ITEMPEDIDO - ENTIDAD INTERMEDIA
 * Esta es una entidad intermedia entre Pedido y Producto CON ATRIBUTOS PROPIOS
 * En lugar de usar @JoinTable, creamos una entidad completa porque necesitamos
 * guardar información adicional como cantidad y precio unitario
 */
@Entity
@Table(name = "items_pedido",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_pedido_producto", 
        columnNames = {"pedido_id", "producto_id"}
    )
)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * RELACIÓN N:1 CON PEDIDO
     * Muchos items pueden pertenecer a un pedido
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /**
     * RELACIÓN N:1 CON PRODUCTO
     * Muchos items pueden referenciar al mismo producto (en diferentes pedidos)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * ATRIBUTOS PROPIOS DE LA RELACIÓN
     * Estos son los datos que justifican tener una entidad intermedia
     */
    
    /**
     * Cantidad del producto en este pedido
     */
    @Column(nullable = false)
    @NotNull
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    /**
     * Precio unitario del producto AL MOMENTO DE HACER EL PEDIDO
     * Importante: guardamos el precio histórico, no el precio actual del producto
     */
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @Positive(message = "El precio unitario debe ser mayor a cero")
    private BigDecimal precioUnitario;

    // ===== CONSTRUCTORES =====

    public ItemPedido() {}

    public ItemPedido(Pedido pedido, Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Constructor que toma el precio actual del producto
     */
    public ItemPedido(Pedido pedido, Producto producto, Integer cantidad) {
        this(pedido, producto, cantidad, producto.getPrecio());
    }

    // ===== MÉTODOS ÚTILES =====

    /**
     * Calcular el subtotal de este item
     * subtotal = cantidad * precio unitario
     */
    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Verificar si este item puede ser agregado al pedido
     * (verificar stock disponible)
     */
    public boolean puedeSerAgregado() {
        return producto.tieneStockSuficiente(cantidad);
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + calcularSubtotal() +
                ", producto=" + (producto != null ? producto.getNombre() : "null") +
                '}';
    }
}
