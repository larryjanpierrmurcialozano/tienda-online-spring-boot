package com.example.tienda.service;

import com.example.tienda.entity.Producto;
import com.example.tienda.entity.Categoria;
import com.example.tienda.repository.ProductoRepository;
import com.example.tienda.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * SERVICIO PRODUCTO
 * Maneja la lógica de negocio para productos y sus categorías
 */
@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * CREAR PRODUCTO
     */
    public Producto crearProducto(String nombre, BigDecimal precio, Integer stock) {
        // Validar que no existe producto con ese nombre
        if (productoRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + nombre);
        }

        Producto producto = new Producto(nombre, precio, stock);
        return productoRepository.save(producto);
    }

    /**
     * ASIGNAR CATEGORÍAS A PRODUCTO
     * Puede crear categorías nuevas o usar existentes
     */
    public Producto asignarCategorias(Long productoId, Set<String> nombresCategorias) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        for (String nombreCategoria : nombresCategorias) {
            // Buscar si la categoría ya existe, si no, crearla
            Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                    .orElseGet(() -> {
                        Categoria nuevaCategoria = new Categoria(nombreCategoria);
                        return categoriaRepository.save(nuevaCategoria);
                    });

            // Agregar la categoría al producto
            producto.addCategoria(categoria);
        }

        return productoRepository.save(producto);
    }

    /**
     * Buscar productos por categoría con paginación
     */
    @Transactional(readOnly = true)
    public Page<Producto> buscarPorCategoria(String nombreCategoria, Pageable pageable) {
        return productoRepository.findByCategoriasNombre(nombreCategoria, pageable);
    }

    /**
     * Listar productos con stock bajo
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo);
    }

    /**
     * Actualizar stock de un producto
     */
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    /**
     * Buscar producto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Listar todos los productos
     */
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
}
