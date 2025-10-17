package com.example.tienda.controller;

import com.example.tienda.entity.Pedido;
import com.example.tienda.service.PedidoService;
import com.example.tienda.dto.TotalClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLADOR REST PARA PEDIDOS
 * Este es el controlador más complejo, maneja la creación de pedidos con items
 */
@RestController
@RequestMapping("/api")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * POST /api/clientes/{clienteId}/pedidos - Crear pedido con items
     * CUMPLE CON EL REQUISITO DEL TALLER
     */
    @PostMapping("/clientes/{clienteId}/pedidos")
    public ResponseEntity<Pedido> crearPedido(@PathVariable Long clienteId,
                                            @RequestBody CrearPedidoRequest request) {
        try {
            Pedido pedido = pedidoService.crearPedido(clienteId, request.getItems());
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/pedidos/{id} - Obtener pedido con items y totales
     * CUMPLE CON EL REQUISITO DEL TALLER
     */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPedidoConItems(id);
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/pedidos/{id}/estado?valor=ENVIADO - Cambiar estado del pedido
     * CUMPLE CON EL REQUISITO DEL TALLER
     */
    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id,
                                              @RequestParam String valor) {
        try {
            Pedido.EstadoPedido nuevoEstado = Pedido.EstadoPedido.valueOf(valor.toUpperCase());
            Pedido pedido = pedidoService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/pedidos/{id}/cancelar - Cancelar pedido y revertir stock
     */
    @PutMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/clientes/{clienteId}/pedidos - Listar pedidos de un cliente
     */
    @GetMapping("/clientes/{clienteId}/pedidos")
    public ResponseEntity<List<Pedido>> listarPedidosDeCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPedidosDeCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/reportes/totales-cliente - Reporte de totales por cliente
     */
    @GetMapping("/reportes/totales-cliente")
    public ResponseEntity<List<TotalClienteDTO>> obtenerTotalesPorCliente() {
        List<TotalClienteDTO> totales = pedidoService.obtenerTotalesPorCliente();
        return ResponseEntity.ok(totales);
    }

    // ===== CLASES AUXILIARES =====

    public static class CrearPedidoRequest {
        private List<PedidoService.ItemPedidoRequest> items;

        public List<PedidoService.ItemPedidoRequest> getItems() { return items; }
        public void setItems(List<PedidoService.ItemPedidoRequest> items) { this.items = items; }
    }
}
