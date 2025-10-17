package com.example.tienda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * CONTROLADOR PRINCIPAL - API INFO
 * Proporciona información sobre los endpoints disponibles
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * GET /api - Página de bienvenida con información de la API
     */
    @GetMapping
    public Map<String, Object> apiInfo() {
        Map<String, Object> info = new LinkedHashMap<>();

        info.put("mensaje", "🛒 Bienvenido a la API de Tienda Online");
        info.put("version", "1.0.0");
        info.put("descripcion", "API REST para gestión de tienda online con JPA");

        // Endpoints de Clientes
        Map<String, String> clientesEndpoints = new LinkedHashMap<>();
        clientesEndpoints.put("GET /api/clientes", "Listar todos los clientes");
        clientesEndpoints.put("POST /api/clientes", "Crear cliente con dirección");
        clientesEndpoints.put("GET /api/clientes/{id}", "Buscar cliente por ID");
        clientesEndpoints.put("PUT /api/clientes/{id}/direccion", "Actualizar dirección");

        // Endpoints de Productos
        Map<String, String> productosEndpoints = new LinkedHashMap<>();
        productosEndpoints.put("GET /api/productos", "Listar productos (con filtros)");
        productosEndpoints.put("POST /api/productos", "Crear producto");
        productosEndpoints.put("GET /api/productos/{id}", "Buscar producto por ID");
        productosEndpoints.put("POST /api/productos/{id}/categorias", "Asignar categorías");
        productosEndpoints.put("GET /api/productos?categoria=Frontend&page=0&size=5", "Filtrar por categoría");

        // Endpoints de Pedidos
        Map<String, String> pedidosEndpoints = new LinkedHashMap<>();
        pedidosEndpoints.put("POST /api/clientes/{clienteId}/pedidos", "Crear pedido con items");
        pedidosEndpoints.put("GET /api/pedidos/{id}", "Ver pedido con items");
        pedidosEndpoints.put("PUT /api/pedidos/{id}/estado?valor=ENVIADO", "Cambiar estado");
        pedidosEndpoints.put("PUT /api/pedidos/{id}/cancelar", "Cancelar pedido");

        // Endpoints de Reportes
        Map<String, String> reportesEndpoints = new LinkedHashMap<>();

        info.put("endpoints", Map.of(
                "clientes", clientesEndpoints,
                "productos", productosEndpoints,
                "pedidos", pedidosEndpoints,
                "reportes", reportesEndpoints
        ));

        info.put("herramientas", Map.of(
                "h2_console", "http://localhost:8080/h2-console",
                "documentacion", "Ver README.md del proyecto"
        ));

        return info;
    }
}




