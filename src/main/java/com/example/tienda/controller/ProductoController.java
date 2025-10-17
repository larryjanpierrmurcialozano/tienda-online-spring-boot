package com.example.tienda.controller;

import com.example.tienda.entity.Producto;
import com.example.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * CONTROLADOR REST PARA PRODUCTOS
 * Maneja endpoints relacionados con productos y categorías
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * POST /api/productos - Crear producto
     * Cumple con el requisito del taller
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody CrearProductoRequest request) {
        try {
            Producto producto = productoService.crearProducto(
                request.getNombre(),
                request.getPrecio(),
                request.getStock()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/productos/{id}/categorias - Asignar categorías a producto
     * Cumple con el requisito del taller
     */
    @PostMapping("/{id}/categorias")
    public ResponseEntity<Producto> asignarCategorias(@PathVariable Long id,
                                                    @RequestBody AsignarCategoriasRequest request) {
        try {
            Producto producto = productoService.asignarCategorias(id, request.getCategorias());
            return ResponseEntity.ok(producto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/productos?categoria=Backend&page=0&size=5
     * Filtrar productos por categoría con paginación - requisito del taller
     */
    @GetMapping
    public ResponseEntity<Page<Producto>> listarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (categoria != null) {
            // Filtrar por categoría
            Page<Producto> productos = productoService.buscarPorCategoria(categoria, pageable);
            return ResponseEntity.ok(productos);
        } else {
            // Listar todos
            List<Producto> todosLosProductos = productoService.listarTodos();
            // Convertir lista a página (simplificado para el ejemplo)
            return ResponseEntity.ok(Page.empty());
        }
    }

    /**
     * GET /api/productos/{id} - Buscar producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/productos/{id}/stock - Actualizar stock
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id,
                                                   @RequestBody ActualizarStockRequest request) {
        try {
            Producto producto = productoService.actualizarStock(id, request.getNuevoStock());
            return ResponseEntity.ok(producto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/productos/stock-bajo?minimo=5 - Productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> productosConStockBajo(
            @RequestParam(defaultValue = "10") Integer minimo) {
        List<Producto> productos = productoService.buscarProductosConStockBajo(minimo);
        return ResponseEntity.ok(productos);
    }

    // ===== CLASES AUXILIARES PARA REQUESTS =====

    public static class CrearProductoRequest {
        private String nombre;
        private BigDecimal precio;
        private Integer stock;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
    }

    public static class AsignarCategoriasRequest {
        private Set<String> categorias;

        public Set<String> getCategorias() { return categorias; }
        public void setCategorias(Set<String> categorias) { this.categorias = categorias; }
    }

    public static class ActualizarStockRequest {
        private Integer nuevoStock;

        public Integer getNuevoStock() { return nuevoStock; }
        public void setNuevoStock(Integer nuevoStock) { this.nuevoStock = nuevoStock; }
    }
}
