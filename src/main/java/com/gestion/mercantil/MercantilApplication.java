
package com.gestion.mercantil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MercantilApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercantilApplication.class, args);
    }
}
