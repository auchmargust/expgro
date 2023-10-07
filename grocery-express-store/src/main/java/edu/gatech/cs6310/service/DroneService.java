package edu.gatech.cs6310.service;

import edu.gatech.cs6310.domain.Drone;
import edu.gatech.cs6310.domain.FuelingStation;

import java.util.List;

public interface DroneService {
    Drone createDrone(String storeName, String droneID, int capacity, int remainNumOfTrips, int fuelRate, int maxFuelCapacity, int currXCoordinate, int currYCoordinate);

    List<Drone> getDronesByStore(String storeName);
    String flyDrone(String storeName, String droneID, String account);
    List<FuelingStation> checkRefuelingOption(String droneID, String storeName);
    void refuelDrone(String droneID, String stationID, String storeName);
}
