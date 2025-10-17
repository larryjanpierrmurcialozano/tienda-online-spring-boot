package com.example.tienda.service;

import com.example.tienda.entity.Cliente;
import com.example.tienda.entity.Direccion;
import com.example.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * SERVICIO CLIENTE
 * Contiene la lógica de negocio para manejar clientes
 * @Service le dice a Spring que esta clase contiene lógica de negocio
 * @Transactional asegura que las operaciones de BD se hagan correctamente
 */
@Service
@Transactional
public class ClienteService {

    // Inyección de dependencias - Spring nos da automáticamente el repositorio
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * CREAR CLIENTE CON DIRECCIÓN
     * Esta es una operación importante: crear cliente y dirección en una sola transacción
     */
    public Cliente crearClienteConDireccion(String nombre, String email,
                                          String calle, String ciudad, String pais, String zip) {
        // 1. Validar que no existe cliente con ese email
        if (clienteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + email);
        }

        // 2. Crear el cliente
        Cliente cliente = new Cliente(nombre, email);

        // 3. Crear la dirección
        Direccion direccion = new Direccion(calle, ciudad, pais, zip);

        // 4. Establecer la relación bidireccional
        cliente.setDireccion(direccion);

        // 5. Guardar (cascade=ALL guardará también la dirección)
        return clienteRepository.save(cliente);
    }

    /**
     * Buscar cliente por ID con su dirección cargada
     */
    @Transactional(readOnly = true) // Optimización para consultas de solo lectura
    public Optional<Cliente> buscarClienteConDireccion(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /**
     * Listar todos los clientes con sus direcciones
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodosConDirecciones() {
        return clienteRepository.findAllWithDirecciones();
    }

    /**
     * Actualizar dirección de un cliente
     */
    public Cliente actualizarDireccion(Long clienteId, String calle, String ciudad, String pais, String zip) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));

        if (cliente.getDireccion() != null) {
            // Actualizar dirección existente
            Direccion direccion = cliente.getDireccion();
            direccion.setCalle(calle);
            direccion.setCiudad(ciudad);
            direccion.setPais(pais);
            direccion.setZip(zip);
        } else {
            // Crear nueva dirección
            Direccion nuevaDireccion = new Direccion(calle, ciudad, pais, zip);
            cliente.setDireccion(nuevaDireccion);
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Eliminar cliente (también eliminará su dirección por orphanRemoval=true)
     */
    public void eliminarCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId);
        }
        clienteRepository.deleteById(clienteId);
    }

    /**
     * Buscar clientes por ciudad
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorCiudad(String ciudad) {
        return clienteRepository.findByCiudad(ciudad);
    }

    /**
     * Listar clientes que tienen pedidos
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarClientesConPedidos() {
        return clienteRepository.findClientesConPedidos();
    }
}
