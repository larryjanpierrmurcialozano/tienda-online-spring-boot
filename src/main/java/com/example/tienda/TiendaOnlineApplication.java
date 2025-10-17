package com.example.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot
 * La anotación @SpringBootApplication hace que Spring configure automáticamente:
 * - El servidor web (Tomcat)
 * - La conexión a base de datos
 * - Los componentes de la aplicación
 */
@SpringBootApplication
public class TiendaOnlineApplication {

    /**
     * Método main - punto de entrada de la aplicación
     * SpringApplication.run() inicia toda la aplicación Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(TiendaOnlineApplication.class, args);
        System.out.println("🛒 Tienda Online iniciada correctamente!");
        System.out.println("📊 H2 Console disponible en: http://localhost:8080/h2-console");
        System.out.println("🌐 API REST disponible en: http://localhost:8080/api");
    }
}
