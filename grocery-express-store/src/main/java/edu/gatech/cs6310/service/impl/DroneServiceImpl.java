package edu.gatech.cs6310.service.impl;

import edu.gatech.cs6310.domain.*;
import edu.gatech.cs6310.repository.DronePilotRepository;
import edu.gatech.cs6310.repository.DroneRepository;
import edu.gatech.cs6310.repository.FuelingStationRepository;
import edu.gatech.cs6310.repository.StoreRepository;
import edu.gatech.cs6310.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DroneServiceImpl implements DroneService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private DronePilotRepository dronePilotRepository;
    @Autowired
    private FuelingStationRepository fuelingStationRepository;

    @Override
    public Drone createDrone(String storeName, String droneID, int capacity, int remainNumOfTrips, int fuelRate, int maxFuelCapacity, int currXCoordinate, int currYCoordinate) {
        Store existingStore = storeRepository.findByStoreName(storeName);
        if (existingStore == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Drone existingDrone = droneRepository.findByStoreAndDroneID(storeRepository.findByStoreName(storeName), droneID);
        if (existingDrone != null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_already_exists");
        }
        Drone drone = new Drone(existingStore, droneID, capacity, remainNumOfTrips, fuelRate, maxFuelCapacity, currXCoordinate, currYCoordinate);
        return droneRepository.save(drone);
    }

    @Override
    public List<Drone> getDronesByStore(String storeName) {
        Optional<Store> optionalStore = Optional.ofNullable(storeRepository.findByStoreName(storeName));
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            return droneRepository.findByStore(store);
        } else {
            throw new RuntimeException("ERROR:store_identifier_does_not_exist");
        }
    }

    @Override
    public String flyDrone(String storeName, String droneID, String account) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Drone drone = droneRepository.findByStoreAndDroneID(store, droneID);
        if (drone == null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_does_not_exist");
        }

        DronePilot pilot = dronePilotRepository.findByAccount(account);
        if (pilot == null) {
            throw new IllegalArgumentException("ERROR:pilot_identifier_does_not_exist");
        }

        DronePilot currentPilot = drone.getPilotAssigned();
        Drone currentDrone = pilot.getDroneAssigned();

        if (currentPilot != null) {
            currentPilot.delDrone();
            drone.delPilot();
        }
        if (currentDrone != null) {
            currentDrone.delPilot();
            pilot.delDrone();
        }

        drone.assignPilot(pilot);
        pilot.assignDrone(drone);

        droneRepository.save(drone);
        dronePilotRepository.save(pilot);

        return "OK:change_completed";
    }

    @Override
    public List<FuelingStation> checkRefuelingOption(String droneID, String storeName) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Drone drone = droneRepository.findByStoreAndDroneID(store, droneID);
        if (drone == null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_does_not_exist");
        }

        int totalWeightOfDrone = 0;
        for (Order order : drone.getOrdersAssigned().values()) {
            totalWeightOfDrone += order.getTotalWeight();
        }

        List<FuelingStation> refuelingOptions = new ArrayList<>();

        for (FuelingStation fuelingStation : drone.getStore().getFuelingStations().values()) {
            int destinationX = fuelingStation.getxCoordinate();
            int destinationY = fuelingStation.getyCoordinate();
            double neededFuel = drone.getNeededFuel(destinationX, destinationY, totalWeightOfDrone);
            if (drone.getRemainFuel() >= neededFuel) {
                refuelingOptions.add(fuelingStation);
            }
        }

        if(refuelingOptions.isEmpty()) {
            throw new IllegalArgumentException("ERROR:drone_cannot_arrive_any_stations_or_store");
        }

        return refuelingOptions;
    }

    @Override
    public void refuelDrone(String droneID, String stationID, String storeName) {
        Store store = storeRepository.findByStoreName(storeName);
        if (store == null) {
            throw new IllegalArgumentException("ERROR:store_identifier_does_not_exist");
        }

        Drone drone = droneRepository.findByStoreAndDroneID(store, droneID);
        if (drone == null) {
            throw new IllegalArgumentException("ERROR:drone_identifier_does_not_exist");
        }

        FuelingStation fuelingStation = fuelingStationRepository.findByStationID(stationID);
        if (fuelingStation == null) {
            throw new IllegalArgumentException("ERROR:station_identifier_does_not_exist");
        }

        List<FuelingStation> refuelingOptions = checkRefuelingOption(droneID, drone.getStore().getName());
        if (!refuelingOptions.contains(fuelingStation)) {
            throw new IllegalArgumentException("ERROR:drone_cannot_arrive_this_station");
        }

        drone.setRemainFuel(drone.getMaxFuelCapacity());
        drone.setCurrXCoordinate(fuelingStation.getxCoordinate());
        drone.setCurrYCoordinate(fuelingStation.getyCoordinate());
        droneRepository.save(drone);
    }
}
