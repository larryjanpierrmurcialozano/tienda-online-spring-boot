package com.example.tienda.repository;

import com.example.tienda.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * REPOSITORIO CATEGORIA
 * Manejo de categorías de productos
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Buscar categoría por nombre exacto
     */
    Optional<Categoria> findByNombre(String nombre);

    /**
     * Verificar si existe una categoría con ese nombre
     */
    boolean existsByNombre(String nombre);

    /**
     * Buscar categorías que contengan cierto texto
     */
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Consulta para obtener categorías con más productos
     */
    @Query("SELECT c FROM Categoria c JOIN c.productos p " +
           "GROUP BY c.id ORDER BY COUNT(p) DESC")
    List<Categoria> findCategoriasConMasProductos();

    /**
     * Buscar categorías que tengan al menos un producto
     */
    @Query("SELECT DISTINCT c FROM Categoria c WHERE SIZE(c.productos) > 0")
    List<Categoria> findCategoriasConProductos();
}
