package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD PEDIDO
 * Tiene relación N:1 con Cliente (muchos pedidos pueden pertenecer a un cliente)
 * Tiene relación 1:N con ItemPedido (un pedido puede tener muchos items)
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora del pedido
     */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Estado del pedido: NUEVO, PAGADO, ENVIADO, CANCELADO
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Guarda el texto del enum, no el número
    private EstadoPedido estado;

    /**
     * Total del pedido - se calcula sumando todos los items
     */
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @Positive(message = "El total debe ser mayor a cero")
    private BigDecimal total;

    /**
     * RELACIÓN N:1 CON CLIENTE
     * Muchos pedidos pueden pertenecer a un cliente
     * JoinColumn crea la columna cliente_id en esta tabla
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * RELACIÓN 1:N CON ITEMPEDIDO
     * Un pedido puede tener muchos items
     * mappedBy = "pedido" significa que la FK está en la tabla items_pedido
     */
    @OneToMany(mappedBy = "pedido", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<ItemPedido> items = new ArrayList<>();

    // ===== ENUM PARA ESTADOS =====
    public enum EstadoPedido {
        NUEVO, PAGADO, ENVIADO, CANCELADO
    }

    // ===== CONSTRUCTORES =====

    public Pedido() {
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.NUEVO;
        this.total = BigDecimal.ZERO;
    }

    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    // ===== MÉTODOS HELPER PARA ITEMS =====

    /**
     * Agregar un item al pedido y mantener la relación bidireccional
     */
    public void addItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
        calcularTotal(); // Recalcula el total cuando se agrega un item
    }

    /**
     * Remover un item del pedido
     */
    public void removeItem(ItemPedido item) {
        items.remove(item);
        item.setPedido(null);
        calcularTotal(); // Recalcula el total cuando se quita un item
    }

    /**
     * Calcular el total del pedido sumando todos los items
     * total = suma de (cantidad * precioUnitario) de cada item
     */
    public void calcularTotal() {
        this.total = items.stream()
                .map(item -> item.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verificar si el pedido se puede cancelar
     */
    public boolean puedeSerCancelado() {
        return estado == EstadoPedido.NUEVO || estado == EstadoPedido.PAGADO;
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", total=" + total +
                ", itemsCount=" + items.size() +
                '}';
    }
}
