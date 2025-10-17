package com.example.tienda.repository;

import com.example.tienda.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORIO ITEMPEDIDO
 * Para manejar los items dentro de los pedidos
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    /**
     * Buscar items de un pedido específico
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);

    /**
     * Buscar si ya existe un item con el mismo producto en el mismo pedido
     * Útil para evitar duplicados
     */
    Optional<ItemPedido> findByPedidoIdAndProductoId(Long pedidoId, Long productoId);

    /**
     * Buscar todos los items que contengan un producto específico
     */
    List<ItemPedido> findByProductoId(Long productoId);

    /**
     * Consulta para obtener productos más vendidos con cantidades
     */
    @Query("SELECT ip.producto.nombre, SUM(ip.cantidad) as totalVendido " +
           "FROM ItemPedido ip " +
           "GROUP BY ip.producto.id, ip.producto.nombre " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidosConCantidad();

    /**
     * Verificar si existe un item con ese pedido y producto
     */
    boolean existsByPedidoIdAndProductoId(Long pedidoId, Long productoId);
}
