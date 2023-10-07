package edu.gatech.cs6310.controller;

import edu.gatech.cs6310.domain.Drone;
import edu.gatech.cs6310.domain.FuelingStation;
import edu.gatech.cs6310.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DroneController {

    @Autowired
    private DroneService droneService;

    @PostMapping("/drone/{storeName}")
    public ResponseEntity createDrone(@PathVariable String storeName, @RequestParam String droneID, @RequestParam int capacity, @RequestParam int remainNumOfTrips, @RequestParam int fuelRate, @RequestParam int maxFuelCapacity, @RequestParam int currXCoordinate, @RequestParam int currYCoordinate) {
        try {
            droneService.createDrone(storeName, droneID, capacity, remainNumOfTrips, fuelRate, maxFuelCapacity, currXCoordinate, currYCoordinate);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/stores/{storeName}/drones")
    public ResponseEntity<List<Drone>> getDronesByStore(@PathVariable String storeName) {
        try {
            List<Drone> drones = droneService.getDronesByStore(storeName);
            return ResponseEntity.ok(drones);
        } catch (RuntimeException e) {
            List<Drone> errorList = new ArrayList<>();
            errorList.add(new Drone(null, e.getMessage(), 0, 0,0,0,0,0));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorList);
        }
    }


    @PostMapping("/drone/fly")
    public ResponseEntity<String> flyDrone(@RequestParam String storeName, @RequestParam String droneID, @RequestParam String account) {
        try {
            droneService.flyDrone(storeName, droneID, account);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/checkRefuelingOption")
    public ResponseEntity<List<FuelingStation>> checkRefuelingOption(@RequestParam String droneID, @RequestParam String storeName) {
        try {
            List<FuelingStation> fuelingStationList = droneService.checkRefuelingOption(droneID, storeName);
            return ResponseEntity.ok(fuelingStationList);
        } catch (IllegalArgumentException e) {
            List<FuelingStation> errorList = new ArrayList<>();
            errorList.add(new FuelingStation(e.getMessage(), 0, 0, null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorList);
        }
    }

    @PostMapping("/refuel")
    public ResponseEntity<String> refuelDrone(@RequestParam String droneID, @RequestParam String stationID, @RequestParam String storeName) {
        try {
            droneService.refuelDrone(droneID, stationID, storeName);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
