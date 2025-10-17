package com.example.tienda.service;

import com.example.tienda.entity.*;
import com.example.tienda.repository.*;
import com.example.tienda.dto.TotalClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SERVICIO PEDIDO
 * Contiene la lógica más compleja del sistema:
 * - Creación de pedidos con validación de stock
 * - Cálculo de totales
 * - Manejo de estados de pedidos
 */
@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    /**
     * CREAR PEDIDO - MÉTODO MÁS IMPORTANTE
     * Valida stock, crea items, calcula total y actualiza inventario
     */
    public Pedido crearPedido(Long clienteId, List<ItemPedidoRequest> itemsRequest) {
        // 1. Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));

        // 2. Crear el pedido
        Pedido pedido = new Pedido(cliente);

        // 3. Procesar cada item del pedido
        for (ItemPedidoRequest itemRequest : itemsRequest) {
            // 3.1 Validar que el producto existe
            Producto producto = productoRepository.findById(itemRequest.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + itemRequest.getProductoId()));

            // 3.2 Validar que no existe ya este producto en el pedido (constraint único)
            if (itemPedidoRepository.existsByPedidoIdAndProductoId(pedido.getId(), producto.getId())) {
                throw new IllegalArgumentException("El producto " + producto.getNombre() + " ya está en el pedido");
            }

            // 3.3 VALIDAR STOCK DISPONIBLE
            if (!producto.tieneStockSuficiente(itemRequest.getCantidad())) {
                throw new IllegalArgumentException("Stock insuficiente para " + producto.getNombre() +
                    ". Disponible: " + producto.getStock() + ", solicitado: " + itemRequest.getCantidad());
            }

            // 3.4 REDUCIR STOCK DEL PRODUCTO
            producto.reducirStock(itemRequest.getCantidad());

            // 3.5 Crear el item del pedido con el precio actual del producto
            ItemPedido item = new ItemPedido(pedido, producto, itemRequest.getCantidad(), producto.getPrecio());

            // 3.6 Agregar el item al pedido
            pedido.addItem(item);
        }

        // 4. CALCULAR TOTAL DEL PEDIDO
        pedido.calcularTotal();

        // 5. Guardar todo (cascade salvará los items)
        return pedidoRepository.save(pedido);
    }

    /**
     * Buscar pedido por ID con todos sus items cargados
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPedidoConItems(Long pedidoId) {
        return pedidoRepository.findWithItemsById(pedidoId);
    }

    /**
     * CAMBIAR ESTADO DE PEDIDO con validaciones de negocio
     */
    public Pedido cambiarEstado(Long pedidoId, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId));

        Pedido.EstadoPedido estadoActual = pedido.getEstado();

        // Validar transiciones de estado permitidas
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new IllegalArgumentException("No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    /**
     * CANCELAR PEDIDO y revertir stock
     */
    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findWithItemsById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId));

        if (!pedido.puedeSerCancelado()) {
            throw new IllegalArgumentException("El pedido en estado " + pedido.getEstado() + " no puede ser cancelado");
        }

        // REVERTIR STOCK de todos los productos
        for (ItemPedido item : pedido.getItems()) {
            Producto producto = item.getProducto();
            producto.aumentarStock(item.getCantidad());
            productoRepository.save(producto);
        }

        // Cambiar estado a cancelado
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    /**
     * Listar pedidos de un cliente
     */
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosDeCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByFechaDesc(clienteId);
    }

    /**
     * Obtener resumen de totales por cliente
     */
    @Transactional(readOnly = true)
    public List<TotalClienteDTO> obtenerTotalesPorCliente() {
        return pedidoRepository.findTotalPorCliente();
    }

    /**
     * Validar si una transición de estado es válida
     */
    private boolean esTransicionValida(Pedido.EstadoPedido estadoActual, Pedido.EstadoPedido nuevoEstado) {
        switch (estadoActual) {
            case NUEVO:
                return nuevoEstado == Pedido.EstadoPedido.PAGADO || nuevoEstado == Pedido.EstadoPedido.CANCELADO;
            case PAGADO:
                return nuevoEstado == Pedido.EstadoPedido.ENVIADO || nuevoEstado == Pedido.EstadoPedido.CANCELADO;
            case ENVIADO:
                return false; // Un pedido enviado no puede cambiar de estado
            case CANCELADO:
                return false; // Un pedido cancelado no puede cambiar de estado
            default:
                return false;
        }
    }

    /**
     * Clase auxiliar para recibir requests de items
     */
    public static class ItemPedidoRequest {
        private Long productoId;
        private Integer cantidad;

        public ItemPedidoRequest() {}

        public ItemPedidoRequest(Long productoId, Integer cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
