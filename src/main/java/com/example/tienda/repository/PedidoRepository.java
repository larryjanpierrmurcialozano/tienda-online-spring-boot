package com.example.tienda.repository;

import com.example.tienda.entity.Pedido;
import com.example.tienda.entity.Cliente;
import com.example.tienda.dto.TotalClienteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORIO PEDIDO
 * Con consultas específicas para gestión de pedidos
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Buscar pedidos de un cliente específico
     */
    List<Pedido> findByClienteOrderByFechaDesc(Cliente cliente);

    /**
     * Buscar pedidos por ID de cliente
     */
    List<Pedido> findByClienteIdOrderByFechaDesc(Long clienteId);

    /**
     * CONSULTA CLAVE: Obtener pedido CON sus items cargados
     * @EntityGraph evita el problema N+1 al cargar los items
     */
    @EntityGraph(attributePaths = {"items", "items.producto"})
    Optional<Pedido> findWithItemsById(Long id);

    /**
     * Buscar pedidos por estado
     */
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    /**
     * Buscar pedidos en un rango de fechas
     */
    List<Pedido> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * CONSULTA IMPORTANTE: Calcular total gastado por cliente usando DTO
     * Esta consulta demuestra cómo usar proyecciones para optimizar consultas
     */
    @Query("SELECT new com.example.tienda.dto.TotalClienteDTO(c.id, c.nombre, COALESCE(SUM(p.total), 0)) " +
           "FROM Cliente c LEFT JOIN c.pedidos p " +
           "WHERE p.estado != 'CANCELADO' OR p.estado IS NULL " +
           "GROUP BY c.id, c.nombre " +
           "ORDER BY COALESCE(SUM(p.total), 0) DESC")
    List<TotalClienteDTO> findTotalPorCliente();

    /**
     * Buscar pedidos por cliente con valor mínimo
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.total >= :totalMinimo")
    List<Pedido> findPedidosGrandesDeCliente(@Param("clienteId") Long clienteId,
                                           @Param("totalMinimo") BigDecimal totalMinimo);

    /**
     * Contar pedidos por estado
     */
    long countByEstado(Pedido.EstadoPedido estado);
}
