package edu.gatech.cs6310;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"edu.gatech.cs6310.customer.controller", "edu.gatech.cs6310.customer.service", "edu.gatech.cs6310.customer.repository"})

public class GroceryExpressCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryExpressCustomerApplication.class, args);
    }

}
