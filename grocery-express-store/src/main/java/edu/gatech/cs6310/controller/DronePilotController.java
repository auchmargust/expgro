package edu.gatech.cs6310.controller;

import edu.gatech.cs6310.domain.DronePilot;
import edu.gatech.cs6310.service.DronePilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
public class DronePilotController {
    @Autowired
    private DronePilotService dronePilotService;

    @PostMapping("/dronepilot")
    public ResponseEntity createDronePilotcreateDronePilot(@RequestParam String account, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phone, @RequestParam String taxID, @RequestParam String licenseId, @RequestParam int numOfSuccDel) {
        try {
            dronePilotService.createDronePilot(account, firstName, lastName, phone, taxID, licenseId, numOfSuccDel);
            return ResponseEntity.ok("OK:change_completed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/dronepilots")
    public ResponseEntity<List<DronePilot>> getAllPilots() {
        List<DronePilot> pilots = dronePilotService.getAllPilots();
        pilots.sort(Comparator.comparing(DronePilot::getAccount));
        return ResponseEntity.ok().body(pilots);
    }



}
