package com.example.tienda.repository;

import com.example.tienda.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * REPOSITORIO CLIENTE
 * JpaRepository nos da métodos automáticos como save(), findById(), findAll(), delete()
 * También podemos crear métodos personalizados
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Buscar cliente por email
     * Spring Data JPA crea automáticamente la consulta basándose en el nombre del método
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Verificar si existe un cliente con ese email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar clientes que contengan cierto texto en el nombre (ignorando mayúsculas)
     */
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar cliente por email CON su dirección cargada
     * @EntityGraph evita el problema N+1 al cargar relaciones LAZY
     */
    @EntityGraph(attributePaths = "direccion")
    Optional<Cliente> findWithDireccionByEmail(String email);

    /**
     * Listar todos los clientes CON sus direcciones
     * Útil para mostrar listados completos sin problemas de performance
     */
    @EntityGraph(attributePaths = "direccion")
    @Query("SELECT c FROM Cliente c")
    List<Cliente> findAllWithDirecciones();

    /**
     * Consulta personalizada: buscar clientes que tengan pedidos
     * @Query nos permite escribir JPQL (similar a SQL pero usando nombres de entidades)
     */
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.pedidos p")
    List<Cliente> findClientesConPedidos();

    /**
     * Buscar clientes por ciudad de su dirección
     */
    @Query("SELECT c FROM Cliente c JOIN c.direccion d WHERE d.ciudad = :ciudad")
    List<Cliente> findByCiudad(@Param("ciudad") String ciudad);
}
