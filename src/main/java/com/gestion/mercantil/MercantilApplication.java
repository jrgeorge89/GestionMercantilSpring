
package com.gestion.mercantil;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MercantilApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercantilApplication.class, args);
    }

    @PostConstruct
    public void init(){
        // Configuración de Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
    }
    
}
