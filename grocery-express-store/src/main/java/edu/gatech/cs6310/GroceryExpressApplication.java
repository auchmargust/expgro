package edu.gatech.cs6310;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"edu.gatech.cs6310.controller", "edu.gatech.cs6310.service", "edu.gatech.cs6310.repository"})

public class GroceryExpressApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryExpressApplication.class, args);
    }

}
