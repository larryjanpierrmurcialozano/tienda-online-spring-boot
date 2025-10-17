package com.example.tienda.repository;

import com.example.tienda.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORIO PRODUCTO
 * Con consultas específicas para la tienda online
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Buscar producto por nombre exacto
     */
    Optional<Producto> findByNombre(String nombre);

    /**
     * Buscar productos que contengan cierto texto en el nombre
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * CONSULTA CLAVE: Buscar productos por categoría
     * Esta es una de las consultas más importantes del taller
     */
    List<Producto> findByCategoriasNombre(String nombreCategoria);

    /**
     * Buscar productos por categoría con paginación
     * Útil para manejar muchos productos
     */
    Page<Producto> findByCategoriasNombre(String nombreCategoria, Pageable pageable);

    /**
     * Buscar productos con stock disponible
     */
    List<Producto> findByStockGreaterThan(Integer stock);

    /**
     * Buscar productos en un rango de precios
     */
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    /**
     * Buscar productos con stock bajo (menos de cierta cantidad)
     */
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);

    /**
     * Consulta para obtener productos más vendidos
     */
    @Query("SELECT p FROM Producto p JOIN ItemPedido ip ON p.id = ip.producto.id " +
           "GROUP BY p.id ORDER BY SUM(ip.cantidad) DESC")
    List<Producto> findProductosMasVendidos(Pageable pageable);

    /**
     * Verificar si existe un producto con ese nombre
     */
    boolean existsByNombre(String nombre);
}
