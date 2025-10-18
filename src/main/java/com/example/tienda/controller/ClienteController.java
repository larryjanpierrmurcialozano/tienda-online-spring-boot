package com.example.tienda.controller;

import com.example.tienda.entity.Cliente;
import com.example.tienda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLADOR REST PARA CLIENTES
 * Define los endpoints (URLs) que se pueden llamar desde el navegador o aplicaciones
 * @RestController le dice a Spring que esta clase maneja peticiones HTTP
 * @RequestMapping define la ruta base para todos los endpoints de esta clase
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * POST /api/clientes - Crear cliente con dirección
     * Este endpoint cumple con el requisito del taller
     */
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody CrearClienteRequest request) {
        try {
            Cliente cliente = clienteService.crearClienteConDireccion(
                request.getNombre(),
                request.getEmail(),
                request.getCalle(),
                request.getCiudad(),
                request.getPais(),
                request.getZip()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/clientes/{id} - Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarClienteConDireccion(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/clientes - Listar todos los clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarTodosConDirecciones();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/email/{email} - Buscar cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/clientes/{id}/direccion - Actualizar dirección
     */
    @PutMapping("/{id}/direccion")
    public ResponseEntity<Cliente> actualizarDireccion(@PathVariable Long id,
                                                     @RequestBody DireccionRequest request) {
        try {
            Cliente cliente = clienteService.actualizarDireccion(id,
                request.getCalle(), request.getCiudad(), request.getPais(), request.getZip());
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/clientes/{id} - Eliminar cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== CLASES AUXILIARES PARA REQUESTS =====

    /**
     * Clase para recibir datos del cliente en el POST
     */
    public static class CrearClienteRequest {
        private String nombre;
        private String email;
        private String calle;
        private String ciudad;
        private String pais;
        private String zip;

        // Getters y setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCalle() { return calle; }
        public void setCalle(String calle) { this.calle = calle; }
        public String getCiudad() { return ciudad; }
        public void setCiudad(String ciudad) { this.ciudad = ciudad; }
        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }
        public String getZip() { return zip; }
        public void setZip(String zip) { this.zip = zip; }
    }

    /**
     * Clase para recibir dato  s de dirección
     */
    public static class DireccionRequest {
        private String calle;
        private String ciudad;
        private String pais;
        private String zip;

        // Getters y setters
        public String getCalle() { return calle; }
        public void setCalle(String calle) { this.calle = calle; }
        public String getCiudad() { return ciudad; }
        public void setCiudad(String ciudad) { this.ciudad = ciudad; }
        public String getPais() { return pais; }
        public void setPais(String pais) { this.pais = pais; }
        public String getZip() { return zip; }
        public void setZip(String zip) { this.zip = zip; }
    }
}