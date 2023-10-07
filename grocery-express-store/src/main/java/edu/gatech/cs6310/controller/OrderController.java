package edu.gatech.cs6310.controller;

import edu.gatech.cs6310.domain.Order;
import edu.gatech.cs6310.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/start_order")
    public ResponseEntity<Object> startOrder(@RequestParam String storeName, @RequestParam String orderID, @RequestParam String droneID, @RequestParam String account) {
        try {
            Order order = orderService.startOrder(storeName, orderID, droneID, account);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/store/{storeName}/orders")
    public ResponseEntity<List<Order>> getOrdersByStore(@PathVariable String storeName) {
        try {
            List<Order> orders = orderService.getOrdersByStore(storeName);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            List<Order> errorList = new ArrayList<>();
            errorList.add(new Order(e.getMessage(), null, null, null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorList);
        }
    }

    @PostMapping("/request_item")
    public ResponseEntity<Object> requestItem(@RequestParam String storeName, @RequestParam String orderID, @RequestParam String itemName, @RequestParam int quantity, @RequestParam int unitPrice) {
        try {
            orderService.requestItem(storeName, orderID, itemName, quantity, unitPrice);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/purchase_order")
    public ResponseEntity<String> purchaseOrder(@RequestParam String storeName, @RequestParam String orderID) {
        try {
            orderService.purchaseOrder(storeName, orderID);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel_order")
    public ResponseEntity<String> cancelOrder(@RequestParam String storeName, @RequestParam String orderID) {
        try {
            orderService.cancelOrder(storeName, orderID);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}