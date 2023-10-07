package edu.gatech.cs6310.controller;

import edu.gatech.cs6310.domain.Coupon;
import edu.gatech.cs6310.domain.Item;
import edu.gatech.cs6310.domain.Store;
import edu.gatech.cs6310.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/store")
    public ResponseEntity<String> createStore(@RequestParam String name, @RequestParam int revenue, @RequestParam int storeXCoordinate, @RequestParam int storeYCoordinate) {
        Store existingStore = storeService.findByName(name);
        if (existingStore != null) {
            return ResponseEntity.badRequest().body("ERROR:store_identifier_already_exists");
        }
        Store newStore = storeService.create(name, revenue, storeXCoordinate, storeYCoordinate);
        return ResponseEntity.ok("OK:change_completed");
    }

    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @PostMapping("/store/sellItem")
    public ResponseEntity<String> addItemToStore(@RequestParam String storeName, @RequestParam String itemName, @RequestParam int weight) {
        try {
            storeService.addItemToStore(storeName, itemName, weight);
            return ResponseEntity.ok("OK:change_completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/store/items")
    public ResponseEntity<List<Item>> getAllItemsByStore(@RequestParam String storeName) {
        try {
            List<Item> items = storeService.getAllItemsByStore(storeName);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            List<Item> errorList = new ArrayList<>();
            errorList.add(new Item(e.getMessage(), 0, null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorList);
        }
    }

    @PostMapping("/transfer_order")
    public ResponseEntity<String> transferOrder(@RequestParam String storeName, @RequestParam String orderID, @RequestParam String droneID) {
        try {
            storeService.transferOrder(storeName, orderID, droneID);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/display_efficiency")
    public ResponseEntity<String> displayEfficiency() {
        StringBuilder sb = new StringBuilder();
        List<Store> stores = storeService.getAllStores();
        stores.forEach(store -> sb.append("name:").append(store.getName())
                .append(",purchases:").append(store.getPurchases())
                .append(",overloads:").append(store.getOverloads())
                .append(",transfers:").append(store.getTransfers())
                .append("\n"));
        sb.append("OK:display_completed");
        return ResponseEntity.ok(sb.toString());
    }

    @PostMapping("/store/makeFuelingStation")
    public ResponseEntity<String> makeFuelingStation(@RequestParam String stationID, @RequestParam int xCoordinate, @RequestParam int yCoordinate, @RequestParam String storeName) {
        try {
            storeService.makeFuelingStation(stationID, xCoordinate, yCoordinate, storeName);
            return ResponseEntity.ok("OK:change_completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deleteFuelingStation")
    public ResponseEntity<String> deleteFuelingStation(@RequestParam String stationID) {
        try {
            storeService.deleteFuelingStation(stationID);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/store/assignCoupon")
    public ResponseEntity<String> assignCoupon(@RequestParam String storeName, @RequestParam String account, @RequestParam String couponId, @RequestParam int couponReduction, @RequestParam int expiryPeriod) {
        try {
            storeService.assignCoupon(storeName, account, couponId, couponReduction, expiryPeriod);
            return ResponseEntity.ok("OK:coupon_assigned");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/store/display_coupon")
    public ResponseEntity<List<Coupon>> displayCoupons(@RequestParam String storeName) {
        try {
            List<Coupon> coupons = storeService.displayCoupons(storeName);
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            List<Coupon> errorList = new ArrayList<>();
            errorList.add(new Coupon(null, e.getMessage(), 0, 0));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorList);
        }
    }

}
