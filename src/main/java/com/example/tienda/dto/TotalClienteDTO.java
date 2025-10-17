package com.example.tienda.dto;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para mostrar el total gastado por cliente
 * Los DTOs se usan para transferir datos de forma optimizada,
 * evitando cargar entidades completas cuando solo necesitas ciertos campos
 */
public class TotalClienteDTO {

    private Long clienteId;
    private String clienteNombre;
    private BigDecimal totalGastado;

    // ===== CONSTRUCTORES =====

    /**
     * Constructor para la consulta JPQL
     * Este constructor es usado por la consulta en PedidoRepository
     */
    public TotalClienteDTO(Long clienteId, String clienteNombre, BigDecimal totalGastado) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.totalGastado = totalGastado;
    }

    // ===== GETTERS Y SETTERS =====

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }

    @Override
    public String toString() {
        return "TotalClienteDTO{" +
                "clienteId=" + clienteId +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", totalGastado=" + totalGastado +
                '}';
    }
}
