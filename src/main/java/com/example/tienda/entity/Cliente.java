package com.example.tienda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD CLIENTE
 * Representa un cliente de la tienda online
 * Esta es una clase que se convierte en una tabla en la base de datos
 */
@Entity // Le dice a JPA que esta clase es una entidad (tabla en BD)
@Table(name = "clientes") // Nombre de la tabla en la base de datos
public class Cliente {

    // ===== ATRIBUTOS BÁSICOS =====
    
    /**
     * ID único del cliente - clave primaria
     * GeneratedValue hace que la BD genere el ID automáticamente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del cliente - no puede ser nulo ni vacío
     */
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    /**
     * Email del cliente - debe ser único y válido
     */
    @Column(nullable = false, unique = true)
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    // ===== RELACIONES CON OTRAS ENTIDADES =====

    /**
     * RELACIÓN 1:1 CON DIRECCIÓN
     * mappedBy = "cliente" significa que la FK está en la tabla direcciones
     * cascade = ALL: si guardo/elimino cliente, también afecta su dirección
     * orphanRemoval = true: si quito la dirección, se elimina de la BD
     * fetch = LAZY: no carga la dirección hasta que la necesite (optimización)
     */
    @OneToOne(mappedBy = "cliente", 
              cascade = CascadeType.ALL, 
              orphanRemoval = true, 
              fetch = FetchType.LAZY)
    private Direccion direccion;

    /**
     * RELACIÓN 1:N CON PEDIDOS
     * Un cliente puede tener muchos pedidos
     * mappedBy = "cliente" significa que la FK cliente_id está en la tabla pedidos
     */
    @OneToMany(mappedBy = "cliente", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    // ===== CONSTRUCTORES =====

    /**
     * Constructor vacío - requerido por JPA
     */
    public Cliente() {}

    /**
     * Constructor con parámetros básicos
     */
    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    // ===== MÉTODOS HELPER PARA RELACIONES =====

    /**
     * Método helper para establecer la dirección bidireccional
     * Cuando asigno una dirección al cliente, también asigno el cliente a la dirección
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
        if (direccion != null) {
            direccion.setCliente(this);
        }
    }

    /**
     * Método helper para agregar un pedido
     * Mantiene la relación bidireccional sincronizada
     */
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setCliente(this);
    }

    /**
     * Método helper para remover un pedido
     */
    public void removePedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setCliente(null);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    // ===== MÉTODOS ÚTILES =====

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
